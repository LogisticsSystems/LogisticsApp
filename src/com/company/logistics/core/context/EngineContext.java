package com.company.logistics.core.context;

import com.company.logistics.core.contracts.LogisticsRepository;
import com.company.logistics.core.services.assignment.AssignmentService;
import com.company.logistics.core.services.delivery.PackageDeliveryService;
import com.company.logistics.core.services.routing.RouteCreationService;
import com.company.logistics.core.services.routing.RouteScheduleService;
import com.company.logistics.core.services.speeds.SpeedService;
import com.company.logistics.enums.City;
import com.company.logistics.models.contracts.DeliveryPackage;
import com.company.logistics.models.contracts.Route;

import java.time.LocalDateTime;
import java.util.List;

public class EngineContext {
    private final LogisticsRepository      repository;
    private SpeedService                   speedService;
    private RouteScheduleService           routeScheduler;
    private final PackageDeliveryService   deliveryService;
    private final AssignmentService        assignmentService;
    private RouteCreationService           routeCreationService;

    public EngineContext(
            LogisticsRepository      repo,
            SpeedService             speedService,
            RouteScheduleService     routeScheduler,
            PackageDeliveryService   deliveryService,
            AssignmentService        assignmentService
    ) {
        this.repository           = repo;
        this.speedService         = speedService;
        this.routeScheduler       = routeScheduler;
        this.deliveryService      = deliveryService;
        this.assignmentService    = assignmentService;
    }

    // core getters
    public LogisticsRepository      getRepository()           { return repository; }
    public SpeedService             getSpeedService()         { return speedService; }
    public RouteScheduleService     getRouteScheduler()       { return routeScheduler; }
    public PackageDeliveryService   getDeliveryService()      { return deliveryService; }
    public AssignmentService        getAssignmentService()    { return assignmentService; }
    public RouteCreationService     getRouteCreationService() { return routeCreationService; }
    public void setRouteCreationService(RouteCreationService svc) {
        this.routeCreationService = svc;
    }

    public void changeSpeedModel(SpeedService newSpeedService) {
        this.speedService   = newSpeedService;
        this.routeScheduler = new RouteScheduleService(newSpeedService);

        for (Route route : repository.getRoutes()) {
            List<City> stops    = route.getLocations();
            LocalDateTime depart   = route.getDepartureTime();
            List<LocalDateTime> newSchedule = routeScheduler.computeSchedule(stops, depart);
            route.setSchedule(newSchedule);

            for (DeliveryPackage pkg : route.getAssignedPackages()) {
                LocalDateTime eta = routeScheduler.getEtaForCity(
                        pkg.getEndLocation(),
                        stops,
                        newSchedule
                );
                pkg.setExpectedArrival(eta);
            }
        }
    }
}
