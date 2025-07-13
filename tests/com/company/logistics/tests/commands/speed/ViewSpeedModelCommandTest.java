package com.company.logistics.tests.commands.speed;

import com.company.logistics.commands.CommandsConstants;
import com.company.logistics.commands.speed.ViewSpeedModelCommand;
import com.company.logistics.enums.UserRole;
import com.company.logistics.exceptions.InvalidUserInputException;
import com.company.logistics.models.contracts.User;
import com.company.logistics.repositories.contracts.UserRepository;
import com.company.logistics.services.speeds.SpeedModelService;
import com.company.logistics.services.speeds.implementation.SeasonalSpeedModel;
import com.company.logistics.utils.PrintConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class ViewSpeedModelCommandTest {
    private static List<String> parameters = new ArrayList<>();

    private SpeedModelService mockService;
    private UserRepository mockUserRepository;
    private User mockUser;

    private ViewSpeedModelCommand command;

    @BeforeEach
    public void setUp() {
        mockService = mock(SpeedModelService.class);
        mockUserRepository = mock(UserRepository.class);
        mockUser = mock(User.class);

        when(mockUserRepository.getLoggedInUser()).thenReturn(mockUser);
        when(mockUser.getRole()).thenReturn(UserRole.MANAGER);

        command = new ViewSpeedModelCommand(mockService, mockUserRepository);
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

    // Test that command works
    @Test
    public void execute_Should_AssignPackageToRoute_When_UserIsEmployeeAndParametersAreValid() {
        // Arrange

        when(mockService.getSpeedModel()).thenReturn(new SeasonalSpeedModel());

        // Act
        String result = command.execute(parameters);

        // Assert
        String expected = PrintConstants.LINE_BREAK + "\n" + String.format(PrintConstants.CURRENT_SPEED_MODEL, "Seasonal");

        Assertions.assertEquals(expected, result);
        verify(mockService, times(1)).getSpeedModel();
    }
}
