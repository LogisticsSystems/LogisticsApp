package com.company.logistics.core.context;

import com.company.logistics.core.contracts.LogisticsRepository;
import com.company.logistics.services.assignment.AssignmentService;
import com.company.logistics.services.delivery.PackageDeliveryService;
import com.company.logistics.services.routing.computing.RouteRecalculatorService;
import com.company.logistics.services.routing.management.RouteCreationService;
import com.company.logistics.services.speeds.SpeedModelService;

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


    public LogisticsRepository getRepository() {
        return repository;
    }
    public SpeedModelService getSpeedModelService() {
        return speedModelService;
    }
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
