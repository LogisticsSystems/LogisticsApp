package com.company.logistics.services.speeds;

import com.company.logistics.services.routing.scheduling.RouteScheduleService;
import com.company.logistics.services.speeds.contract.SpeedModel;
import com.company.logistics.services.speeds.implementation.ConstantSpeedModel;
import com.company.logistics.services.speeds.implementation.SeasonalSpeedModel;
import com.company.logistics.services.speeds.implementation.SinusoidalSpeedModel;
import com.company.logistics.enums.SpeedModelType;

public class SpeedModelService {
    private SpeedModel speedModel;

    public SpeedModelService(SpeedModel speedModel) {
        this.speedModel = speedModel;
    }

    public void changeModel(SpeedModelType type) {
        switch(type) {
            case CONSTANT   ->   speedModel = new ConstantSpeedModel();
            case SEASONAL   ->   speedModel = new SeasonalSpeedModel();
            case SINUSOIDAL ->   speedModel = new SinusoidalSpeedModel();
        }
    }

    public RouteScheduleService getRouteScheduler() {
        return new RouteScheduleService(speedModel);
    }

    public SpeedModel getSpeedModel() {
        return speedModel;
    }
}
