package com.company.logistics.tests.commands.listing;

import com.company.logistics.commands.CommandsConstants;
import com.company.logistics.commands.listing.ListRoutesWithNoAssignedTrucksCommand;
import com.company.logistics.enums.UserRole;
import com.company.logistics.exceptions.InvalidUserInputException;
import com.company.logistics.models.contracts.Route;
import com.company.logistics.models.contracts.Truck;
import com.company.logistics.models.contracts.User;
import com.company.logistics.models.delivery.RouteImpl;
import com.company.logistics.models.vehicles.TruckImpl;
import com.company.logistics.repositories.contracts.RouteRepository;
import com.company.logistics.repositories.contracts.UserRepository;
import com.company.logistics.utils.ListingHelpers;
import com.company.logistics.utils.PrintConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class ListRoutesWithNoAssignedTrucksCommandTest {
    private static final List<String> parameters = new ArrayList<>();
    private static final List<Route> EMPTY_LIST = new ArrayList<>();

    private RouteRepository mockRouteRepository;
    private RouteRepository mockRouteRepositoryWithAllTrucksAssigned;
    private RouteRepository mockEmptyRouteRepository;
    private UserRepository mockUserRepository;
    private User mockUser;

    private static Route mockRoteOne = mock(RouteImpl.class);
    private static Route mockRoteTwo = mock(RouteImpl.class);
    private static Truck truck;

    private static final List<Route> VALID_LIST = List.of(mockRoteOne, mockRoteTwo);
    private static final List<Route> VALID_LIST_NO_TRUCK = List.of(mockRoteTwo);
    private static final List<Route> VALID_LIST_ALL_TRUCK = List.of(mockRoteOne);

    private ListRoutesWithNoAssignedTrucksCommand command;
    private ListRoutesWithNoAssignedTrucksCommand commandWithAllTrucksAssigned;
    private ListRoutesWithNoAssignedTrucksCommand commandEmpty;

    @BeforeEach
    public void setUp() {
        mockRouteRepository = mock(RouteRepository.class);
        mockRouteRepositoryWithAllTrucksAssigned = mock(RouteRepository.class);
        mockEmptyRouteRepository = mock(RouteRepository.class);
        mockUserRepository = mock(UserRepository.class);
        mockUser = mock(User.class);
        truck = new TruckImpl(1, "silsil", 125, 122);

        when(mockRoteOne.print()).thenReturn("print route with truck");
        when(mockRoteOne.getAssignedTruck()).thenReturn(Optional.ofNullable(truck));

        when(mockRoteTwo.print()).thenReturn("print route without truck");
        when(mockRoteTwo.getAssignedTruck()).thenReturn(null);

        when(mockUserRepository.getLoggedInUser()).thenReturn(mockUser);
        when(mockUser.getRole()).thenReturn(UserRole.EMPLOYEE);

        when(mockRouteRepository.getRoutes()).thenReturn(VALID_LIST);
        when(mockRouteRepositoryWithAllTrucksAssigned.getRoutes()).thenReturn(VALID_LIST_ALL_TRUCK);
        when(mockEmptyRouteRepository.getRoutes()).thenReturn(EMPTY_LIST);

        command = new ListRoutesWithNoAssignedTrucksCommand(mockRouteRepository, mockUserRepository);
        commandWithAllTrucksAssigned = new ListRoutesWithNoAssignedTrucksCommand(mockRouteRepositoryWithAllTrucksAssigned, mockUserRepository);
        commandEmpty = new ListRoutesWithNoAssignedTrucksCommand(mockEmptyRouteRepository, mockUserRepository);
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
    public void execute_Should_ReturnCorrectString_When_NoRoutesInList() {
        when(mockEmptyRouteRepository.getRoutes()).thenReturn(EMPTY_LIST);

        List<String> parameters = new ArrayList<>();

        // Act
        String result = commandEmpty.execute(parameters);

        // Assert
        String expected = String.format(
                CommandsConstants.NO_ROUTES_WITHOUT_TRUCKS
        );

        Assertions.assertEquals(expected, result);
        verify(mockEmptyRouteRepository, times(1)).getRoutes();
    }

    @Test
    public void execute_Should_ReturnCorrectString_When_NoRoutesInListWithoutTrucks() {
        // Act
        String result = commandWithAllTrucksAssigned.execute(parameters);

        // Assert
        String expected = String.format(
                CommandsConstants.NO_ROUTES_WITHOUT_TRUCKS
        );

        Assertions.assertEquals(expected, result);
        verify(mockRouteRepositoryWithAllTrucksAssigned, times(1)).getRoutes();
    }

    @Test
    public void execute_Should_ReturnCorrectString_When_ListingRoutesWithNoTrucks() {
        String result = command.execute(parameters);

        String expected = PrintConstants.LINE_BREAK + "\n" + ListingHelpers.elementsToString(VALID_LIST_NO_TRUCK);

        Assertions.assertEquals(expected, result);
        verify(mockRouteRepository, times(1)).getRoutes();
    }

}
