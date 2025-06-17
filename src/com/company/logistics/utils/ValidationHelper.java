package com.company.logistics.utils;
import com.company.logistics.enums.City;

import java.util.List;
import java.util.Map;


public class ValidationHelper {

    public static void validateArgumentsCount(List<String> parameters, int EXPECTED_NUMBER_OF_ARGUMENTS) {
        //TODO
    }


    // Validation methods specific to DistanceMap operations
    public static void validateNotAlreadyInitialized(Object instance, String name) {
        if (instance != null) {
            throw new IllegalStateException(String.format(ErrorMessages.ALREADY_INITIALIZED, name));
        }
    }

    public static void validateInitialized(Object instance, String name) {
        if (instance == null) {
            throw new IllegalStateException(String.format(ErrorMessages.NOT_INITIALIZED, name));
        }
    }

    public static void validateNotNull(Object obj, String paramName) {
        if (obj == null) {
            throw new IllegalArgumentException(String.format(ErrorMessages.NOT_NULL, paramName));
        }
    }

    public static void validateCityInDistanceMap(City from, City to, Map<City, Map<City, Integer>> distances) {
        validateNotNull(from, "from city");
        validateNotNull(to, "to city");
        if (!distances.containsKey(from)) {
            throw new IllegalArgumentException(String.format(ErrorMessages.UNKNOWN_FROM_CITY, from));
        }
        Map<City, Integer> row = distances.get(from);
        if (!row.containsKey(to)) {
            throw new IllegalArgumentException(String.format(ErrorMessages.NO_DISTANCE_DEFINED, from, to));
        }
    }
}
