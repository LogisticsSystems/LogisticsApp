package com.company.logistics.tests.commands.authenticating;

import com.company.logistics.commands.CommandsConstants;
import com.company.logistics.commands.authenticating.LogoutCommand;
import com.company.logistics.exceptions.InvalidUserInputException;
import com.company.logistics.models.contracts.User;
import com.company.logistics.repositories.contracts.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class LogoutCommandTest {
    private static final String username = "sil";

    private UserRepository mockUserRepository;
    private User mockUser;
    private static final List<String> parameters = new ArrayList<>();

    private LogoutCommand command;

    @BeforeEach
    public void setUp() {
        mockUserRepository = mock(UserRepository.class);
        mockUser = mock(User.class);
        when(mockUser.getUsername()).thenReturn(username);

        when(mockUserRepository.getLoggedInUser()).thenReturn(mockUser);

        command = new LogoutCommand(mockUserRepository);
    }

    // Test if there is a logged-in user
    @Test
    public void execute_Should_ThrowException_When_UserIsNotLoggedIn() {
        when(mockUserRepository.hasLoggedInUser()).thenReturn(false);

        Exception ex = Assertions.assertThrows(
                InvalidUserInputException.class,
                () -> command.execute(parameters)
        );

        String expectedMessage = CommandsConstants.NO_LOGGED_IN_USER;

        Assertions.assertEquals(expectedMessage, ex.getMessage());
    }

    // Test that command works
    @Test
    public void execute_Should_LogOutUser_When_UserIsLoggedIn() {
        when(mockUserRepository.hasLoggedInUser()).thenReturn(true);

        String result = command.execute(parameters);

        String expected = String.format(
                CommandsConstants.USER_LOGGED_OUT, username
        );

        Assertions.assertEquals(expected, result);
        verify(mockUserRepository, times(1)).logout();
    }
}
