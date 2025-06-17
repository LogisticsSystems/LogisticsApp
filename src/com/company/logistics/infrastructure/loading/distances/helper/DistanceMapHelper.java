package com.company.logistics.infrastructure.loading.distances.helper;

import com.company.logistics.enums.City;

import java.util.EnumMap;
import java.util.Map;

public class DistanceMapHelper {
    public static void addSymmetric(Map<City, Map<City, Integer>> map, City from, City to, int distance) {
        map.computeIfAbsent(from, k -> new EnumMap<>(City.class)).put(to, distance);
        map.computeIfAbsent(to, k -> new EnumMap<>(City.class)).put(from, distance);
    }
}
