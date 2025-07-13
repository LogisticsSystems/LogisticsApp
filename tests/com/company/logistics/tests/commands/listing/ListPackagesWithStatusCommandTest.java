package com.company.logistics.tests.commands.listing;

import com.company.logistics.commands.CommandsConstants;
import com.company.logistics.commands.listing.ListPackagesWithStatusCommand;
import com.company.logistics.enums.PackageStatus;
import com.company.logistics.enums.UserRole;
import com.company.logistics.exceptions.InvalidUserInputException;
import com.company.logistics.models.contracts.DeliveryPackage;
import com.company.logistics.models.contracts.User;
import com.company.logistics.repositories.contracts.PackageRepository;
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

public class ListPackagesWithStatusCommandTest {
    private static final int EXPECTED_PARAMETER_COUNT = 1;

    private static final List<String> parameters = List.of("DELIVERED");

    private static DeliveryPackage mockPackageOne = mock(DeliveryPackage.class);
    private static DeliveryPackage mockPackageTwo = mock(DeliveryPackage.class);

    private static final List<DeliveryPackage> VALID_LIST = List.of(
            mockPackageOne, mockPackageTwo
    );
    private static final List<DeliveryPackage> EMPTY_LIST = new ArrayList<>();
    private static final List<DeliveryPackage> VALID_LIST_OF_DELIVERED = List.of(mockPackageTwo);

    private PackageRepository mockPackageRepository;
    private PackageRepository mockEmptyPackageRepository;
    private UserRepository mockUserRepository;
    private User mockUser;

    private ListPackagesWithStatusCommand command;
    private ListPackagesWithStatusCommand commandEmpty;

    @BeforeEach
    public void setUp() {
        mockPackageRepository = mock(PackageRepository.class);
        mockEmptyPackageRepository = mock(PackageRepository.class);
        mockUserRepository = mock(UserRepository.class);
        mockUser = mock(User.class);

        when(mockUserRepository.getLoggedInUser()).thenReturn(mockUser);
        when(mockUser.getRole()).thenReturn(UserRole.EMPLOYEE);

        when(mockPackageRepository.getPackages()).thenReturn(VALID_LIST);
        when(mockEmptyPackageRepository.getPackages()).thenReturn(EMPTY_LIST);

        when(mockPackageOne.print()).thenReturn("printed package 1");
        when(mockPackageOne.getStatus()).thenReturn(PackageStatus.PENDING);

        when(mockPackageTwo.print()).thenReturn("printed package 2");
        when(mockPackageTwo.getStatus()).thenReturn(PackageStatus.DELIVERED);

        command = new ListPackagesWithStatusCommand(mockPackageRepository, mockUserRepository);
        commandEmpty = new ListPackagesWithStatusCommand(mockEmptyPackageRepository, mockUserRepository);
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
    public void execute_Should_ThrowException_When_UserIsManager() {
        when(mockUser.getRole()).thenReturn(UserRole.MANAGER);

        List<String> parameters = List.of("5", "10");

        Exception ex = Assertions.assertThrows(
                InvalidUserInputException.class,
                () -> command.execute(parameters)
        );
    }

    //Test that parameters parse correctly
    @Test
    public void execute_Should_ThrowException_When_ParameterIsNotStatus() {
        List<String> parameters = List.of("asdsa");

        Exception ex = Assertions.assertThrows(
                InvalidUserInputException.class,
                () -> command.execute(parameters)
        );

        String expectedMessage = ParsingHelpers.printEnum(PackageStatus.class);
        Assertions.assertEquals(expectedMessage, ex.getMessage());
    }

    // Test that command works
    @Test
    public void execute_Should_ReturnCorrectString_When_NoPackagesInList() {
        // Act
        String result = commandEmpty.execute(parameters);

        // Assert
        String expected = String.format(
                CommandsConstants.NO_PACKAGES_WITH_STATUS, "Delivered"
        );

        Assertions.assertEquals(expected, result);
        verify(mockPackageRepository, times(1)).getPackages();
    }

    @Test
    public void execute_Should_ReturnCorrectString_When_NoPackagesInListWithRequestedRole() {
        List<String> parameters = List.of("In_Transit");

        // Act
        String result = command.execute(parameters);

        // Assert
        String expected = String.format(
                CommandsConstants.NO_PACKAGES_WITH_STATUS, "In transit"
        );

        Assertions.assertEquals(expected, result);
        verify(mockPackageRepository, times(1)).getPackages();
    }

    @Test
    public void execute_Should_ReturnCorrectString_When_ListingPackages() {
        String result = command.execute(parameters);

        String expected = PrintConstants.LINE_BREAK + "\n" + ListingHelpers.elementsToString(VALID_LIST_OF_DELIVERED);

        Assertions.assertEquals(expected, result);
        verify(mockPackageRepository, times(1)).getPackages();
    }

}
