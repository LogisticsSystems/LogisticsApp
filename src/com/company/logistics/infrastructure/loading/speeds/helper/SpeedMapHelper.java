package com.company.logistics.infrastructure.loading.speeds.helper;

import com.company.logistics.enums.City;

import java.util.EnumMap;
import java.util.Map;

public class SpeedMapHelper {
    public static void addSymmetric(Map<City, Map<City, Double>> m, City a, City b, double speed) {
        m.computeIfAbsent(a, k -> new EnumMap<>(City.class)).put(b, speed);
        m.computeIfAbsent(b, k -> new EnumMap<>(City.class)).put(a, speed);
    }
}
