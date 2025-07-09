package com.company.logistics.services.routing.management;

import com.company.logistics.repositories.contracts.RouteRepository;
import com.company.logistics.services.routing.scheduling.RouteScheduleService;
import com.company.logistics.services.speeds.SpeedModelService;
import com.company.logistics.enums.City;
import com.company.logistics.models.contracts.Route;

import java.time.LocalDateTime;
import java.util.List;

public class RouteCreationService {
    private final RouteRepository routeRepository;
    private final SpeedModelService speedModelService;

    public RouteCreationService(RouteRepository routeRepository, SpeedModelService speedModelService) {
        this.routeRepository   = routeRepository;
        this.speedModelService = speedModelService;
    }

    public Route createRoute(List<City> stops, LocalDateTime departure) {
        Route route = routeRepository.createRoute(stops, departure);

        RouteScheduleService routeScheduleService = speedModelService.getRouteScheduler();
        route.setSchedule(routeScheduleService.computeSchedule(stops, departure));

        return route;
    }
}
