package com.company.logistics.core.services.speeds.implementation;

import com.company.logistics.core.services.speeds.SpeedService;
import com.company.logistics.enums.City;
import com.company.logistics.infrastructure.SpeedMap;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Map;

public class SeasonalSpeedService implements SpeedService {
    private static final Map<Month, Double> MONTH_FACTORS = Map.ofEntries(
            Map.entry(Month.DECEMBER, 1.10), // summer ↑10%
            Map.entry(Month.JANUARY,  1.10),
            Map.entry(Month.FEBRUARY, 1.10),

            Map.entry(Month.JUNE,     0.85), // winter ↓15%
            Map.entry(Month.JULY,     0.85),
            Map.entry(Month.AUGUST,   0.85)
            // all other months will default to 1.00
    );

    @Override
    public double getSpeed(City from, City to, LocalDateTime departure) {
        double baseSpeed = SpeedMap.getInstance().getBaseSpeed(from, to);
        Month month = departure.getMonth();
        double factor = MONTH_FACTORS.getOrDefault(month, 1.00);
        return baseSpeed * factor;
    }
}
