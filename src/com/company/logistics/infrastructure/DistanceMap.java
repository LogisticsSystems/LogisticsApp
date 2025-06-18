package com.company.logistics.infrastructure;

import com.company.logistics.enums.City;
import com.company.logistics.infrastructure.loading.distances.contracts.DistanceLoader;
import com.company.logistics.utils.ErrorMessages;
import com.company.logistics.utils.ValidationHelper;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

public final class DistanceMap {
    private static DistanceMap instance;

    private final Map<City, Map<City, Integer>> distances;

    private DistanceMap(DistanceLoader loader) {
        Map<City, Map<City, Integer>> loaded = loader.loadDistances();
        this.distances = createImmutableDeepCopy(loaded);
    }

    public static synchronized void initialize(DistanceLoader loader) {
        ValidationHelper.validateNotAlreadyInitialized(instance, "DistanceMap");
        instance = new DistanceMap(loader);
    }

    public static DistanceMap getInstance() {
        ValidationHelper.validateInitialized(instance, "DistanceMap");
        return instance;
    }

    public int getDistance(City from, City to) {
        ValidationHelper.validateKeyPairInNestedMap(from, to, distances, ErrorMessages.UNKNOWN_FROM_CITY, ErrorMessages.NO_DISTANCE_DEFINED);
        return distances.get(from).get(to);
    }

    public boolean isValidCity(City city) {
        ValidationHelper.validateNotNull(city, "City");
        return distances.containsKey(city);
    }

    public Set<City> getAllCities() {
        return distances.keySet();
    }

    private Map<City, Map<City, Integer>> createImmutableDeepCopy(Map<City, Map<City, Integer>> loaded) {
        Map<City, Map<City, Integer>> result = new EnumMap<>(City.class);
        for (Map.Entry<City, Map<City, Integer>> entry : loaded.entrySet()) {
            result.put(entry.getKey(), Collections.unmodifiableMap(new EnumMap<>(entry.getValue())));
        }
        return Collections.unmodifiableMap(result);
    }
}
