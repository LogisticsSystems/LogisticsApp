package com.company.logistics.core.services.speeds;

import com.company.logistics.core.services.routing.scheduling.RouteScheduleService;
import com.company.logistics.core.services.speeds.contract.SpeedModel;
import com.company.logistics.core.services.speeds.implementation.ConstantSpeedModel;
import com.company.logistics.core.services.speeds.implementation.SeasonalSpeedModel;
import com.company.logistics.core.services.speeds.implementation.SinusoidalSpeedModel;
import com.company.logistics.enums.SpeedModelType;

public class SpeedModelService {
    private SpeedModel speedModel;

    public SpeedModelService(SpeedModel speedModel) {
        this.speedModel = speedModel;
    }

    /** Swap to a new model */
    public void changeModel(SpeedModelType type) {
        switch(type) {
            case CONSTANT   ->   speedModel = new ConstantSpeedModel();
            case SEASONAL   ->   speedModel = new SeasonalSpeedModel();
            case SINUSOIDAL ->   speedModel = new SinusoidalSpeedModel();
        }
    }

    /** Get a brand-new scheduler backed by whatever model is live now. */
    public RouteScheduleService getRouteScheduler() {
        return new RouteScheduleService(speedModel);
    }

    public SpeedModel getSpeedModel() {
        return speedModel;
    }
}
