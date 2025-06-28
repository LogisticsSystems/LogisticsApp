package com.company.logistics.core.services.routing;

import com.company.logistics.core.context.EngineContext;
import com.company.logistics.enums.City;
import com.company.logistics.models.contracts.Route;

import java.time.LocalDateTime;
import java.util.List;

public class RouteCreationService {
    private final EngineContext engineContext;

    public RouteCreationService(EngineContext engineContext) {
        this.engineContext = engineContext;
    }

    public Route createRoute(List<City> stops, LocalDateTime departure) {
        // 1) raw CRUD
        Route route = engineContext.getRepository().createRoute(stops, departure);
        // 2) business logic
        route.setSchedule(engineContext.getRouteScheduler().computeSchedule(stops, departure));
        return route;
    }
}
