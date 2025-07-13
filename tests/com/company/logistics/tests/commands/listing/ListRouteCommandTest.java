package com.company.logistics.tests.commands.listing;

import com.company.logistics.commands.CommandsConstants;
import com.company.logistics.commands.listing.ListRoutesCommand;
import com.company.logistics.enums.City;
import com.company.logistics.enums.UserRole;
import com.company.logistics.exceptions.InvalidUserInputException;
import com.company.logistics.models.contracts.Route;
import com.company.logistics.models.contracts.User;
import com.company.logistics.models.delivery.RouteImpl;
import com.company.logistics.repositories.contracts.RouteRepository;
import com.company.logistics.repositories.contracts.UserRepository;
import com.company.logistics.utils.ListingHelpers;
import com.company.logistics.utils.PrintConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ListRouteCommandTest {
    private static final List<City> CITIES = List.of(City.MEL, City.ADL);
    private static final List<Route> EMPTY_LIST = new ArrayList<>();

    private RouteRepository mockRouteRepository;
    private RouteRepository mockEmptyRouteRepository;
    private UserRepository mockUserRepository;
    private User mockUser;

    private static Route mockRoteOne = mock(RouteImpl.class);
    private static Route mockRoteTwo = mock(RouteImpl.class);

    private static final List<Route> VALID_LIST = List.of(mockRoteOne, mockRoteTwo);

    private ListRoutesCommand command;
    private ListRoutesCommand commandEmpty;

    @BeforeEach
    public void setUp() {
        mockRouteRepository = mock(RouteRepository.class);
        mockEmptyRouteRepository = mock(RouteRepository.class);
        mockUserRepository = mock(UserRepository.class);
        mockUser = mock(User.class);

        when(mockRoteOne.getLocations()).thenReturn(CITIES);
        when(mockRoteOne.getAssignedPackages()).thenReturn(new ArrayList<>());
        when(mockRoteOne.getAssignedTruck()).thenReturn(null);
        when(mockRoteOne.getSchedule()).thenReturn(List.of(LocalDateTime.now(), LocalDateTime.now()));
        when(mockRoteOne.getDepartureTime()).thenReturn(LocalDateTime.now());
        when(mockRoteOne.getId()).thenReturn(1);

        when(mockRoteTwo.getLocations()).thenReturn(CITIES);
        when(mockRoteTwo.getAssignedPackages()).thenReturn(new ArrayList<>());
        when(mockRoteTwo.getAssignedTruck()).thenReturn(null);
        when(mockRoteTwo.getSchedule()).thenReturn(List.of(LocalDateTime.now(), LocalDateTime.now()));
        when(mockRoteTwo.getDepartureTime()).thenReturn(LocalDateTime.now());
        when(mockRoteTwo.getId()).thenReturn(2);

        when(mockUserRepository.getLoggedInUser()).thenReturn(mockUser);
        when(mockUser.getRole()).thenReturn(UserRole.EMPLOYEE);

        when(mockRouteRepository.getRoutes()).thenReturn(VALID_LIST);
        when(mockEmptyRouteRepository.getRoutes()).thenReturn(EMPTY_LIST);

        command = new ListRoutesCommand(mockRouteRepository, mockUserRepository);
        commandEmpty = new ListRoutesCommand(mockEmptyRouteRepository, mockUserRepository);
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
                CommandsConstants.NONE_FOUND_MESSAGE, "routes"
        );

        Assertions.assertEquals(expected, result);
        verify(mockEmptyRouteRepository, times(1)).getRoutes();
    }

    @Test
    public void execute_Should_ReturnCorrectString_When_ListingRoutes() {
        List<String> parameters = new ArrayList<>();

        String result = command.execute(parameters);

        String expected = PrintConstants.LINE_BREAK + "\n" + ListingHelpers.elementsToString(VALID_LIST);

        Assertions.assertEquals(expected, result);
        verify(mockRouteRepository, times(1)).getRoutes();
    }
}
