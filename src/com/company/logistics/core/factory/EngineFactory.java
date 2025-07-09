package com.company.logistics.core.factory;

import com.company.logistics.core.context.EngineContext;
import com.company.logistics.core.contracts.CommandFactory;
import com.company.logistics.core.contracts.Engine;
import com.company.logistics.core.contracts.LogisticsRepository;
import com.company.logistics.core.implementation.CommandFactoryImpl;
import com.company.logistics.core.implementation.EngineImpl;
import com.company.logistics.core.implementation.LogisticsRepositoryImpl;
import com.company.logistics.services.assignment.AssignmentService;
import com.company.logistics.services.assignment.strategy.implementation.DefaultPackageAssignmentStrategy;
import com.company.logistics.services.assignment.strategy.implementation.DefaultPackageRemovalStrategy;
import com.company.logistics.services.assignment.strategy.implementation.DefaultTruckAssignmentStrategy;
import com.company.logistics.services.assignment.strategy.implementation.DefaultTruckRemovalStrategy;
import com.company.logistics.services.delivery.PackageDeliveryService;
import com.company.logistics.services.engine.CommandProcessor;
import com.company.logistics.services.routing.computing.RouteRecalculatorService;
import com.company.logistics.services.routing.management.RouteCreationService;
import com.company.logistics.services.speeds.SpeedModelService;
import com.company.logistics.services.speeds.implementation.ConstantSpeedModel;
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

        CommandFactory   commandFactory   = new CommandFactoryImpl(engineContext);
        CommandProcessor commandProcessor = new CommandProcessor(commandFactory);
        return new EngineImpl(commandProcessor);
    }

    private static void initInfrastructure() {
        DistanceMap.initialize(new DefaultDistanceLoader());
        SpeedMap.initialize(new DefaultSpeedLoader());
    }

    private static EngineContext createEngineContext() {
        LogisticsRepository  repository = new LogisticsRepositoryImpl(new DefaultVehicleLoader());

        SpeedModelService speedModelService               = new SpeedModelService(new ConstantSpeedModel());
        RouteCreationService routeCreationService         = new RouteCreationService(repository, speedModelService);
        RouteRecalculatorService routeRecalculatorService = new RouteRecalculatorService(repository, speedModelService);

        DefaultPackageAssignmentStrategy packageAssignmentStrategy = new DefaultPackageAssignmentStrategy(repository, speedModelService);
        DefaultTruckAssignmentStrategy truckAssignmentStrategy     = new DefaultTruckAssignmentStrategy(repository);
        DefaultPackageRemovalStrategy packageRemovalStrategy       = new DefaultPackageRemovalStrategy(repository);
        DefaultTruckRemovalStrategy truckRemovalStrategy           = new DefaultTruckRemovalStrategy(repository);
        AssignmentService assignmentService = new AssignmentService(
                packageAssignmentStrategy,
                truckAssignmentStrategy,
                truckRemovalStrategy,
                packageRemovalStrategy);


        PackageDeliveryService deliveryService = new PackageDeliveryService(repository);

        return new EngineContext(
                repository,
                speedModelService,
                deliveryService,
                assignmentService,
                routeCreationService,
                routeRecalculatorService
        );
    }
}
