package com.company.logistics.commands.speed;

import com.company.logistics.commands.CommandsConstants;
import com.company.logistics.commands.contracts.Command;
import com.company.logistics.enums.UserRole;
import com.company.logistics.models.contracts.User;
import com.company.logistics.repositories.contracts.UserRepository;
import com.company.logistics.services.routing.computing.RouteRecalculatorService;
import com.company.logistics.services.speeds.SpeedModelService;
import com.company.logistics.enums.SpeedModelType;
import com.company.logistics.utils.ParsingHelpers;
import com.company.logistics.utils.ValidationHelper;

import java.util.List;

public class ChangeSpeedModelCommand implements Command {
    private static final int EXPECTED_NUMBER_OF_ARGUMENTS = 1;

    private final SpeedModelService        speedModelService;
    private final RouteRecalculatorService routeRecalculatorService;
    private final User loggedInUser;

    public ChangeSpeedModelCommand(SpeedModelService speedModelService,
                                   RouteRecalculatorService routeRecalculatorService,
                                   UserRepository userRepository) {
        this.speedModelService        = speedModelService;
        this.routeRecalculatorService = routeRecalculatorService;
        this.loggedInUser = userRepository.getLoggedInUser();
    }

    @Override
    public String execute(List<String> parameters) {
        validateLoggedInUser();

        ValidationHelper.validateArgumentsCount(parameters, EXPECTED_NUMBER_OF_ARGUMENTS);

        SpeedModelType newSpeedModelType = ParsingHelpers.tryParseEnum(
                parameters.get(0).toUpperCase(),
                SpeedModelType.class
        );

        speedModelService.changeModel(newSpeedModelType);
        routeRecalculatorService.recomputeAll();

        return String.format(CommandsConstants.SPEED_MODEL_SWITCH, newSpeedModelType);
    }

    private void validateLoggedInUser() {
        ValidationHelper.validateUserHasRole(loggedInUser, UserRole.MANAGER);
    }
}
