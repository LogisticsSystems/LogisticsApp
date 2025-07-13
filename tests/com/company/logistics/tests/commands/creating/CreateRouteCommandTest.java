package com.company.logistics.tests.commands.creating;

import com.company.logistics.commands.CommandsConstants;
import com.company.logistics.commands.creating.CreateRouteCommand;
import com.company.logistics.enums.City;
import com.company.logistics.enums.UserRole;
import com.company.logistics.exceptions.InvalidUserInputException;
import com.company.logistics.models.contracts.Route;
import com.company.logistics.models.contracts.User;
import com.company.logistics.repositories.contracts.UserRepository;
import com.company.logistics.services.routing.management.RouteCreationService;
import com.company.logistics.utils.ErrorMessages;
import com.company.logistics.utils.ParsingHelpers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class CreateRouteCommandTest {
    public static final int EXPECTED_PARAMETER_COUNT = 2;

    private static final List<City> VALID_LOCATIONS = List.of(City.MEL, City.ADL);
    private static final LocalDateTime VALID_DATE = LocalDateTime.now();

    private static final String LOCATIONS_RAW = "MEL,ADL";
    private static final String DATE_RAW = VALID_DATE.toString();
    private static List<String> parameters = List.of(LOCATIONS_RAW, DATE_RAW);

    private RouteCreationService mockService;
    private UserRepository mockUserRepository;
    private User mockUser;

    private CreateRouteCommand command;

    @BeforeEach
    public void setUp() {
        mockService = mock(RouteCreationService.class);
        mockUserRepository = mock(UserRepository.class);
        mockUser = mock(User.class);

        when(mockUserRepository.getLoggedInUser()).thenReturn(mockUser);
        when(mockUser.getRole()).thenReturn(UserRole.EMPLOYEE);

        command = new CreateRouteCommand(mockService, mockUserRepository);
    }

    // Test parameter count
    @Test
    public void execute_Should_ThrowException_When_NotEnoughParameters() {
        List<String> parameters = List.of("5");

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
        List<String> parameters = List.of("5", "5", "5");

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

        List<String> parameters = List.of("5", "10");

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

        List<String> parameters = List.of("5", "10");

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
    public void execute_Should_ThrowException_When_FirstParameterIsNotCities() {
        List<String> parameters = List.of("asd", DATE_RAW);

        Exception ex = Assertions.assertThrows(
                InvalidUserInputException.class,
                () -> command.execute(parameters)
        );

        String expectedMessage = ParsingHelpers.printEnum(City.class);
        Assertions.assertEquals(expectedMessage, ex.getMessage());
    }

    @Test
    public void execute_Should_ThrowException_When_SecondParameterIsNotDate() {
        List<String> parameters = List.of(LOCATIONS_RAW, "asd");

        Exception ex = Assertions.assertThrows(
                InvalidUserInputException.class,
                () -> command.execute(parameters)
        );

        String expectedMessage = ErrorMessages.INCORRECT_DATE_TIME_INPUT;
        Assertions.assertEquals(expectedMessage, ex.getMessage());
    }

    // Test that command works
    @Test
    public void execute_Should_CreateRoute_When_UserIsEmployeeAndParametersAreValid() {
        // Arrange
        Route mockRoute = mock(Route.class);
        when(mockRoute.getId()).thenReturn(1);

        when(mockService.createRoute(VALID_LOCATIONS, VALID_DATE)).thenReturn(mockRoute);

        // Act
        String result = command.execute(parameters);

        // Assert
        String expected = String.format(
                CommandsConstants.ROUTE_CREATED_MESSAGE, mockRoute.getId()
        );

        Assertions.assertEquals(expected, result);
        verify(mockService, times(1)).createRoute(VALID_LOCATIONS, VALID_DATE);
    }
}
