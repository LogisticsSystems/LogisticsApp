package com.company.logistics.services.routing.management;

import com.company.logistics.core.contracts.LogisticsRepository;
import com.company.logistics.services.routing.scheduling.RouteScheduleService;
import com.company.logistics.services.speeds.SpeedModelService;
import com.company.logistics.enums.City;
import com.company.logistics.models.contracts.Route;

import java.time.LocalDateTime;
import java.util.List;

public class RouteCreationService {
    private final LogisticsRepository repo;
    private final SpeedModelService speedModelService;

    public RouteCreationService(LogisticsRepository repo, SpeedModelService speedModelService) {
        this.repo = repo;
        this.speedModelService = speedModelService;
    }

    public Route createRoute(List<City> stops, LocalDateTime departure) {
        // 1) raw CRUD
        Route route = repo.createRoute(stops, departure);

        // 2) business logic
        RouteScheduleService routeScheduleService = speedModelService.getRouteScheduler();
        route.setSchedule(routeScheduleService.computeSchedule(stops, departure));

        return route;
    }
}
