package com.company.logistics.core.factory;

import com.company.logistics.core.contracts.CommandFactory;
import com.company.logistics.core.contracts.Engine;
import com.company.logistics.core.contracts.LogisticsRepository;
import com.company.logistics.core.implementation.CommandFactoryImpl;
import com.company.logistics.core.implementation.EngineImpl;
import com.company.logistics.core.implementation.LogisticsRepositoryImpl;
import com.company.logistics.core.services.engine.EngineHelper;
import com.company.logistics.core.services.routing.RouteScheduleService;
import com.company.logistics.core.services.speeds.SpeedService;
import com.company.logistics.core.services.speeds.implementation.SinusoidalSpeedService;
import com.company.logistics.infrastructure.DistanceMap;
import com.company.logistics.infrastructure.SpeedMap;
import com.company.logistics.infrastructure.loading.distances.implementation.DefaultDistanceLoader;
import com.company.logistics.infrastructure.loading.speeds.implementation.DefaultSpeedLoader;
import com.company.logistics.infrastructure.loading.vehicles.implementation.DefaultVehicleLoader;

import java.time.LocalDate;
import java.time.Month;

public final class EngineFactory {
    private EngineFactory() { }

    public static Engine create() {
        // initialize shared infrastructure
        initMaps();

        LogisticsRepository repo = createRepository();
        EngineHelper helper = createEngineHelper(repo);

        return new EngineImpl(helper);
    }

    private static void initMaps() {
        DistanceMap.initialize(new DefaultDistanceLoader());
        SpeedMap.initialize(new DefaultSpeedLoader());
    }

    private static LogisticsRepository createRepository() {
        SpeedService speedService = new SinusoidalSpeedService(10.0, LocalDate.of(LocalDate.now().getYear(), Month.JANUARY, 26));
        RouteScheduleService routeScheduleService = new RouteScheduleService(speedService);
        return new LogisticsRepositoryImpl(new DefaultVehicleLoader(), routeScheduleService);
    }

    private static EngineHelper createEngineHelper(LogisticsRepository repo) {
        CommandFactory commandFactory = new CommandFactoryImpl();
        return new EngineHelper(commandFactory, repo);
    }
}
