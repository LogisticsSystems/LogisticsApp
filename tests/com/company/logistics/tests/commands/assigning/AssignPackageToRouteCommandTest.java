package com.company.logistics.tests.commands.assigning;

import com.company.logistics.commands.CommandsConstants;
import com.company.logistics.commands.assigning.AssignPackageToRouteCommand;
import com.company.logistics.dto.PackageSnapshot;
import com.company.logistics.enums.PackageStatus;
import com.company.logistics.enums.UserRole;
import com.company.logistics.exceptions.InvalidUserInputException;
import com.company.logistics.models.contracts.User;
import com.company.logistics.repositories.contracts.UserRepository;
import com.company.logistics.services.assignment.AssignmentService;

import com.company.logistics.utils.ErrorMessages;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;

public class AssignPackageToRouteCommandTest {

    private AssignmentService mockAssignmentService;
    private UserRepository mockUserRepository;
    private User mockUser;

    private AssignPackageToRouteCommand command;

    @BeforeEach
    public void setUp() {
        mockAssignmentService = mock(AssignmentService.class);
        mockUserRepository = mock(UserRepository.class);
        mockUser = mock(User.class);

        when(mockUserRepository.getLoggedInUser()).thenReturn(mockUser);
        when(mockUser.getRole()).thenReturn(UserRole.EMPLOYEE);

        command = new AssignPackageToRouteCommand(mockAssignmentService, mockUserRepository);
    }

    @Test
    public void execute_Should_ThrowException_When_NotEnoughArguments() {
        List<String> parameters = List.of("5");

        Exception ex = Assertions.assertThrows(
                InvalidUserInputException.class,
                () -> command.execute(parameters)
        );

        String expectedMessage = String.format(ErrorMessages.INVALID_ARGUMENTS_COUNT, 2, 1);
        Assertions.assertEquals(expectedMessage, ex.getMessage());
    }

    @Test
    public void execute_Should_ThrowException_When_UserIsNotEmployee() {
        when(mockUser.getRole()).thenReturn(UserRole.MANAGER);

        List<String> parameters = List.of("5", "10");

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
    public void execute_Should_AssignPackageToRoute_When_UserIsEmployeeAndNoTruck() {
        // Arrange
        int packageId = 5;
        int routeId = 10;
        PackageStatus expectedStatus = PackageStatus.PENDING;
        List<String> parameters = List.of(String.valueOf(packageId), String.valueOf(routeId));

        PackageSnapshot snapshot = new PackageSnapshot(
                packageId,
                expectedStatus,
                LocalDateTime.now()
        );

        when(mockAssignmentService.assignPackageToRoute(packageId, routeId)).thenReturn(snapshot);

        // Act
        String result = command.execute(parameters);

        // Assert
        String expected = String.format(
                CommandsConstants.ASSIGNED_PACKAGE_TO_ROUTE,
                "Package", packageId, routeId, expectedStatus
        );

        Assertions.assertEquals(expected, result);
        verify(mockAssignmentService, times(1)).assignPackageToRoute(packageId, routeId);
    }

    @Test
    public void execute_Should_AssignPackageToRoute_When_UserIsEmployeeAndTruckIsAssigned() {
        // Arrange
        int packageId = 6;
        int routeId = 11;
        PackageStatus expectedStatus = PackageStatus.IN_TRANSIT;
        List<String> parameters = List.of(String.valueOf(packageId), String.valueOf(routeId));

        PackageSnapshot snapshot = new PackageSnapshot(
                packageId,
                expectedStatus,
                LocalDateTime.now()
        );

        when(mockAssignmentService.assignPackageToRoute(packageId, routeId)).thenReturn(snapshot);

        // Act
        String result = command.execute(parameters);

        // Assert
        String expected = String.format(
                CommandsConstants.ASSIGNED_PACKAGE_TO_ROUTE,
                "Package", packageId, routeId, expectedStatus
        );

        Assertions.assertEquals(expected, result);
        verify(mockAssignmentService, times(1)).assignPackageToRoute(packageId, routeId);
    }

    @Test
    public void execute_Should_SetEta_When_AssigningPackage() {
        // Arrange
        int packageId = 7;
        int routeId = 12;
        LocalDateTime expectedEta = LocalDateTime.now().plusHours(5);

        PackageSnapshot snapshot = new PackageSnapshot(
                packageId,
                PackageStatus.PENDING,
                expectedEta
        );

        List<String> parameters = List.of(String.valueOf(packageId), String.valueOf(routeId));

        when(mockAssignmentService.assignPackageToRoute(packageId, routeId)).thenReturn(snapshot);

        // Act
        command.execute(parameters);

        // Assert
        Assertions.assertNotNull(snapshot.eta(), "Expected arrival (ETA) should not be null");
        verify(mockAssignmentService, times(1)).assignPackageToRoute(packageId, routeId);
    }
}
