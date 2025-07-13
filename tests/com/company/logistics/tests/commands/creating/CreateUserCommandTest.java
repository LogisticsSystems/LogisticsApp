package com.company.logistics.tests.commands.creating;

import com.company.logistics.commands.CommandsConstants;
import com.company.logistics.commands.creating.CreateUserCommand;
import com.company.logistics.enums.UserRole;
import com.company.logistics.exceptions.InvalidUserInputException;
import com.company.logistics.models.contracts.User;
import com.company.logistics.repositories.contracts.UserRepository;
import com.company.logistics.utils.ErrorMessages;
import com.company.logistics.utils.ParsingHelpers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class CreateUserCommandTest {
    public static final int EXPECTED_PARAMETER_COUNT = 4;

    private static final String VALID_USERNAME = "sil";
    private static final String VALID_NAME = "name";
    private static final String VALID_PASSWORD = "sil123";
    private static final UserRole VALID_ROLE = UserRole.EMPLOYEE;
    private static final String INVALID_ROLE = "dhasjfl";
    private static List<String> parameters = List.of(VALID_USERNAME, VALID_NAME, VALID_PASSWORD, VALID_ROLE.toString());

    private UserRepository mockUserRepository;
    private User mockUser;

    private CreateUserCommand command;

    @BeforeEach
    public void setUp() {
        mockUserRepository = mock(UserRepository.class);
        mockUser = mock(User.class);

        when(mockUserRepository.getLoggedInUser()).thenReturn(mockUser);
        when(mockUser.getRole()).thenReturn(UserRole.MANAGER);

        command = new CreateUserCommand(mockUserRepository);
    }

    // Test parameter count
    @Test
    public void execute_Should_ThrowException_When_NotEnoughParameters() {
        List<String> parameters = List.of(VALID_USERNAME, VALID_NAME, VALID_PASSWORD);

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
        List<String> parameters = List.of(VALID_USERNAME, VALID_NAME, VALID_PASSWORD, VALID_PASSWORD, VALID_PASSWORD);

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

    //Test that parameters parse correctly
    @Test
    public void execute_Should_ThrowException_When_FourthParameterIsNotRole() {
        List<String> parameters = List.of(VALID_USERNAME, VALID_NAME, VALID_PASSWORD, INVALID_ROLE);

        Exception ex = Assertions.assertThrows(
                InvalidUserInputException.class,
                () -> command.execute(parameters)
        );

        String expectedMessage = ParsingHelpers.printEnum(UserRole.class);
        Assertions.assertEquals(expectedMessage, ex.getMessage());
    }

    // Test that command works
    @Test
    public void execute_Should_CreateUser_When_UserIsEmployeeAndParametersAreValid() {
        // Arrange

        when(mockUserRepository.createUser(VALID_USERNAME, VALID_NAME, VALID_PASSWORD, VALID_ROLE)).thenReturn(mockUser);
        when(mockUser.getUsername()).thenReturn(VALID_USERNAME);

        // Act
        String result = command.execute(parameters);

        // Assert
        String expected = String.format(
                CommandsConstants.USER_CREATED_MESSAGE, VALID_USERNAME
        );

        Assertions.assertEquals(expected, result);
        verify(mockUserRepository, times(1)).createUser(VALID_USERNAME, VALID_NAME, VALID_PASSWORD, VALID_ROLE);
    }

}
