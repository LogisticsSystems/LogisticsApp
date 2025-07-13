package com.company.logistics.tests.commands.querying;

import com.company.logistics.commands.CommandsConstants;
import com.company.logistics.commands.querying.FindRouteCommand;
import com.company.logistics.enums.City;
import com.company.logistics.enums.UserRole;
import com.company.logistics.exceptions.InvalidUserInputException;
import com.company.logistics.models.contracts.Route;
import com.company.logistics.models.contracts.User;
import com.company.logistics.models.delivery.RouteImpl;
import com.company.logistics.repositories.contracts.RouteRepository;
import com.company.logistics.repositories.contracts.UserRepository;
import com.company.logistics.utils.ErrorMessages;
import com.company.logistics.utils.ListingHelpers;
import com.company.logistics.utils.ParsingHelpers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class FindRouteTests {
    private static final int EXPECTED_PARAMETER_COUNT = 2;

    private static final List<String> parameters = List.of("MEL", "ADL");
    private static final List<Route> EMPTY_LIST = new ArrayList<>();


    private RouteRepository mockRouteRepository;
    private RouteRepository mockEmptyRouteRepository;
    private UserRepository mockUserRepository;
    private User mockUser;

    private static Route mockRoteViable = mock(RouteImpl.class);

    private static final List<Route> VALID_LIST = List.of(mockRoteViable);

    private FindRouteCommand command;
    private FindRouteCommand commandEmpty;

    @BeforeEach
    public void setUp() {
        mockRouteRepository = mock(RouteRepository.class);
        mockEmptyRouteRepository = mock(RouteRepository.class);
        mockUserRepository = mock(UserRepository.class);
        mockUser = mock(User.class);

        when(mockRoteViable.print()).thenReturn("print viable route");

        when(mockUserRepository.getLoggedInUser()).thenReturn(mockUser);
        when(mockUser.getRole()).thenReturn(UserRole.EMPLOYEE);

        when(mockRouteRepository.findRoutes(City.MEL, City.ADL)).thenReturn(VALID_LIST);
        when(mockEmptyRouteRepository.findRoutes(City.MEL, City.ADL)).thenReturn(EMPTY_LIST);

        command = new FindRouteCommand(mockRouteRepository, mockUserRepository);
        commandEmpty = new FindRouteCommand(mockEmptyRouteRepository, mockUserRepository);
    }

    // Test parameter count
    @Test
    public void execute_Should_ThrowException_When_NotEnoughParameters() {
        List<String> parameters = List.of("MEL");

        Exception ex = Assertions.assertThrows(
                InvalidUserInputException.class,
                () -> command.execute(parameters)
        );

        String expectedMessage = String.format(ErrorMessages.INVALID_ARGUMENTS_COUNT,
                EXPECTED_PARAMETER_COUNT,
                EXPECTED_PARAMETER_COUNT -1);
        Assertions.assertEquals(expectedMessage, ex.getMessage());
    }

    @Test
    public void execute_Should_ThrowException_When_TooManyParameters() {
        List<String> parameters = List.of("MEL", "ADL", "DAR");

        Exception ex = Assertions.assertThrows(
                InvalidUserInputException.class,
                () -> command.execute(parameters)
        );

        String expectedMessage = String.format(ErrorMessages.INVALID_ARGUMENTS_COUNT,
                EXPECTED_PARAMETER_COUNT,
                EXPECTED_PARAMETER_COUNT + 1);
        Assertions.assertEquals(expectedMessage, ex.getMessage());
    }

    // Test role
    @Test
    public void execute_Should_ThrowException_When_UserIsManager() {
        when(mockUser.getRole()).thenReturn(UserRole.MANAGER);

        Exception ex = Assertions.assertThrows(
                InvalidUserInputException.class,
                () -> command.execute(parameters)
        );

        String expectedMessage = String.format(
                CommandsConstants.COMMAND_UNAVAILABLE_FOR_USER,
                UserRole.MANAGER
        );

        Assertions.assertEquals(expectedMessage, ex.getMessage());
    }

    @Test
    public void execute_Should_ThrowException_When_UserIsDataAnalyst() {
        when(mockUser.getRole()).thenReturn(UserRole.DATA_ANALYST);

        Exception ex = Assertions.assertThrows(
                InvalidUserInputException.class,
                () -> command.execute(parameters)
        );

        String expectedMessage = String.format(
                CommandsConstants.COMMAND_UNAVAILABLE_FOR_USER,
                UserRole.DATA_ANALYST
        );

        Assertions.assertEquals(expectedMessage, ex.getMessage());
    }

    //Test that parameters parse correctly
    @Test
    public void execute_Should_ThrowException_When_FirstParameterIsNotCity() {
        List<String> parameters = List.of("dsada", "ADL");

        Exception ex = Assertions.assertThrows(
                InvalidUserInputException.class,
                () -> command.execute(parameters)
        );

        String expectedMessage = ParsingHelpers.printEnum(City.class);
        Assertions.assertEquals(expectedMessage, ex.getMessage());
    }


    @Test
    public void execute_Should_ThrowException_When_SecondParameterIsNotCity() {
        List<String> parameters = List.of("MEL", "dsfdsaf");

        Exception ex = Assertions.assertThrows(
                InvalidUserInputException.class,
                () -> command.execute(parameters)
        );

        String expectedMessage = ParsingHelpers.printEnum(City.class);
        Assertions.assertEquals(expectedMessage, ex.getMessage());
    }


    // Test that command works
    @Test
    public void execute_Should_ReturnCorrectString_When_NoRoutesInList() {
        // Act
        String result = commandEmpty.execute(parameters);

        // Assert
        String expected = String.format(
                CommandsConstants.NO_MATCHING_ROUTES_MESSAGE, parameters.get(0), parameters.get(1)
        );

        Assertions.assertEquals(expected, result);
        verify(mockEmptyRouteRepository, times(1)).findRoutes(City.MEL, City.ADL);
    }

    @Test
    public void execute_Should_ReturnCorrectString_When_ListingViableRoutes() {
        String result = command.execute(parameters);

        String expected = ListingHelpers.elementsToString(VALID_LIST);

        Assertions.assertEquals(expected, result);
        verify(mockRouteRepository, times(1)).findRoutes(City.MEL, City.ADL);
    }
}
