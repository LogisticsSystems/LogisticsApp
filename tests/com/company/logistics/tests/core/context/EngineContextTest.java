package com.company.logistics.tests.core.context;

import com.company.logistics.core.context.EngineContext;
import com.company.logistics.repositories.contracts.*;
import com.company.logistics.services.assignment.AssignmentService;
import com.company.logistics.services.delivery.PackageDeliveryService;
import com.company.logistics.services.persistence.PersistenceService;
import com.company.logistics.services.routing.computing.RouteRecalculatorService;
import com.company.logistics.services.routing.management.RouteCreationService;
import com.company.logistics.services.speeds.SpeedModelService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class EngineContextTest {

    @Test
    public void engineContext_Should_ReturnCorrectInstances() {
        // Arrange
        PackageRepository packageRepo = mock(PackageRepository.class);
        RouteRepository routeRepo = mock(RouteRepository.class);
        TruckRepository truckRepo = mock(TruckRepository.class);
        UserRepository userRepo = mock(UserRepository.class);
        SpeedModelService speedService = mock(SpeedModelService.class);
        PackageDeliveryService deliveryService = mock(PackageDeliveryService.class);
        AssignmentService assignmentService = mock(AssignmentService.class);
        RouteCreationService routeCreationService = mock(RouteCreationService.class);
        RouteRecalculatorService routeRecalculatorService = mock(RouteRecalculatorService.class);
        PersistenceService persistenceService = mock(PersistenceService.class);

        EngineContext context = new EngineContext(
                packageRepo,
                routeRepo,
                truckRepo,
                userRepo,
                speedService,
                deliveryService,
                assignmentService,
                routeCreationService,
                routeRecalculatorService,
                persistenceService
        );

        // Assert
        assertSame(packageRepo, context.getPackageRepository());
        assertSame(routeRepo, context.getRouteRepository());
        assertSame(truckRepo, context.getTruckRepository());
        assertSame(userRepo, context.getUserRepository());
        assertSame(speedService, context.getSpeedModelService());
        assertSame(deliveryService, context.getDeliveryService());
        assertSame(assignmentService, context.getAssignmentService());
        assertSame(routeCreationService, context.getRouteCreationService());
        assertSame(routeRecalculatorService, context.getRouteRecalculatorService());
        assertSame(persistenceService, context.getPersistenceService());
    }
}
