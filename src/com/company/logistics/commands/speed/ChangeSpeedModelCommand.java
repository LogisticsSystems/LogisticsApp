package com.company.logistics.commands.speed;

import com.company.logistics.commands.CommandsConstants;
import com.company.logistics.commands.contracts.Command;
import com.company.logistics.core.contracts.LogisticsRepository;
import com.company.logistics.core.services.routing.computing.RouteRecalculatorService;
import com.company.logistics.core.services.speeds.SpeedModelService;
import com.company.logistics.enums.SpeedModelType;
import com.company.logistics.utils.ErrorMessages;
import com.company.logistics.utils.ParsingHelpers;
import com.company.logistics.utils.ValidationHelper;

import java.util.List;

public class ChangeSpeedModelCommand implements Command {
    private static final int EXPECTED_NUMBER_OF_ARGUMENTS = 1;
    private static final int MAX_ROUTES = 10;

    private final LogisticsRepository      repository;
    private final SpeedModelService        speedModelService;
    private final RouteRecalculatorService routeRecalculatorService;


    public ChangeSpeedModelCommand(LogisticsRepository repository,
                                   SpeedModelService speedModelService,
                                   RouteRecalculatorService routeRecalculatorService) {
        this.repository = repository;
        this.speedModelService = speedModelService;
        this.routeRecalculatorService = routeRecalculatorService;
    }

    @Override
    public String execute(List<String> parameters) {
        // 1) validate arguments
        ValidationHelper.validateArgumentsCount(parameters, EXPECTED_NUMBER_OF_ARGUMENTS);

        // 2) guard on number of routes
        ValidationHelper.validateListSizeAtMost(
                repository.getRoutes(),
                "routes",
                MAX_ROUTES,
                ErrorMessages.SPEED_MODEL_MAX_ROUTES_EXCEED
        );

        // 3) try parse speed model
        SpeedModelType newSpeedModelType = ParsingHelpers.tryParseEnum(
                parameters.get(0).toUpperCase(),
                SpeedModelType.class,
                ErrorMessages.UNKNOWN_SPEED_MODEL
        );


        // 4) swap model
        speedModelService.changeModel(newSpeedModelType);

        // 5) recompute all schedules + ETAs
        routeRecalculatorService.recomputeAll();

        return String.format(CommandsConstants.SPEED_MODEL_SWITCH, newSpeedModelType);
    }
}
