package com.company.logistics.core.services.speeds.implementation;

import com.company.logistics.core.services.speeds.SpeedService;
import com.company.logistics.enums.City;
import com.company.logistics.infrastructure.SpeedMap;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.EnumMap;
import java.util.Map;

public class SeasonalSpeedService implements SpeedService {
    private static final double DEFAULT_FACTOR = 1.00;
    private static final double SUMMER_FACTOR  = 1.10;
    private static final double WINTER_FACTOR  = 0.85;

    private static final Map<Month, Double> MONTH_FACTORS = new EnumMap<>(Month.class);

    static {
        // summer
        MONTH_FACTORS.put(Month.DECEMBER, SUMMER_FACTOR);
        MONTH_FACTORS.put(Month.JANUARY,  SUMMER_FACTOR);
        MONTH_FACTORS.put(Month.FEBRUARY, SUMMER_FACTOR);
        // winter
        MONTH_FACTORS.put(Month.JUNE,     WINTER_FACTOR);
        MONTH_FACTORS.put(Month.JULY,     WINTER_FACTOR);
        MONTH_FACTORS.put(Month.AUGUST,   WINTER_FACTOR);
    }

    @Override
    public double getSpeed(City from, City to, LocalDateTime departure) {
        double base   = SpeedMap.getInstance().getBaseSpeed(from, to);
        Month  month  = departure.getMonth();
        double factor = MONTH_FACTORS.getOrDefault(month, DEFAULT_FACTOR);
        return base * factor;
    }
}
