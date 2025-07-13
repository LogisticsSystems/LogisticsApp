package com.company.logistics.tests.commands.speed;

import com.company.logistics.commands.CommandsConstants;
import com.company.logistics.commands.speed.ChangeSpeedModelCommand;
import com.company.logistics.commands.speed.ViewSpeedModelCommand;
import com.company.logistics.enums.City;
import com.company.logistics.enums.SpeedModelType;
import com.company.logistics.enums.UserRole;
import com.company.logistics.exceptions.InvalidUserInputException;
import com.company.logistics.models.contracts.User;
import com.company.logistics.repositories.contracts.UserRepository;
import com.company.logistics.services.routing.computing.RouteRecalculatorService;
import com.company.logistics.services.speeds.SpeedModelService;
import com.company.logistics.services.speeds.contract.SpeedModel;
import com.company.logistics.services.speeds.implementation.SeasonalSpeedModel;
import com.company.logistics.utils.ParsingHelpers;
import com.company.logistics.utils.PrintConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class ChangeSpeedModelCommandTest {
    private static List<String> parameters = List.of("SEASONAL");

    private SpeedModelService mockService;
    private UserRepository mockUserRepository;
    private RouteRecalculatorService mockRouteService;
    private User mockUser;

    private ChangeSpeedModelCommand command;

    @BeforeEach
    public void setUp() {
        mockService = mock(SpeedModelService.class);
        mockUserRepository = mock(UserRepository.class);
        mockRouteService = mock(RouteRecalculatorService.class);
        mockUser = mock(User.class);

        when(mockUserRepository.getLoggedInUser()).thenReturn(mockUser);
        when(mockUser.getRole()).thenReturn(UserRole.MANAGER);

        command = new ChangeSpeedModelCommand(mockService, mockRouteService, mockUserRepository);
    }

    // Test role
    @Test
    public void execute_Should_ThrowException_When_UserIsEmployee() {
        when(mockUser.getRole()).thenReturn(UserRole.EMPLOYEE);

        Exception ex = Assertions.assertThrows(
                InvalidUserInputException.class,
                () -> command.execute(parameters)
        );

        String expectedMessage = String.format(
                CommandsConstants.COMMAND_UNAVAILABLE_FOR_USER,
                UserRole.EMPLOYEE
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
    public void execute_Should_ThrowException_When_ParameterIsNotModel() {
        List<String> parameters = List.of("dsfdsafs");

        Exception ex = Assertions.assertThrows(
                InvalidUserInputException.class,
                () -> command.execute(parameters)
        );

        String expectedMessage = ParsingHelpers.printEnum(SpeedModelType.class);
        Assertions.assertEquals(expectedMessage, ex.getMessage());
    }

    // Test that command works
    @Test
    public void execute_Should_AssignPackageToRoute_When_UserIsEmployeeAndParametersAreValid() {
        // Arrange

        when(mockService.getSpeedModel()).thenReturn(new SeasonalSpeedModel());

        // Act
        String result = command.execute(parameters);

        // Assert
        String expected = String.format(CommandsConstants.SPEED_MODEL_SWITCH, "SEASONAL");

        Assertions.assertEquals(expected, result);
        verify(mockService, times(1)).changeModel(SpeedModelType.SEASONAL);
    }
}
