package com.company.logistics.services.speeds.implementation;

import com.company.logistics.services.speeds.contract.SpeedModel;
import com.company.logistics.enums.City;
import com.company.logistics.infrastructure.SpeedMap;

import java.time.LocalDateTime;

public class SinusoidalSpeedModel implements SpeedModel {
    private static final int    CYCLE_DAYS       = 365;
    private static final double AMPLITUDE        = 10.0;
    private static final int    PEAK_DAY_OF_YEAR = 26;
    private static final int    CYCLE_SPLIT      = 4;

    // shift so that sin(2π*(PEAK_DAY_OF_YEAR – PHASE_SHIFT) / CYCLE_DAYS) == +1
    private static final double PHASE_SHIFT      = PEAK_DAY_OF_YEAR - ( (double) CYCLE_DAYS / CYCLE_SPLIT);

    @Override
    public double getSpeed(City from, City to, LocalDateTime departure) {
        double baseSpeed = SpeedMap.getInstance().getBaseSpeed(from, to);

        int    dayOfYear = departure.getDayOfYear();
        double theta     = 2 * Math.PI * (dayOfYear - PHASE_SHIFT) / CYCLE_DAYS;
        double adjustment= AMPLITUDE * Math.sin(theta);

        return baseSpeed + adjustment;
    }
}
