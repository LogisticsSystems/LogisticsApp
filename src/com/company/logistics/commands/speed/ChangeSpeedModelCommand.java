package com.company.logistics.commands.speed;

import com.company.logistics.commands.CommandsConstants;
import com.company.logistics.commands.contracts.Command;
import com.company.logistics.core.context.EngineContext;
import com.company.logistics.core.services.routing.RouteScheduleService;
import com.company.logistics.core.services.speeds.implementation.ConstantSpeedService;
import com.company.logistics.core.services.speeds.implementation.SeasonalSpeedService;
import com.company.logistics.core.services.speeds.implementation.SinusoidalSpeedService;
import com.company.logistics.enums.City;
import com.company.logistics.enums.SpeedModelType;
import com.company.logistics.models.contracts.DeliveryPackage;
import com.company.logistics.models.contracts.Route;
import com.company.logistics.utils.ErrorMessages;
import com.company.logistics.utils.ParsingHelpers;
import com.company.logistics.utils.ValidationHelper;

import java.time.LocalDateTime;
import java.util.List;

public class ChangeSpeedModelCommand implements Command {
    private static final int EXPECTED_NUMBER_OF_ARGUMENTS = 1;
    private static final int MAX_ROUTES = 10;

    private final EngineContext engineContext;

    public ChangeSpeedModelCommand(EngineContext engineContext) {
        this.engineContext = engineContext;
    }

    @Override
    public String execute(List<String> parameters) {
        SpeedModelType speedModelType = parseAndValidate(parameters);

        ValidationHelper.validateListSizeAtMost(engineContext.getRepository().getRoutes(),
                "routes",
                MAX_ROUTES,
                ErrorMessages.SPEED_MODEL_MAX_ROUTES_EXCEED);

        updateSpeedService(speedModelType);
        rebuildRouteScheduler();
        recomputeAllRoutesAndPackages();

        return String.format(CommandsConstants.SPEED_MODEL_SWITCH, speedModelType);
    }

    private SpeedModelType parseAndValidate(List<String> params) {
        ValidationHelper.validateArgumentsCount(params, EXPECTED_NUMBER_OF_ARGUMENTS);
        return ParsingHelpers.tryParseEnum(
                params.get(0).toUpperCase(),
                SpeedModelType.class,
                ErrorMessages.UNKNOWN_SPEED_MODEL
        );
    }

    private void updateSpeedService(SpeedModelType type) {
        switch (type) {
            case CONSTANT -> engineContext.setSpeedService(new ConstantSpeedService());
            case SEASONAL -> engineContext.setSpeedService(new SeasonalSpeedService());
            case SINUSOIDAL -> engineContext.setSpeedService(new SinusoidalSpeedService());
        }
    }

    private void rebuildRouteScheduler() {
        RouteScheduleService scheduler = new RouteScheduleService(engineContext.getSpeedService());
        engineContext.setRouteScheduler(scheduler);
    }

    private void recomputeAllRoutesAndPackages() {
        RouteScheduleService scheduler = engineContext.getRouteScheduler();

        List<Route> routes = engineContext.getRepository().getRoutes();
        for (Route route : routes) {
            List<City> stops = route.getLocations();
            LocalDateTime departureTime = route.getDepartureTime();

            List<LocalDateTime> newRouteSchedule = scheduler.computeSchedule(stops, departureTime);
            route.setSchedule(newRouteSchedule);

            for (DeliveryPackage pkg : route.getAssignedPackages()) {
                LocalDateTime eta = scheduler.getEtaForCity(pkg.getEndLocation(), stops, newRouteSchedule);
                pkg.setExpectedArrival(eta);
            }
        }
    }
}
