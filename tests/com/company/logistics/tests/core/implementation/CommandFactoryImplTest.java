package com.company.logistics.tests.core.implementation;

import com.company.logistics.commands.contracts.Command;
import com.company.logistics.commands.creating.CreatePackageCommand;
import com.company.logistics.commands.creating.CreateRouteCommand;
import com.company.logistics.core.context.EngineContext;
import com.company.logistics.core.implementation.CommandFactoryImpl;
import com.company.logistics.exceptions.InvalidUserInputException;
import com.company.logistics.repositories.contracts.*;
import com.company.logistics.services.assignment.AssignmentService;
import com.company.logistics.services.delivery.PackageDeliveryService;
import com.company.logistics.services.persistence.PersistenceService;
import com.company.logistics.services.routing.computing.RouteRecalculatorService;
import com.company.logistics.services.routing.management.RouteCreationService;
import com.company.logistics.services.speeds.SpeedModelService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CommandFactoryImplTest {

    private CommandFactoryImpl factory;

    @BeforeEach
    public void setup() {
        // Mock all dependencies
        PackageRepository packageRepo = mock(PackageRepository.class);
        RouteRepository routeRepo     = mock(RouteRepository.class);
        TruckRepository truckRepo     = mock(TruckRepository.class);
        UserRepository userRepo       = mock(UserRepository.class);
        AssignmentService assignmentService = mock(AssignmentService.class);
        PackageDeliveryService deliveryService = mock(PackageDeliveryService.class);
        RouteCreationService routeCreationService = mock(RouteCreationService.class);
        SpeedModelService speedModelService = mock(SpeedModelService.class);
        RouteRecalculatorService routeRecalculatorService = mock(RouteRecalculatorService.class);
        PersistenceService persistenceService = mock(PersistenceService.class);

        // Create context
        EngineContext context = new EngineContext(
                packageRepo, routeRepo, truckRepo, userRepo,
                speedModelService, deliveryService, assignmentService,
                routeCreationService, routeRecalculatorService, persistenceService
        );

        factory = new CommandFactoryImpl(context);
    }

    @Test
    public void createCommandFromCommandName_Should_ReturnCorrectCommand_WhenValidName() {
        // Act
        Command command = factory.createCommandFromCommandName("CreatePackage");

        // Assert
        assertNotNull(command);
        assertTrue(command instanceof CreatePackageCommand);
    }

    @Test
    public void createCommandFromCommandName_Should_ReturnCorrectCommand_WhenValidName2() {
        // Act
        Command command = factory.createCommandFromCommandName("CreateRoute");

        // Assert
        assertNotNull(command);
        assertTrue(command instanceof CreateRouteCommand);
    }

    @Test
    public void createCommandFromCommandName_Should_BeCaseInsensitive() {
        Command command = factory.createCommandFromCommandName("createpackage");
        assertTrue(command instanceof CreatePackageCommand);
    }

    @Test
    public void createCommandFromCommandName_Should_Throw_WhenInvalidName() {
        assertThrows(InvalidUserInputException.class,
                () -> factory.createCommandFromCommandName("NotACommand"));
    }

    @Test
    public void createCommandFromCommandName_Should_Throw_WhenNull() {
        assertThrows(InvalidUserInputException.class,
                () -> factory.createCommandFromCommandName(null));
    }
}
