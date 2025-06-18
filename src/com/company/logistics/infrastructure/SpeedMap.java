package com.company.logistics.infrastructure;

import com.company.logistics.enums.City;
import com.company.logistics.infrastructure.loading.speeds.contracts.SpeedLoader;
import com.company.logistics.utils.ErrorMessages;
import com.company.logistics.utils.ValidationHelper;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

public class SpeedMap {
    private static SpeedMap instance;
    private final Map<City, Map<City, Double>> baseSpeeds;

    private SpeedMap(SpeedLoader loader) {
        Map<City, Map<City, Double>> loaded = loader.loadBaseSpeeds();
        this.baseSpeeds = createImmutableDeepCopy(loaded);
    }

    public static synchronized void initialize(SpeedLoader loader) {
        ValidationHelper.validateNotAlreadyInitialized(instance, "SpeedMap");
        instance = new SpeedMap(loader);
    }

    public static SpeedMap getInstance() {
        ValidationHelper.validateInitialized(instance, "SpeedMap");
        return instance;
    }

    public double getBaseSpeed(City from, City to) {
        ValidationHelper.validateKeyPairInNestedMap(from, to, baseSpeeds, ErrorMessages.UNKNOWN_FROM_CITY, ErrorMessages.NO_SPEED_DEFINED);
        return baseSpeeds.get(from).get(to);
    }

    private Map<City, Map<City, Double>> createImmutableDeepCopy(Map<City, Map<City, Double>> loaded) {
        Map<City, Map<City, Double>> result = new EnumMap<>(City.class);
        for (Map.Entry<City, Map<City, Double>> entry : loaded.entrySet()) {
            result.put(entry.getKey(), Collections.unmodifiableMap(new EnumMap<>(entry.getValue())));
        }
        return Collections.unmodifiableMap(result);
    }
}
