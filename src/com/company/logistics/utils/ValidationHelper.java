package com.company.logistics.utils;
import com.company.logistics.enums.City;
import com.company.logistics.enums.PackageStatus;
import com.company.logistics.exceptions.InvalidUserInputException;
import com.company.logistics.infrastructure.DistanceMap;
import com.company.logistics.models.contracts.DeliveryPackage;
import com.company.logistics.models.contracts.Route;
import com.company.logistics.models.contracts.Truck;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class ValidationHelper {
    private ValidationHelper() { }

    // --- Initialization -------------------------------------------------

    public static void validateNotAlreadyInitialized(Object instance, String name) {
        if (instance != null) {
            throw new InvalidUserInputException(String.format(
                    ErrorMessages.ALREADY_INITIALIZED, name));
        }
    }

    public static void validateInitialized(Object instance, String name) {
        if (instance == null) {
            throw new InvalidUserInputException(String.format(
                    ErrorMessages.NOT_INITIALIZED, name));
        }
    }

    // --- Null / Basic checks -------------------------------------------------

    public static void validateNotNull(Object obj, String paramName) {
        if (obj == null) {
            throw new InvalidUserInputException(String.format(
                    ErrorMessages.NOT_NULL, paramName));
        }
    }

    public static void validateArgumentsCount(List<String> params, int expected) {
        if (params.size() < expected) {
            throw new InvalidUserInputException(String.format(
                    ErrorMessages.INVALID_ARGUMENTS_COUNT, expected, params.size()));
        }
    }

    // --- Primitive validations -------------------------------------------------

    public static void validateIntPositive(int value, String name) {
        if (value <= 0) {
            throw new InvalidUserInputException(String.format(
                    ErrorMessages.NON_POSITIVE_INT, name));
        }
    }

    public static void validateDoubleNonNegative(double value, String name) {
        if (value < 0) {
            throw new InvalidUserInputException(String.format(
                    ErrorMessages.NEGATIVE_DOUBLE, name));
        }
    }

    public static void validateStringLength(String s, int min, int max, String name) {
        validateIntRange(s.length(), min, max, name);
    }

    private static void validateIntRange(int actual, int min, int max, String name) {
        if (actual < min || actual > max) {
            throw new InvalidUserInputException(String.format(
                    ErrorMessages.NUMBER_NOT_IN_RANGE, name, min, max));
        }
    }

    public static void validateDoubleRange(double actual, int min, int max, String name) {
        if (actual < min || actual > max) {
            throw new InvalidUserInputException(String.format(
                    ErrorMessages.NUMBER_NOT_IN_RANGE, name, min, max));
        }
    }

    // --- Collection size validations -------------------------------------------------

    public static <T> void validateListSizeAtLeast(List<T> list, String name, int min) {
        validateNotNull(list, name);
        if (list.size() < min) {
            throw new InvalidUserInputException(String.format(
                    ErrorMessages.MIN_LIST_SIZE, name, min));
        }
    }

    public static <T> void validateListSizeEquals(List<T> list, String name, int expected) {
        validateNotNull(list, name);
        if (list.size() != expected) {
            throw new InvalidUserInputException(String.format(
                    ErrorMessages.EXACT_LIST_SIZE, name, expected));
        }
    }

    public static <T> void validateListSizeAtMost(
            List<T> list, String name, int max, String errorMsgFmt) {
        validateNotNull(list, name);
        if (list.size() > max) {
            throw new InvalidUserInputException(String.format(errorMsgFmt,
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
            throw new InvalidUserInputException(String.format(
                    unknownFromMsg, from));
        }

        Map<K, V> inner = map.get(from);
        if (!inner.containsKey(to)) {
            throw new InvalidUserInputException(String.format(
                    noValueMsg, from, to));
        }
    }

    // --- Domain‐specific validations -------------------------------------------------

    public static void validatePackageInRoute(DeliveryPackage deliveryPackage, Route route){
        if(route.getAssignedPackages().stream()
                .noneMatch(pkg->pkg.equals(deliveryPackage))) {
            throw new IllegalArgumentException(String.format(ErrorMessages.PACKAGE_NOT_OT_ROUTE,
                    deliveryPackage.getId(),
                    route.getId()));
        }
    }

    public static void validateTruckAssignedToRoute(Truck truck, Route route){
        if(route.getAssignedTruck().isEmpty() || !route.getAssignedTruck().get().equals(truck)){
            throw new IllegalArgumentException(String.format(ErrorMessages.TRUCK_NOT_ON_ROUTE,
                    truck.getId(),
                    route.getId()));
        }
    }

    public static void validatePackageRouteCompatibility(
            City start, City end, List<City> stops
    ) {
        validateNotNull(start, "startLocation");
        validateNotNull(end,   "endLocation");

        int idxFrom = stops.indexOf(start);
        if (idxFrom < 0) {
            throw new InvalidUserInputException(String.format(ErrorMessages.CITY_NOT_ON_ROUTE, start));
        }

        int idxTo = stops.lastIndexOf(end);
        if (idxTo < 0) {
            throw new InvalidUserInputException(String.format(ErrorMessages.CITY_NOT_ON_ROUTE, end));
        }

        if (idxTo <= idxFrom) {
            throw new InvalidUserInputException(String.format(ErrorMessages.PACKAGE_ROUTE_MISMATCH, start, end));
        }
    }

    public static void validatePackageStatus(
            DeliveryPackage pkg, PackageStatus expected
    ) {
        if (pkg.getStatus() != expected) {
            throw new InvalidUserInputException(String.format(
                    ErrorMessages.PACKAGE_STATUS_ERROR, expected));
        }
    }

    public static void validateTotalLoadWithinCapacity(
            List<DeliveryPackage> packages,
            double capacityKg, String error
    ) {
        double load = Calculations.calculateTotalLoad(packages);

        if (load > capacityKg) {
            throw new InvalidUserInputException(error);
        }
    }

    public static void validateRouteRangeWithin(
            List<City> stops, double maxRangeKm, int truckId, int routeId
    ) {
        double dist = Calculations.calculateTotalDistance(stops);

        if (dist > maxRangeKm) {
            throw new InvalidUserInputException(String.format(
                    ErrorMessages.ROUTE_DISTANCE_EXCEEDS_RANGE,
                    truckId, routeId, dist, maxRangeKm));
        }
    }

    public static void validateNotSameCities(City startLocation, City endLocation) {
        if (startLocation == endLocation) {
            throw new InvalidUserInputException("Start/End cities cannot be the same");
        }
    }

    public static void validateUniqueIntermediateStops(List<City> locations) {
        Set<City> seen = new HashSet<>();
        City first = locations.get(0);
        int lastIdx = locations.size() - 1;

        for (int i = 0; i < locations.size(); i++) {
            City city = locations.get(i);

            // first and last stops can be the same
            if (i == lastIdx && city.equals(first)) { continue; }

            // every other city must be unique
            if (!seen.add(city)) {
                throw new InvalidUserInputException(String.format(ErrorMessages.NON_UNIQUE_INTERMEDIATE_STOPS, city));
            }
        }
    }

}
