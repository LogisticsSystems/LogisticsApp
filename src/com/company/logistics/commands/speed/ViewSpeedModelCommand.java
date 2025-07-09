package com.company.logistics.commands.speed;

import com.company.logistics.commands.contracts.Command;
import com.company.logistics.services.speeds.SpeedModelService;
import com.company.logistics.services.speeds.contract.SpeedModel;
import com.company.logistics.utils.PrintConstants;
import com.company.logistics.utils.ValidationHelper;

import java.util.List;

public class ViewSpeedModelCommand implements Command {
    private static final int EXPECTED_NUMBER_OF_ARGUMENTS = 0;

    private final SpeedModelService speedModelService;

    public ViewSpeedModelCommand(SpeedModelService speedModelService) {
        this.speedModelService = speedModelService;
    }

    @Override
    public String execute(List<String> parameters) {
        ValidationHelper.validateArgumentsCount(parameters, EXPECTED_NUMBER_OF_ARGUMENTS);

        SpeedModel speedModel = speedModelService.getSpeedModel();
        String modelName = speedModel.getClass().getSimpleName().replace("SpeedModel", "");

        return PrintConstants.LINE_BREAK + "\n" + String.format(PrintConstants.CURRENT_SPEED_MODEL, modelName);
    }
}
