package com.company.logistics.core.context;

import com.company.logistics.core.contracts.LogisticsRepository;
import com.company.logistics.core.services.assignment.AssignmentService;
import com.company.logistics.core.services.delivery.PackageDeliveryService;
import com.company.logistics.core.services.routing.computing.RouteRecalculatorService;
import com.company.logistics.core.services.routing.management.RouteCreationService;
import com.company.logistics.core.services.speeds.SpeedModelService;

public class EngineContext {
    private final LogisticsRepository    repository;
    private final SpeedModelService      speedModelService;
    private final PackageDeliveryService deliveryService;
    private final AssignmentService      assignmentService;
    private final RouteCreationService   routeCreationService;
    private final RouteRecalculatorService routeRecalculatorService;

    public EngineContext(
            LogisticsRepository    repository,
            SpeedModelService      speedModelService,
            PackageDeliveryService deliveryService,
            AssignmentService      assignmentService,
            RouteCreationService   routeCreationService,
            RouteRecalculatorService routeRecalculatorService
    ) {
        this.repository               = repository;
        this.speedModelService        = speedModelService;
        this.deliveryService          = deliveryService;
        this.assignmentService        = assignmentService;
        this.routeCreationService     = routeCreationService;
        this.routeRecalculatorService = routeRecalculatorService;
    }

    /** Raw data access */
    public LogisticsRepository getRepository() {
        return repository;
    }

    /** The one place to change speed models and rebuild the scheduler */
    public SpeedModelService getSpeedModelService() {
        return speedModelService;
    }

    /** Business services */
    public PackageDeliveryService getDeliveryService() {
        return deliveryService;
    }

    public AssignmentService getAssignmentService() {
        return assignmentService;
    }

    public RouteCreationService getRouteCreationService() {
        return routeCreationService;
    }

    public RouteRecalculatorService getRouteRecalculatorService() {
        return routeRecalculatorService;
    }
}
