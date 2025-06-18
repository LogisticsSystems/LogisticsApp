package com.company.logistics.core.services.speeds.implementation;

import com.company.logistics.core.services.speeds.SpeedService;
import com.company.logistics.enums.City;
import com.company.logistics.infrastructure.SpeedMap;

import java.time.LocalDateTime;

public class ConstantSpeedService implements SpeedService {

    @Override
    public double getSpeed(City from, City to, LocalDateTime departure) {
        return SpeedMap.getInstance().getBaseSpeed(from, to);
    }
}
