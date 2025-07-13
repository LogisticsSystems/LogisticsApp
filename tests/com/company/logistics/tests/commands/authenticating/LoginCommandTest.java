package com.company.logistics.tests.commands.authenticating;

import com.company.logistics.commands.CommandsConstants;
import com.company.logistics.commands.authenticating.LoginCommand;
import com.company.logistics.exceptions.InvalidUserInputException;
import com.company.logistics.models.contracts.User;
import com.company.logistics.repositories.contracts.UserRepository;
import com.company.logistics.utils.ErrorMessages;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class LoginCommandTest {
    private static final int EXPECTED_PARAMETER_COUNT = 2;
    private static final String username = "sil";
    private static final String password = "sil123";
    private static final List<String> parameters = List.of(username, password);

    private UserRepository mockUserRepository;
    private User mockUser;

    private LoginCommand command;

    @BeforeEach
    public void setUp() {
        mockUserRepository = mock(UserRepository.class);
        mockUser = mock(User.class);

        when(mockUserRepository.getLoggedInUser()).thenReturn(mockUser);

        command = new LoginCommand(mockUserRepository);
    }

    // Test parameter count
    @Test
    public void execute_Should_ThrowException_When_NotEnoughParameters() {
        List<String> parameters = List.of("sil");

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
        List<String> parameters = List.of("sil", "sil", "sil");

        Exception ex = Assertions.assertThrows(
                InvalidUserInputException.class,
                () -> command.execute(parameters)
        );

        String expectedMessage = String.format(ErrorMessages.INVALID_ARGUMENTS_COUNT,
                EXPECTED_PARAMETER_COUNT,
                EXPECTED_PARAMETER_COUNT + 1);
        Assertions.assertEquals(expectedMessage, ex.getMessage());
    }

    // Test if there is a logged-in user
    @Test
    public void execute_Should_ThrowException_When_UserIsLoggedIn() {
        when(mockUserRepository.hasLoggedInUser()).thenReturn(true);

        Exception ex = Assertions.assertThrows(
                InvalidUserInputException.class,
                () -> command.execute(parameters)
        );

        String expectedMessage = String.format(
                CommandsConstants.USER_LOGGED_IN_ALREADY,
                mockUserRepository.getLoggedInUser().getUsername()
        );

        Assertions.assertEquals(expectedMessage, ex.getMessage());
    }

    // Test that command works
    @Test
    public void execute_Should_LogInUser_When_NoUserIsLoggedInAndParametersAreCorrect() {
        // Arrange
        when(mockUserRepository.findUserByUsername(username)).thenReturn(mockUser);
        when(mockUser.getUsername()).thenReturn(username);
        when(mockUser.checkPassword(password)).thenReturn(true);

        // Act
        String result = command.execute(parameters);

        // Assert
        String expected = String.format(
                CommandsConstants.USER_LOGGED_IN, username
        );

        Assertions.assertEquals(expected, result);
        verify(mockUserRepository, times(1)).login(username, password);
    }
}
