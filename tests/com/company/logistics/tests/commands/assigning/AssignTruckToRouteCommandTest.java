package com.company.logistics.tests.commands.assigning;

import com.company.logistics.commands.CommandsConstants;
import com.company.logistics.commands.assigning.AssignTruckToRouteCommand;
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
import static org.mockito.Mockito.times;

public class AssignTruckToRouteCommandTest {
    private static final int EXPECTED_PARAMETER_COUNT = 2;

    private AssignmentService mockAssignmentService;
    private UserRepository mockUserRepository;
    private User mockUser;

    private AssignTruckToRouteCommand command;

    @BeforeEach
    public void setUp() {
        mockAssignmentService = mock(AssignmentService.class);
        mockUserRepository = mock(UserRepository.class);
        mockUser = mock(User.class);

        when(mockUserRepository.getLoggedInUser()).thenReturn(mockUser);
        when(mockUser.getRole()).thenReturn(UserRole.EMPLOYEE);

        command = new AssignTruckToRouteCommand(mockAssignmentService, mockUserRepository);
    }

    // Test parameter count
    @Test
    public void execute_Should_ThrowException_When_NotEnoughParameters() {
        List<String> parameters = List.of("5");

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
        List<String> parameters = List.of("5", "5", "5");

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

        String expectedMessage = String.format(
                CommandsConstants.COMMAND_UNAVAILABLE_FOR_USER,
                UserRole.MANAGER
        );

        Assertions.assertEquals(expectedMessage, ex.getMessage());
    }

    @Test
    public void execute_Should_ThrowException_When_UserIsDataAnalyst() {
        when(mockUser.getRole()).thenReturn(UserRole.DATA_ANALYST);

        List<String> parameters = List.of("5", "10");

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
    public void execute_Should_ThrowException_When_FirstParameterIsNotNumber() {
        List<String> parameters = List.of("asd", "5");

        Exception ex = Assertions.assertThrows(
                InvalidUserInputException.class,
                () -> command.execute(parameters)
        );

        String expectedMessage = String.format(ErrorMessages.INCORRECT_DATA_INPUT
                ,"Truck ID","number");
        Assertions.assertEquals(expectedMessage, ex.getMessage());
    }

    @Test
    public void execute_Should_ThrowException_When_SecondParameterIsNotNumber() {
        List<String> parameters = List.of("3", "asd");

        Exception ex = Assertions.assertThrows(
                InvalidUserInputException.class,
                () -> command.execute(parameters)
        );

        String expectedMessage = String.format(ErrorMessages.INCORRECT_DATA_INPUT
                ,"Route ID","number");
        Assertions.assertEquals(expectedMessage, ex.getMessage());
    }

    // Test that command works
    @Test
    public void execute_Should_AssignTruckToRoute_When_UserIsEmployeeAndParametersAreValid() {
        // Arrange
        int truckId = 6;
        int routeId = 11;
        PackageStatus expectedStatus = PackageStatus.PENDING;
        List<String> parameters = List.of(String.valueOf(truckId), String.valueOf(routeId));

        PackageSnapshot snapshot = new PackageSnapshot(
                truckId,
                expectedStatus,
                LocalDateTime.now(),
                ""
        );

        when(mockAssignmentService.assignPackageToRoute(truckId, routeId)).thenReturn(snapshot);

        // Act
        String result = command.execute(parameters);

        // Assert
        String expected = String.format(
                CommandsConstants.ASSIGNED_TRUCK_ROUTE,
                "Truck", truckId, routeId
        );

        Assertions.assertEquals(expected, result);
        verify(mockAssignmentService, times(1)).assignTruckToRoute(truckId, routeId);
    }
}
