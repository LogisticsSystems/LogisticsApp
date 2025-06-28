package com.company.logistics.core.factory;

import com.company.logistics.core.context.EngineContext;
import com.company.logistics.core.contracts.CommandFactory;
import com.company.logistics.core.contracts.Engine;
import com.company.logistics.core.contracts.LogisticsRepository;
import com.company.logistics.core.implementation.CommandFactoryImpl;
import com.company.logistics.core.implementation.EngineImpl;
import com.company.logistics.core.implementation.LogisticsRepositoryImpl;
import com.company.logistics.core.services.assignment.AssignmentService;
import com.company.logistics.core.services.delivery.PackageDeliveryService;
import com.company.logistics.core.services.engine.CommandProcessor;
import com.company.logistics.core.services.routing.RouteCreationService;
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
        // 1) initialize shared infrastructure
        DistanceMap.initialize(new DefaultDistanceLoader());
        SpeedMap.initialize(new DefaultSpeedLoader());

        // 2) build the four “core” collaborators
        SpeedService           initialSpeed          = new ConstantSpeedService();
        RouteScheduleService   initialScheduler      = new RouteScheduleService(initialSpeed);
        LogisticsRepository    repo                  = new LogisticsRepositoryImpl(new DefaultVehicleLoader());
        PackageDeliveryService deliveryService       = new PackageDeliveryService(repo);
        AssignmentService      assignmentService     = new AssignmentService(repo, initialScheduler);

        // 3) bundle into EngineContext
        EngineContext engineContext = new EngineContext(
                repo,
                initialSpeed,
                initialScheduler,
                deliveryService,
                assignmentService
        );

        // 4) now build & attach the creation service with the context
        engineContext.setRouteCreationService(new RouteCreationService(engineContext));

        // 5) build command processor + engine
        CommandFactory   commandFactory = new CommandFactoryImpl(engineContext);
        CommandProcessor commandProcessor = new CommandProcessor(commandFactory);
        return new EngineImpl(commandProcessor);
    }
}
