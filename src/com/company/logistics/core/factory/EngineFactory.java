package com.company.logistics.core.factory;

import com.company.logistics.core.contracts.CommandFactory;
import com.company.logistics.core.contracts.Engine;
import com.company.logistics.core.contracts.LogisticsRepository;
import com.company.logistics.core.implementation.CommandFactoryImpl;
import com.company.logistics.core.implementation.EngineImpl;
import com.company.logistics.core.implementation.LogisticsRepositoryImpl;
import com.company.logistics.core.services.EngineHelper;
import com.company.logistics.infrastructure.DistanceMap;
import com.company.logistics.infrastructure.loading.distances.implementation.DefaultDistanceLoader;
import com.company.logistics.infrastructure.loading.vehicles.implementation.DefaultVehicleLoader;

public final class EngineFactory {
    private EngineFactory() { }

    public static Engine create() {
        // initialize shared infrastructure
        DistanceMap.initialize(new DefaultDistanceLoader());

        LogisticsRepository repo = new LogisticsRepositoryImpl(new DefaultVehicleLoader());

        CommandFactory cmdFactory = new CommandFactoryImpl();
        EngineHelper helper = new EngineHelper(cmdFactory, repo);

        return new EngineImpl(helper);
    }
}
