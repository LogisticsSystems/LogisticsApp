package com.company.logistics.core.services.routing.computing;

import com.company.logistics.core.contracts.LogisticsRepository;
import com.company.logistics.core.services.routing.scheduling.RouteScheduleService;
import com.company.logistics.core.services.speeds.SpeedModelService;
import com.company.logistics.models.contracts.DeliveryPackage;
import com.company.logistics.models.contracts.Route;

import java.time.LocalDateTime;
import java.util.List;

public class RouteRecalculatorService {
    private final LogisticsRepository repository;
    private final SpeedModelService speedModelService;

    public RouteRecalculatorService(
            LogisticsRepository repository,
            SpeedModelService speedModelService
    ) {
        this.repository = repository;
        this.speedModelService = speedModelService;
    }

    public void recomputeAll() {
        RouteScheduleService scheduler = speedModelService.getRouteScheduler();

        for (Route route : repository.getRoutes()) {
            // compute the new schedule for every route and set it
            List<LocalDateTime> newSchedule = scheduler.computeSchedule(route.getLocations(), route.getDepartureTime());
            route.setSchedule(newSchedule);

            // for every assigned package on every route, compute the new eta and set it
            for (DeliveryPackage pkg : route.getAssignedPackages()) {
                pkg.setExpectedArrival(scheduler.getEtaForCity(pkg.getEndLocation(), route.getLocations(), newSchedule));
            }
        }
    }
}
