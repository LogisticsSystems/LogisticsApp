package com.company.logistics.tests.commands.listing;

import com.company.logistics.commands.CommandsConstants;
import com.company.logistics.commands.listing.ListUserCommand;
import com.company.logistics.enums.UserRole;
import com.company.logistics.exceptions.InvalidUserInputException;
import com.company.logistics.models.contracts.User;
import com.company.logistics.models.users.UserImpl;
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
import static org.mockito.Mockito.verify;

public class ListUserCommandTest {
    private static final List<User> VALID_LIST = List.of(
            new UserImpl("sil", "silvia", "sil123", UserRole.EMPLOYEE),
            new UserImpl("sil2", "silvia", "sil123", UserRole.MANAGER)
    );
    private static final List<User> EMPTY_LIST = new ArrayList<>();

    private UserRepository mockEmptyUserRepository;
    private UserRepository mockUserRepository;
    private User mockUser;

    private ListUserCommand command;
    private ListUserCommand commandEmpty;

    @BeforeEach
    public void setUp() {
        mockEmptyUserRepository = mock(UserRepository.class);
        mockUserRepository = mock(UserRepository.class);
        mockUser = mock(User.class);

        when(mockUserRepository.getLoggedInUser()).thenReturn(mockUser);
        when(mockEmptyUserRepository.getLoggedInUser()).thenReturn(mockUser);
        when(mockUser.getRole()).thenReturn(UserRole.MANAGER);

        when(mockUserRepository.getUsers()).thenReturn(VALID_LIST);
        when(mockEmptyUserRepository.getUsers()).thenReturn(EMPTY_LIST);

        command = new ListUserCommand(mockUserRepository);
        commandEmpty = new ListUserCommand(mockEmptyUserRepository);
    }

    // Test role
    @Test
    public void execute_Should_ThrowException_When_UserIsEmployee() {
        when(mockUser.getRole()).thenReturn(UserRole.EMPLOYEE);

        List<String> parameters = List.of("5", "10");

        Exception ex = Assertions.assertThrows(
                InvalidUserInputException.class,
                () -> command.execute(parameters)
        );
    }

    // Test that command works
    @Test
    public void execute_Should_ReturnCorrectString_When_NoUsersInList() {
        List<String> parameters = new ArrayList<>();

        String result = commandEmpty.execute(parameters);

        String expected = String.format(
                CommandsConstants.NONE_FOUND_MESSAGE, "users"
        );

        Assertions.assertEquals(expected, result);
        verify(mockEmptyUserRepository, times(1)).getUsers();
    }

    @Test
    public void execute_Should_ReturnCorrectString_When_ListingUsers() {
        List<String> parameters = new ArrayList<>();

        String result = command.execute(parameters);

        String expected = PrintConstants.LINE_BREAK + "\n" + ListingHelpers.elementsToString(VALID_LIST);

        Assertions.assertEquals(expected, result);
        verify(mockUserRepository, times(1)).getUsers();
    }
}
