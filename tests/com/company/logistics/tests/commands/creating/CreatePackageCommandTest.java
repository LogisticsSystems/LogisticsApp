package com.company.logistics.tests.commands.creating;

import com.company.logistics.commands.CommandsConstants;
import com.company.logistics.commands.creating.CreatePackageCommand;
import com.company.logistics.enums.City;
import com.company.logistics.enums.UserRole;
import com.company.logistics.exceptions.InvalidUserInputException;
import com.company.logistics.models.contracts.DeliveryPackage;
import com.company.logistics.models.contracts.User;
import com.company.logistics.repositories.contracts.PackageRepository;
import com.company.logistics.repositories.contracts.UserRepository;
import com.company.logistics.utils.ErrorMessages;
import com.company.logistics.utils.ParsingHelpers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class CreatePackageCommandTest {
    public static final int EXPECTED_PARAMETER_COUNT = 4;

    private static final String CONTACT = "package";
    private static final String WEIGHT = "123";
    private static final String CITY_ONE = "MEL";
    private static final String CITY_TWO = "ADL";
    private static final String INVALID_CITY = "dhasjfl";
    private static List<String> parameters = List.of(CONTACT, WEIGHT, CITY_ONE, CITY_TWO);

    private PackageRepository mockPackageRepository;
    private UserRepository mockUserRepository;
    private User mockUser;

    private CreatePackageCommand command;

    @BeforeEach
    public void setUp() {
        mockPackageRepository = mock(PackageRepository.class);
        mockUserRepository = mock(UserRepository.class);
        mockUser = mock(User.class);

        when(mockUserRepository.getLoggedInUser()).thenReturn(mockUser);
        when(mockUser.getRole()).thenReturn(UserRole.EMPLOYEE);

        command = new CreatePackageCommand(mockPackageRepository, mockUserRepository);
    }

    // Test parameter count
    @Test
    public void execute_Should_ThrowException_When_NotEnoughParameters() {
        List<String> parameters = List.of(CONTACT, WEIGHT, CITY_ONE);

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
        List<String> parameters = List.of(CONTACT, WEIGHT, CITY_ONE, CITY_TWO, CITY_TWO);

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
    public void execute_Should_ThrowException_When_SecondParameterIsNotNumber() {
        List<String> parameters = List.of(CONTACT, "asd", CITY_ONE, CITY_TWO);

        Exception ex = Assertions.assertThrows(
                InvalidUserInputException.class,
                () -> command.execute(parameters)
        );

        String expectedMessage = String.format(ErrorMessages.INCORRECT_DATA_INPUT
                ,"Weight", "fractional number");
        Assertions.assertEquals(expectedMessage, ex.getMessage());
    }

    @Test
    public void execute_Should_ThrowException_When_ThirdParameterIsNotCity() {
        List<String> parameters = List.of(CONTACT, WEIGHT, INVALID_CITY, CITY_TWO);

        Exception ex = Assertions.assertThrows(
                InvalidUserInputException.class,
                () -> command.execute(parameters)
        );

        String expectedMessage = ParsingHelpers.printEnum(City.class);
        Assertions.assertEquals(expectedMessage, ex.getMessage());
    }

    @Test
    public void execute_Should_ThrowException_When_FourthParameterIsNotCity() {
        List<String> parameters = List.of(CONTACT, WEIGHT, CITY_ONE, INVALID_CITY);

        Exception ex = Assertions.assertThrows(
                InvalidUserInputException.class,
                () -> command.execute(parameters)
        );

        String expectedMessage = ParsingHelpers.printEnum(City.class);
        Assertions.assertEquals(expectedMessage, ex.getMessage());
    }

    // Test that command works
    @Test
    public void execute_Should_CreatePackage_When_UserIsEmployeeAndParametersAreValid() {
        // Arrange
        DeliveryPackage mockPackage = mock(DeliveryPackage.class);
        when(mockPackage.getId()).thenReturn(1);

        when(mockPackageRepository.createPackage(CONTACT, 123, City.MEL, City.ADL)).thenReturn(mockPackage);

        // Act
        String result = command.execute(parameters);

        // Assert
        String expected = String.format(
                CommandsConstants.PACKAGE_CREATED_MESSAGE, mockPackage.getId()
        );

        Assertions.assertEquals(expected, result);
        verify(mockPackageRepository, times(1)).createPackage(CONTACT, 123, City.MEL, City.ADL);
    }
}
