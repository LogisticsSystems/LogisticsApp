package com.company.logistics.core.context;

import com.company.logistics.repositories.contracts.PackageRepository;
import com.company.logistics.repositories.contracts.RouteRepository;
import com.company.logistics.repositories.contracts.TruckRepository;
import com.company.logistics.services.assignment.AssignmentService;
import com.company.logistics.services.delivery.PackageDeliveryService;
import com.company.logistics.services.persistence.PersistenceService;
import com.company.logistics.services.routing.computing.RouteRecalculatorService;
import com.company.logistics.services.routing.management.RouteCreationService;
import com.company.logistics.services.speeds.SpeedModelService;

public class EngineContext {
    private final PackageRepository        packageRepository;
    private final RouteRepository          routeRepository;
    private final TruckRepository          truckRepository;
    private final SpeedModelService        speedModelService;
    private final PackageDeliveryService   deliveryService;
    private final AssignmentService        assignmentService;
    private final RouteCreationService     routeCreationService;
    private final RouteRecalculatorService routeRecalculatorService;
    private final PersistenceService       persistenceService;

    public EngineContext(
            PackageRepository        packageRepository,
            RouteRepository          routeRepository,
            TruckRepository          truckRepository,
            SpeedModelService        speedModelService,
            PackageDeliveryService   deliveryService,
            AssignmentService        assignmentService,
            RouteCreationService     routeCreationService,
            RouteRecalculatorService routeRecalculatorService,
            PersistenceService       persistenceService
    ) {
        this.packageRepository        = packageRepository;
        this.routeRepository          = routeRepository;
        this.truckRepository          = truckRepository;
        this.speedModelService        = speedModelService;
        this.deliveryService          = deliveryService;
        this.assignmentService        = assignmentService;
        this.routeCreationService     = routeCreationService;
        this.routeRecalculatorService = routeRecalculatorService;
        this.persistenceService       = persistenceService;
    }


    public PackageRepository getPackageRepository() { return packageRepository; }
    public RouteRepository getRouteRepository()     { return routeRepository; }
    public TruckRepository getTruckRepository()     { return truckRepository; }

    public SpeedModelService getSpeedModelService()               { return speedModelService; }
    public RouteCreationService getRouteCreationService()         { return routeCreationService; }
    public RouteRecalculatorService getRouteRecalculatorService() { return routeRecalculatorService; }
    public PackageDeliveryService getDeliveryService()            { return deliveryService; }
    public AssignmentService getAssignmentService()               { return assignmentService; }
    public PersistenceService getPersistenceService()             { return persistenceService; }
}
