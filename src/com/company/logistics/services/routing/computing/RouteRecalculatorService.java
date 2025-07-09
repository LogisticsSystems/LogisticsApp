package com.company.logistics.services.routing.computing;

import com.company.logistics.repositories.contracts.RouteRepository;
import com.company.logistics.services.routing.scheduling.RouteScheduleService;
import com.company.logistics.services.speeds.SpeedModelService;
import com.company.logistics.models.contracts.DeliveryPackage;
import com.company.logistics.models.contracts.Route;

import java.time.LocalDateTime;
import java.util.List;

public class RouteRecalculatorService {
    private final RouteRepository routeRepository;
    private final SpeedModelService speedModelService;

    public RouteRecalculatorService(
            RouteRepository routeRepository,
            SpeedModelService speedModelService
    ) {
        this.routeRepository   = routeRepository;
        this.speedModelService = speedModelService;
    }

    public void recomputeAll() {
        RouteScheduleService scheduler = speedModelService.getRouteScheduler();

        for (Route route : routeRepository.getRoutes()) {
            List<LocalDateTime> newSchedule = scheduler.computeSchedule(route.getLocations(), route.getDepartureTime());
            route.setSchedule(newSchedule);

            for (DeliveryPackage pkg : route.getAssignedPackages()) {
                pkg.setExpectedArrival(scheduler.getEtaForCity(pkg.getEndLocation(), route.getLocations(), newSchedule));
            }
        }
    }
}
