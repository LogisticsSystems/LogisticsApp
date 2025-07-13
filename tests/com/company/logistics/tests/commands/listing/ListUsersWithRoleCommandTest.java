package com.company.logistics.tests.commands.listing;

import com.company.logistics.commands.CommandsConstants;
import com.company.logistics.commands.listing.ListUsersWithRoleCommand;
import com.company.logistics.enums.UserRole;
import com.company.logistics.exceptions.InvalidUserInputException;
import com.company.logistics.models.contracts.User;
import com.company.logistics.repositories.contracts.UserRepository;
import com.company.logistics.utils.ErrorMessages;
import com.company.logistics.utils.ListingHelpers;
import com.company.logistics.utils.ParsingHelpers;
import com.company.logistics.utils.PrintConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ListUsersWithRoleCommandTest {
    private static final int EXPECTED_PARAMETER_COUNT = 1;

    private static final List<String> parameters = List.of("MANAGER");

    private static User mockUserEmployee = mock(User.class);
    private static User mockUserManager = mock(User.class);

    private static final List<User> VALID_LIST = List.of(
            mockUserEmployee, mockUserManager
    );

    private static final List<User> EMPTY_LIST = new ArrayList<>();
    private static final List<User> VALID_LIST_OF_MANAGER = List.of(mockUserManager);

    private UserRepository mockEmptyUserRepository;
    private UserRepository mockUserRepository;
    private User mockUser;

    private ListUsersWithRoleCommand command;
    private ListUsersWithRoleCommand commandEmpty;

    @BeforeEach
    public void setUp() {
        mockUserRepository = mock(UserRepository.class);
        mockEmptyUserRepository = mock(UserRepository.class);
        mockUser = mock(User.class);

        when(mockUserRepository.getLoggedInUser()).thenReturn(mockUser);
        when(mockEmptyUserRepository.getLoggedInUser()).thenReturn(mockUser);
        when(mockUser.getRole()).thenReturn(UserRole.MANAGER);

        when(mockUserRepository.getUsers()).thenReturn(VALID_LIST);
        when(mockEmptyUserRepository.getUsers()).thenReturn(EMPTY_LIST);

        when(mockUserEmployee.print()).thenReturn("printed employee");
        when(mockUserEmployee.getRole()).thenReturn(UserRole.EMPLOYEE);

        when(mockUserManager.print()).thenReturn("printed manager");
        when(mockUserManager.getRole()).thenReturn(UserRole.MANAGER);

        command = new ListUsersWithRoleCommand(mockUserRepository);
        commandEmpty = new ListUsersWithRoleCommand(mockEmptyUserRepository);
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
        List<String> parameters = List.of("delivered", "delivered");

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

        List<String> parameters = List.of("5", "10");

        Exception ex = Assertions.assertThrows(
                InvalidUserInputException.class,
                () -> command.execute(parameters)
        );
    }

    //Test that parameters parse correctly
    @Test
    public void execute_Should_ThrowException_When_ParameterIsNotRole() {
        List<String> parameters = List.of("asdsa");

        Exception ex = Assertions.assertThrows(
                InvalidUserInputException.class,
                () -> command.execute(parameters)
        );

        String expectedMessage = ParsingHelpers.printEnum(UserRole.class);
        Assertions.assertEquals(expectedMessage, ex.getMessage());
    }

    // Test that command works
    @Test
    public void execute_Should_ReturnCorrectString_When_NoUsersInList() {
        String result = commandEmpty.execute(parameters);

        // Assert
        String expected = String.format(
                CommandsConstants.NO_USERS_WITH_ROLE, "MANAGER"
        );

        Assertions.assertEquals(expected, result);
        verify(mockEmptyUserRepository, times(1)).getUsers();
    }

    @Test
    public void execute_Should_ReturnCorrectString_When_NoUsersInListWithRequestedRole() {
        List<String> parameters = List.of("DATA_ANALYST");

        // Act
        String result = command.execute(parameters);

        // Assert
        String expected = String.format(
                CommandsConstants.NO_USERS_WITH_ROLE, "DATA_ANALYST"
        );

        Assertions.assertEquals(expected, result);
        verify(mockUserRepository, times(1)).getUsers();
    }

    @Test
    public void execute_Should_ReturnCorrectString_When_ListingUsers() {
        String result = command.execute(parameters);

        String expected = PrintConstants.LINE_BREAK + "\n" + ListingHelpers.elementsToString(VALID_LIST_OF_MANAGER);

        Assertions.assertEquals(expected, result);
        verify(mockUserRepository, times(1)).getUsers();
    }


}
