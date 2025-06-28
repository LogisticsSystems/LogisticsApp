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
        ValidationHelper.validateArgumentsCount(parameters, EXPECTED_NUMBER_OF_ARGUMENTS);
        SpeedModelType speedModelType = ParsingHelpers.tryParseEnum(
                parameters.get(0).toUpperCase(),
                SpeedModelType.class,
                ErrorMessages.UNKNOWN_SPEED_MODEL
        );

        ValidationHelper.validateListSizeAtMost(
                engineContext.getRepository().getRoutes(),
                "routes",
                MAX_ROUTES,
                ErrorMessages.SPEED_MODEL_MAX_ROUTES_EXCEED);

        switch (speedModelType) {
            case CONSTANT   -> engineContext.changeSpeedModel(new ConstantSpeedService());
            case SEASONAL   -> engineContext.changeSpeedModel(new SeasonalSpeedService());
            case SINUSOIDAL -> engineContext.changeSpeedModel(new SinusoidalSpeedService());
        }

        return String.format(CommandsConstants.SPEED_MODEL_SWITCH, speedModelType);
    }
}
