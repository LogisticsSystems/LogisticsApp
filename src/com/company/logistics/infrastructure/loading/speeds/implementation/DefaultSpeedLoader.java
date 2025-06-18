package com.company.logistics.infrastructure.loading.speeds.implementation;

import com.company.logistics.enums.City;
import com.company.logistics.infrastructure.loading.speeds.contracts.SpeedLoader;

import java.util.EnumMap;
import java.util.Map;

import static com.company.logistics.infrastructure.loading.speeds.helper.SpeedMapHelper.addSymmetric;

public class DefaultSpeedLoader implements SpeedLoader {
    @Override
    public Map<City, Map<City, Double>> loadBaseSpeeds() {
        Map<City, Map<City, Double>> speeds = new EnumMap<>(City.class);

        addSymmetric(speeds, City.SYD, City.MEL, 90.0);
        addSymmetric(speeds, City.SYD, City.ADL, 85.0);
        addSymmetric(speeds, City.SYD, City.ASP, 80.0);
        addSymmetric(speeds, City.SYD, City.BRI, 88.0);
        addSymmetric(speeds, City.SYD, City.DAR, 75.0);
        addSymmetric(speeds, City.SYD, City.PER, 78.0);

        addSymmetric(speeds, City.MEL, City.ADL, 88.0);
        addSymmetric(speeds, City.MEL, City.ASP, 82.0);
        addSymmetric(speeds, City.MEL, City.BRI, 83.0);
        addSymmetric(speeds, City.MEL, City.DAR, 76.0);
        addSymmetric(speeds, City.MEL, City.PER, 79.0);

        addSymmetric(speeds, City.ADL, City.ASP, 84.0);
        addSymmetric(speeds, City.ADL, City.BRI, 80.0);
        addSymmetric(speeds, City.ADL, City.DAR, 78.0);
        addSymmetric(speeds, City.ADL, City.PER, 77.0);

        addSymmetric(speeds, City.ASP, City.BRI, 86.0);
        addSymmetric(speeds, City.ASP, City.DAR, 74.0);
        addSymmetric(speeds, City.ASP, City.PER, 76.0);

        addSymmetric(speeds, City.BRI, City.DAR, 73.0);
        addSymmetric(speeds, City.BRI, City.PER, 75.0);

        addSymmetric(speeds, City.DAR, City.PER, 72.0);

        return speeds;
    }
}
