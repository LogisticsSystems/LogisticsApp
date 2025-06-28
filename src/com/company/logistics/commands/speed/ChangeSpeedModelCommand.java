package com.company.logistics.commands.speed;

import com.company.logistics.commands.CommandsConstants;
import com.company.logistics.commands.contracts.Command;
import com.company.logistics.core.services.speeds.SpeedModelService;
import com.company.logistics.enums.SpeedModelType;
import com.company.logistics.utils.ErrorMessages;
import com.company.logistics.utils.ParsingHelpers;
import com.company.logistics.utils.ValidationHelper;

import java.util.List;

public class ChangeSpeedModelCommand implements Command {
    private static final int EXPECTED_NUMBER_OF_ARGUMENTS = 1;

    private final SpeedModelService speedModelService;

    public ChangeSpeedModelCommand(SpeedModelService speedModelService) {
        this.speedModelService = speedModelService;
    }

    @Override
    public String execute(List<String> parameters) {

        ValidationHelper.validateArgumentsCount(parameters, EXPECTED_NUMBER_OF_ARGUMENTS);

        SpeedModelType newSpeedModelType = ParsingHelpers.tryParseEnum(
                parameters.get(0).toUpperCase(),
                SpeedModelType.class,
                ErrorMessages.UNKNOWN_SPEED_MODEL
        );


        // swap model, rebuild scheduler and recompute all existing routes/packages
        speedModelService.changeModel(newSpeedModelType);

        return String.format(CommandsConstants.SPEED_MODEL_SWITCH, newSpeedModelType);
    }
}
