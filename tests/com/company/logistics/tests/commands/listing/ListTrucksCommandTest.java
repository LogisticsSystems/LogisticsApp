package com.company.logistics.tests.commands.listing;

import com.company.logistics.commands.CommandsConstants;
import com.company.logistics.commands.listing.ListTrucksCommand;
import com.company.logistics.enums.UserRole;
import com.company.logistics.exceptions.InvalidUserInputException;
import com.company.logistics.models.contracts.Truck;
import com.company.logistics.models.contracts.User;
import com.company.logistics.models.vehicles.TruckImpl;
import com.company.logistics.repositories.contracts.TruckRepository;
import com.company.logistics.repositories.contracts.UserRepository;
import com.company.logistics.utils.ListingHelpers;
import com.company.logistics.utils.PrintConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ListTrucksCommandTest {
    private static final List<Truck> VALID_LIST = List.of(
            new TruckImpl(1, "string", 120, 120),
            new TruckImpl(2, "second", 120, 120)
    );
    private static final List<Truck> EMPTY_LIST = new ArrayList<>();

    private TruckRepository mockTruckRepository;
    private TruckRepository mockEmptyTruckRepository;
    private UserRepository mockUserRepository;
    private User mockUser;

    private ListTrucksCommand command;
    private ListTrucksCommand commandEmpty;

    @BeforeEach
    public void setUp() {
        mockTruckRepository = mock(TruckRepository.class);
        mockEmptyTruckRepository = mock(TruckRepository.class);
        mockUserRepository = mock(UserRepository.class);
        mockUser = mock(User.class);

        when(mockUserRepository.getLoggedInUser()).thenReturn(mockUser);
        when(mockUser.getRole()).thenReturn(UserRole.EMPLOYEE);

        when(mockTruckRepository.getTrucks()).thenReturn(VALID_LIST);
        when(mockEmptyTruckRepository.getTrucks()).thenReturn(EMPTY_LIST);

        command = new ListTrucksCommand(mockTruckRepository, mockUserRepository);
        commandEmpty = new ListTrucksCommand(mockEmptyTruckRepository, mockUserRepository);
    }

    // Test role
    @Test
    public void execute_Should_ThrowException_When_UserIsManager() {
        when(mockUser.getRole()).thenReturn(UserRole.MANAGER);

        List<String> parameters = List.of("5", "10");

        Exception ex = Assertions.assertThrows(
                InvalidUserInputException.class,
                () -> command.execute(parameters)
        );
    }

    // Test that command works
    @Test
    public void execute_Should_ReturnCorrectString_When_NoTrucksInList() {
        when(mockTruckRepository.getTrucks()).thenReturn(EMPTY_LIST);

        List<String> parameters = new ArrayList<>();

        // Act
        String result = commandEmpty.execute(parameters);

        // Assert
        String expected = String.format(
                CommandsConstants.NONE_FOUND_MESSAGE, "trucks"
        );

        Assertions.assertEquals(expected, result);
        verify(mockTruckRepository, times(1)).getTrucks();
    }

    @Test
    public void execute_Should_ReturnCorrectString_When_ListingTrucks() {
        List<String> parameters = new ArrayList<>();

        String result = command.execute(parameters);

        String expected = PrintConstants.LINE_BREAK + "\n" + ListingHelpers.elementsToString(VALID_LIST);

        Assertions.assertEquals(expected, result);
        verify(mockTruckRepository, times(1)).getTrucks();
    }
}
