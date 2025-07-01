package com.company.logistics.services.speeds.implementation;

import com.company.logistics.services.speeds.contract.SpeedModel;
import com.company.logistics.enums.City;
import com.company.logistics.infrastructure.SpeedMap;

import java.time.LocalDateTime;

public class ConstantSpeedModel implements SpeedModel {

    @Override
    public double getSpeed(City from, City to, LocalDateTime departure) {
        return SpeedMap.getInstance().getBaseSpeed(from, to);
    }
}
