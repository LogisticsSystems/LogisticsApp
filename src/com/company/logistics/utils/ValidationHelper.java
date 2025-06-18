package com.company.logistics.utils;
import com.company.logistics.enums.City;

import java.util.List;
import java.util.Map;


public class ValidationHelper {

    public static void validateArgumentsCount(List<String> parameters, int EXPECTED_NUMBER_OF_ARGUMENTS) {
        if(parameters.size()<EXPECTED_NUMBER_OF_ARGUMENTS){
            throw new IllegalArgumentException(String.format(ErrorMessages.INVALID_ARGUMENTS_COUNT
                    ,EXPECTED_NUMBER_OF_ARGUMENTS,parameters.size()));
        }
    }

    public static <T> void validateListSizeAtLeast(List<T> list, String name, int minSize) {
        validateNotNull(list, name);
        if (list.size() < minSize) {
            throw new IllegalArgumentException(String.format(ErrorMessages.MIN_LIST_SIZE, name, minSize));
        }
    }

    public static <T> void validateListSizeEquals(List<T> list, String name, int expectedSize) {
        validateNotNull(list, name);
        if (list.size() != expectedSize) {
            throw new IllegalArgumentException(String.format(ErrorMessages.EXACT_LIST_SIZE, name, expectedSize));
        }
    }

    public static void validateIntPositive(int intToValidate, String type) {
        if (intToValidate <= 0) {
            throw new IllegalArgumentException(String.format(ErrorMessages.NON_POSITIVE_INT, type));
        }
    }

    public static void validateDoubleNonNegative(double dataToValidate,String type){
        if(dataToValidate<0){
            throw new IllegalArgumentException(String.format(ErrorMessages.NEGATIVE_DOUBLE,type));
        }
    }

    public static void valideStringLenght(String stringToValidate, int minLenght, int maxLenght, String type){
        validateIntRange(stringToValidate.length()
                ,minLenght
                ,maxLenght
                ,type);
    }

    private static void validateIntRange(int numToValidate, int minLenght, int maxLenght, String type) {

        if(numToValidate<minLenght || numToValidate>maxLenght){
            throw new IllegalArgumentException(String.format(ErrorMessages.STRING_NOT_IN_RANGE
                    ,type,minLenght,maxLenght));
        }
    }

    public static void validatePackageRouteCompatibility(City start, City end, List<City> stops) {
        validateNotNull(start, "startLocation");
        validateNotNull(end,   "endLocation");

        int idxFrom = stops.indexOf(start);
        if (idxFrom < 0) {
            throw new IllegalArgumentException(String.format(ErrorMessages.CITY_NOT_ON_ROUTE, start));
        }

        int idxTo = stops.indexOf(end);
        if (idxTo < 0) {
            throw new IllegalArgumentException(String.format(ErrorMessages.CITY_NOT_ON_ROUTE, end));
        }

        if (idxTo <= idxFrom) {
            throw new IllegalArgumentException(String.format(ErrorMessages.PACKAGE_ROUTE_MISMATCH, start, end));
        }
    }

    // Validation methods specific to DistanceMap and SpeedMap operations
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

    public static <K, V> void validateKeyPairInNestedMap(
            K from,
            K to,
            Map<K, Map<K, V>> map,
            String unknownFromMsg,
            String noValueMsg
    ) {
        validateNotNull(from, "from");
        validateNotNull(to,   "to");
        if (!map.containsKey(from)) {
            throw new IllegalArgumentException(String.format(unknownFromMsg, from));
        }
        Map<K, V> inner = map.get(from);
        if (!inner.containsKey(to)) {
            throw new IllegalArgumentException(String.format(noValueMsg, from, to));
        }
    }

}
