package com.company.logistics.core.services.speeds.implementation;

import com.company.logistics.core.services.speeds.SpeedService;
import com.company.logistics.enums.City;
import com.company.logistics.infrastructure.SpeedMap;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class SinusoidalSpeedService implements SpeedService {
    private static final int CYCLE_DIVISIONS = 4;

    private final double amplitude;
    private final double phaseShiftDays;

    public SinusoidalSpeedService(double amplitude, LocalDate peakDate) {
        this.amplitude = amplitude;
        int peakDay    = peakDate.getDayOfYear();
        int periodDays = peakDate.lengthOfYear();
        // solve for phase shift so that sin(2π*(peakDay - phaseShiftDays)/periodDays) == +1
        // ⇒ (peakDay - phaseShiftDays)/periodDays = 1/CYCLE_DIVISIONS
        this.phaseShiftDays = peakDay - (periodDays / (double) CYCLE_DIVISIONS);
    }

    @Override
    public double getSpeed(City from, City to, LocalDateTime departure) {
        double baseSpeed  = SpeedMap.getInstance().getBaseSpeed(from, to);
        LocalDate date    = departure.toLocalDate();
        int dayOfYear     = date.getDayOfYear();
        int periodDays    = date.lengthOfYear();

        double theta      = 2 * Math.PI * (dayOfYear - phaseShiftDays) / periodDays;
        double adjustment = amplitude * Math.sin(theta);
        return baseSpeed + adjustment;
    }
}
