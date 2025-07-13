package com.company.logistics.tests.commands.removing;

import com.company.logistics.commands.CommandsConstants;
import com.company.logistics.commands.removing.RemoveUserCommand;
import com.company.logistics.enums.UserRole;
import com.company.logistics.exceptions.InvalidUserInputException;
import com.company.logistics.models.contracts.User;
import com.company.logistics.repositories.contracts.UserRepository;
import com.company.logistics.utils.ErrorMessages;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class RemoveUserCommandTest {
    public static final int EXPECTED_PARAMETER_COUNT = 1;

    private static final String VALID_USERNAME = "sil";
    private static final String INVALID_USERNAME = "sil15";

    private static List<String> parameters = List.of(VALID_USERNAME);

    private UserRepository mockUserRepository;
    private User mockUser;
    private User mockUserTwo;

    private RemoveUserCommand command;

    @BeforeEach
    public void setUp() {
        mockUserRepository = mock(UserRepository.class);
        mockUser = mock(User.class);
        mockUserTwo = mock(User.class);

        when(mockUserRepository.getLoggedInUser()).thenReturn(mockUser);
        when(mockUser.getRole()).thenReturn(UserRole.MANAGER);
        when(mockUser.getUsername()).thenReturn(INVALID_USERNAME);

        command = new RemoveUserCommand(mockUserRepository);
    }

    // Test parameter count
    @Test
    public void execute_Should_ThrowException_When_NotEnoughParameters() {
        List<String> parameters = new ArrayList<>();

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
        List<String> parameters = List.of(VALID_USERNAME, VALID_USERNAME);

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
    public void execute_Should_RemoveUser_When_UserIsManagerAndParametersAreValid() {
        // Arrange

        when(mockUserRepository.removeUser(VALID_USERNAME)).thenReturn(mockUserTwo);
        when(mockUserTwo.getUsername()).thenReturn(VALID_USERNAME);

        // Act
        String result = command.execute(parameters);

        // Assert
        String expected = String.format(
                CommandsConstants.USER_REMOVED_MESSAGE, VALID_USERNAME
        );

        Assertions.assertEquals(expected, result);
        verify(mockUserRepository, times(1)).removeUser(VALID_USERNAME);
    }





}
