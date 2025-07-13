package com.company.logistics.tests.commands.listing;

import com.company.logistics.commands.CommandsConstants;
import com.company.logistics.commands.listing.ListPackagesCommand;
import com.company.logistics.enums.City;
import com.company.logistics.enums.UserRole;
import com.company.logistics.exceptions.InvalidUserInputException;
import com.company.logistics.models.contracts.DeliveryPackage;
import com.company.logistics.models.contracts.User;
import com.company.logistics.models.delivery.DeliveryPackageImpl;
import com.company.logistics.repositories.contracts.PackageRepository;
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

public class ListPackageCommandTest {
    private static final List<DeliveryPackage> VALID_LIST = List.of(
              new DeliveryPackageImpl(1, City.MEL, City.ADL, 15000, "asdfg "),
              new DeliveryPackageImpl(2, City.ADL, City.DAR, 15000, "asdfg ")
    );
    private static final List<DeliveryPackage> EMPTY_LIST = new ArrayList<>();

    private PackageRepository mockPackageRepository;
    private PackageRepository mockEmptyPackageRepository;
    private UserRepository mockUserRepository;
    private User mockUser;

    private ListPackagesCommand command;
    private ListPackagesCommand commandEmpty;

    @BeforeEach
    public void setUp() {
        mockPackageRepository = mock(PackageRepository.class);
        mockEmptyPackageRepository = mock(PackageRepository.class);
        mockUserRepository = mock(UserRepository.class);
        mockUser = mock(User.class);

        when(mockUserRepository.getLoggedInUser()).thenReturn(mockUser);
        when(mockUser.getRole()).thenReturn(UserRole.EMPLOYEE);

        when(mockPackageRepository.getPackages()).thenReturn(VALID_LIST);
        when(mockEmptyPackageRepository.getPackages()).thenReturn(EMPTY_LIST);

        command = new ListPackagesCommand(mockPackageRepository, mockUserRepository);
        commandEmpty = new ListPackagesCommand(mockEmptyPackageRepository, mockUserRepository);
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
    public void execute_Should_ReturnCorrectString_When_NoPackagesInList() {
        when(mockPackageRepository.getPackages()).thenReturn(EMPTY_LIST);

        List<String> parameters = new ArrayList<>();

        // Act
        String result = commandEmpty.execute(parameters);

        // Assert
        String expected = String.format(
                CommandsConstants.NONE_FOUND_MESSAGE, "packages"
        );

        Assertions.assertEquals(expected, result);
        verify(mockPackageRepository, times(1)).getPackages();
    }

    @Test
    public void execute_Should_ReturnCorrectString_When_ListingPackages() {
        List<String> parameters = new ArrayList<>();

        String result = command.execute(parameters);

        String expected = PrintConstants.LINE_BREAK + "\n" + ListingHelpers.elementsToString(VALID_LIST);

        Assertions.assertEquals(expected, result);
        verify(mockPackageRepository, times(1)).getPackages();
    }
}
