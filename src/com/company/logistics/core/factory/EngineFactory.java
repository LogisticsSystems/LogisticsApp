package com.company.logistics.core.factory;

import com.company.logistics.core.context.EngineContext;
import com.company.logistics.core.contracts.CommandFactory;
import com.company.logistics.core.contracts.Engine;
import com.company.logistics.core.contracts.LogisticsRepository;
import com.company.logistics.core.implementation.CommandFactoryImpl;
import com.company.logistics.core.implementation.EngineImpl;
import com.company.logistics.core.implementation.LogisticsRepositoryImpl;
import com.company.logistics.core.services.engine.CommandProcessor;
import com.company.logistics.core.services.routing.RouteScheduleService;
import com.company.logistics.core.services.speeds.SpeedService;
import com.company.logistics.core.services.speeds.implementation.ConstantSpeedService;
import com.company.logistics.infrastructure.DistanceMap;
import com.company.logistics.infrastructure.SpeedMap;
import com.company.logistics.infrastructure.loading.distances.implementation.DefaultDistanceLoader;
import com.company.logistics.infrastructure.loading.speeds.implementation.DefaultSpeedLoader;
import com.company.logistics.infrastructure.loading.vehicles.implementation.DefaultVehicleLoader;

public final class EngineFactory {
    private EngineFactory() { }

    public static Engine create() {
        initInfrastructure();
        EngineContext engineContext = createEngineContext();
        CommandProcessor commandProcessor = createCommandProcessor(engineContext);
        return new EngineImpl(commandProcessor);
    }

    private static void initInfrastructure() {
        DistanceMap.initialize(new DefaultDistanceLoader());
        SpeedMap.initialize(new DefaultSpeedLoader());
    }

    private static EngineContext createEngineContext() {
        // 1) initial speed model (default = constant)
        SpeedService initialSpeedModel = new ConstantSpeedService();

        // 2) initial route scheduler
        RouteScheduleService initialScheduler = new RouteScheduleService(initialSpeedModel);

        // 3) repository with that route scheduler
        LogisticsRepository repo = new LogisticsRepositoryImpl(
                new DefaultVehicleLoader(),
                initialScheduler
        );

        // 4) context bundles repo, speed model and route scheduler
        return new EngineContext(repo, initialSpeedModel, initialScheduler);
    }

    private static CommandProcessor createCommandProcessor(EngineContext engineContext) {
        CommandFactory commandFactory = new CommandFactoryImpl(engineContext);
        return new CommandProcessor(commandFactory);
    }
}
