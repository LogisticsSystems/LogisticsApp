package com.company.logistics.core.context;

import com.company.logistics.core.contracts.LogisticsRepository;
import com.company.logistics.core.services.delivery.PackageDeliveryService;
import com.company.logistics.core.services.routing.RouteScheduleService;
import com.company.logistics.core.services.speeds.SpeedService;

public class EngineContext {
    private final LogisticsRepository repository;
    private SpeedService speedService;
    private RouteScheduleService routeScheduler;
    private final PackageDeliveryService deliveryService;

    public EngineContext(
            LogisticsRepository repo,
            SpeedService initialSpeed,
            RouteScheduleService initialRouteScheduler,
            PackageDeliveryService deliveryService
    ) {
        this.repository   = repo;
        this.speedService = initialSpeed;
        this.routeScheduler = initialRouteScheduler;
        this.deliveryService = deliveryService;
    }

    public LogisticsRepository getRepository() {
        return repository;
    }

    public SpeedService getSpeedService() {
        return speedService;
    }

    public RouteScheduleService getRouteScheduler() {
        return routeScheduler;
    }

    public PackageDeliveryService getDeliveryService() { return deliveryService; }

    public void setSpeedService(SpeedService speedService) {
        this.speedService = speedService;
    }

    public void setRouteScheduler(RouteScheduleService newRouteScheduler) {
        this.routeScheduler = newRouteScheduler;
    }
}
