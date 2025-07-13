package com.company.logistics.utils;

import com.company.logistics.enums.City;
import com.company.logistics.infrastructure.DistanceMap;
import com.company.logistics.models.contracts.DeliveryPackage;

import java.util.List;

public class CalculationHelpers {

    public static double calculateTotalLoad(List<DeliveryPackage> packages) {
        return packages.stream()
                .mapToDouble(DeliveryPackage::getWeightKg)
                .sum();
    }

    public static double calculateTotalDistance(List<City> stops) {
        double dist = 0;
        for (int i = 0; i < stops.size() - 1; i++) {
            dist += DistanceMap.getInstance()
                    .getDistance(stops.get(i), stops.get(i + 1));
        }
        return dist;
    }
}
