package com.company.logistics.tests.commands.querying;

import com.company.logistics.commands.CommandsConstants;
import com.company.logistics.commands.querying.ViewPackageWithIDCommand;
import com.company.logistics.enums.UserRole;
import com.company.logistics.exceptions.InvalidUserInputException;
import com.company.logistics.models.contracts.DeliveryPackage;
import com.company.logistics.models.contracts.User;
import com.company.logistics.repositories.contracts.PackageRepository;
import com.company.logistics.repositories.contracts.UserRepository;
import com.company.logistics.utils.ErrorMessages;
import com.company.logistics.utils.PrintConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class ViewPackageWithIDCommandTest {
    public static final int EXPECTED_PARAMETER_COUNT = 1;

    private static List<String> parameters = List.of("1");

    private PackageRepository mockPackageRepository;
    private UserRepository mockUserRepository;
    private User mockUser;
    private DeliveryPackage mockPackage;

    private ViewPackageWithIDCommand command;

    @BeforeEach
    public void setUp() {
        mockPackageRepository = mock(PackageRepository.class);
        mockUserRepository = mock(UserRepository.class);
        mockUser = mock(User.class);
        mockPackage = mock(DeliveryPackage.class);

        when(mockUserRepository.getLoggedInUser()).thenReturn(mockUser);
        when(mockUser.getRole()).thenReturn(UserRole.EMPLOYEE);

        when(mockPackageRepository.findPackageById(1)).thenReturn(mockPackage);
        when(mockPackage.print()).thenReturn("Print package with ID 1");

        command = new ViewPackageWithIDCommand(mockPackageRepository, mockUserRepository);
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
        List<String> parameters = List.of("1", "1");

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

    //Test that parameters parse correctly
    @Test
    public void execute_Should_ThrowException_When_FirstParameterIsNotNumber() {
        List<String> parameters = List.of("asd");

        Exception ex = Assertions.assertThrows(
                InvalidUserInputException.class,
                () -> command.execute(parameters)
        );

        String expectedMessage = String.format(ErrorMessages.INCORRECT_DATA_INPUT
                ,"Package ID", "number");
        Assertions.assertEquals(expectedMessage, ex.getMessage());
    }

    // Test that command works
    @Test
    public void execute_Should_ReturnPackage_When_UserIsEmployeeAndParametersAreValid() {
        // Act
        String result = command.execute(parameters);

        // Assert
        String expected = PrintConstants.LINE_BREAK + "\n" + mockPackage.print();

        Assertions.assertEquals(expected, result);
        verify(mockPackageRepository, times(1)).findPackageById(1);
    }


}
