package com.company.logistics.core.services.speeds;

import com.company.logistics.core.contracts.LogisticsRepository;
import com.company.logistics.core.services.routing.scheduling.RouteScheduleService;
import com.company.logistics.core.services.speeds.contract.SpeedModel;
import com.company.logistics.core.services.speeds.implementation.ConstantSpeedModel;
import com.company.logistics.core.services.speeds.implementation.SeasonalSpeedModel;
import com.company.logistics.core.services.speeds.implementation.SinusoidalSpeedModel;
import com.company.logistics.enums.SpeedModelType;
import com.company.logistics.models.contracts.DeliveryPackage;
import com.company.logistics.models.contracts.Route;
import com.company.logistics.utils.ErrorMessages;
import com.company.logistics.utils.ValidationHelper;

import java.time.LocalDateTime;
import java.util.List;

public class SpeedModelService {
    private static final int MAX_ROUTES = 10;

    private final LogisticsRepository repository;
    private SpeedModel speedModel;
    private RouteScheduleService routeScheduleService;

    public SpeedModelService(LogisticsRepository repository, SpeedModel speedModel) {
        this.repository = repository;
        this.speedModel = speedModel;
        this.routeScheduleService = new RouteScheduleService(speedModel);
    }

    /** swap to a new model and rebuild the scheduler under the hood */
    public void changeModel(SpeedModelType type) {
        ValidationHelper.validateListSizeAtMost(
                repository.getRoutes(),
                "routes",
                MAX_ROUTES,
                ErrorMessages.SPEED_MODEL_MAX_ROUTES_EXCEED);

        switch(type) {
            case CONSTANT   ->   speedModel = new ConstantSpeedModel();
            case SEASONAL   ->   speedModel = new SeasonalSpeedModel();
            case SINUSOIDAL ->   speedModel = new SinusoidalSpeedModel();
        }
        routeScheduleService = new RouteScheduleService(speedModel);

        recomputeAllRoutesAndEtas();
    }

    /** everything else just grabs the up-to-date scheduler here */
    public RouteScheduleService getRouteScheduleService() {
        return routeScheduleService;
    }

    public SpeedModel getSpeedModel() {
        return speedModel;
    }

    /** recomputes every route & package ETA against the current scheduler */
    private void recomputeAllRoutesAndEtas() {
        for (Route route : repository.getRoutes()) {
            List<LocalDateTime> newSchedule = routeScheduleService.computeSchedule(route.getLocations(), route.getDepartureTime());
            route.setSchedule(newSchedule);

            for (DeliveryPackage pkg : route.getAssignedPackages()) {
                LocalDateTime eta = routeScheduleService.getEtaForCity(
                        pkg.getEndLocation(),
                        route.getLocations(),
                        newSchedule
                );
                pkg.setExpectedArrival(eta);
            }
        }
    }
}
