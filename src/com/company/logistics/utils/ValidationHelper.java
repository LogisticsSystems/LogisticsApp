package com.company.logistics.utils;
import com.company.logistics.enums.City;
import com.company.logistics.enums.PackageStatus;
import com.company.logistics.infrastructure.DistanceMap;
import com.company.logistics.models.contracts.DeliveryPackage;

import java.util.List;
import java.util.Map;


public class ValidationHelper {
    private ValidationHelper() { }

    // --- Initialization -------------------------------------------------

    public static void validateNotAlreadyInitialized(Object instance, String name) {
        if (instance != null) {
            throw new IllegalStateException(String.format(
                    ErrorMessages.ALREADY_INITIALIZED, name));
        }
    }

    public static void validateInitialized(Object instance, String name) {
        if (instance == null) {
            throw new IllegalStateException(String.format(
                    ErrorMessages.NOT_INITIALIZED, name));
        }
    }

    // --- Null / Basic checks -------------------------------------------------

    public static void validateNotNull(Object obj, String paramName) {
        if (obj == null) {
            throw new IllegalArgumentException(String.format(
                    ErrorMessages.NOT_NULL, paramName));
        }
    }

    public static void validateArgumentsCount(List<String> params, int expected) {
        if (params.size() < expected) {
            throw new IllegalArgumentException(String.format(
                    ErrorMessages.INVALID_ARGUMENTS_COUNT, expected, params.size()));
        }
    }

    // --- Primitive validations -------------------------------------------------

    public static void validateIntPositive(int value, String name) {
        if (value <= 0) {
            throw new IllegalArgumentException(String.format(
                    ErrorMessages.NON_POSITIVE_INT, name));
        }
    }

    public static void validateDoubleNonNegative(double value, String name) {
        if (value < 0) {
            throw new IllegalArgumentException(String.format(
                    ErrorMessages.NEGATIVE_DOUBLE, name));
        }
    }

    public static void validateStringLength(String s, int min, int max, String name) {
        validateIntRange(s.length(), min, max, name);
    }

    private static void validateIntRange(int actual, int min, int max, String name) {
        if (actual < min || actual > max) {
            throw new IllegalArgumentException(String.format(
                    ErrorMessages.NUMBER_NOT_IN_RANGE, name, min, max));
        }
    }

    public static void validateDoubleRange(double actual, int min, int max, String name) {
        if (actual < min || actual > max) {
            throw new IllegalArgumentException(String.format(
                    ErrorMessages.NUMBER_NOT_IN_RANGE, name, min, max));
        }
    }

    // --- Collection size validations -------------------------------------------------

    public static <T> void validateListSizeAtLeast(List<T> list, String name, int min) {
        validateNotNull(list, name);
        if (list.size() < min) {
            throw new IllegalArgumentException(String.format(
                    ErrorMessages.MIN_LIST_SIZE, name, min));
        }
    }

    public static <T> void validateListSizeEquals(List<T> list, String name, int expected) {
        validateNotNull(list, name);
        if (list.size() != expected) {
            throw new IllegalArgumentException(String.format(
                    ErrorMessages.EXACT_LIST_SIZE, name, expected));
        }
    }

    public static <T> void validateListSizeAtMost(
            List<T> list, String name, int max, String errorMsgFmt) {
        validateNotNull(list, name);
        if (list.size() > max) {
            throw new IllegalStateException(String.format(errorMsgFmt,
                    list.size(), max));
        }
    }

    // --- Map / nested-map lookups -------------------------------------------------

    public static <K, V> void validateKeyPairInNestedMap(
            K from, K to, Map<K, Map<K, V>> map,
            String unknownFromMsg, String noValueMsg
    ) {
        validateNotNull(from, "from");
        validateNotNull(to,   "to");
        if (!map.containsKey(from)) {
            throw new IllegalArgumentException(String.format(
                    unknownFromMsg, from));
        }
        Map<K, V> inner = map.get(from);
        if (!inner.containsKey(to)) {
            throw new IllegalArgumentException(String.format(
                    noValueMsg, from, to));
        }
    }

    // --- Domain‐specific validations -------------------------------------------------

    /** Ensure start/end both appear in the route, in the correct order. */
    public static void validatePackageRouteCompatibility(
            City start, City end, List<City> stops
    ) {
        validateNotNull(start, "startLocation");
        validateNotNull(end,   "endLocation");

        int idxFrom = stops.indexOf(start);
        if (idxFrom < 0) {
            throw new IllegalArgumentException(String.format(
                    ErrorMessages.CITY_NOT_ON_ROUTE, start));
        }

        int idxTo = stops.indexOf(end);
        if (idxTo < 0) {
            throw new IllegalArgumentException(String.format(
                    ErrorMessages.CITY_NOT_ON_ROUTE, end));
        }

        if (idxTo <= idxFrom) {
            throw new IllegalArgumentException(String.format(
                    ErrorMessages.PACKAGE_ROUTE_MISMATCH, start, end));
        }
    }

    /** Guard that a package is in exactly the expected status. */
    public static void validatePackageStatus(
            DeliveryPackage pkg, PackageStatus expected
    ) {
        if (pkg.getStatus() != expected) {
            throw new IllegalArgumentException(String.format(
                    ErrorMessages.PACKAGE_STATUS_ERROR, expected));
        }
    }

    /** Total weight of packages must not exceed truck capacity. */
    public static void validateTotalLoadWithinCapacity(
            List<DeliveryPackage> packages,
            double capacityKg, int truckId, int routeId, String error
    ) {
        double load = Calculations.calculateTotalLoad(packages);

        if (load > capacityKg) {
            throw new IllegalArgumentException(String.format(
                    error));
        }
    }

    /** Sum of route legs must not exceed truck’s maximum range. */
    public static void validateRouteRangeWithin(
            List<City> stops, double maxRangeKm, int truckId, int routeId
    ) {
        double dist = Calculations.calculateTotalDistance(stops);

        if (dist > maxRangeKm) {
            throw new IllegalArgumentException(String.format(
                    ErrorMessages.ROUTE_DISTANCE_EXCEEDS_RANGE,
                    truckId, routeId, dist, maxRangeKm));
        }
    }

}
