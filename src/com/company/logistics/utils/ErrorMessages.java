package com.company.logistics.utils;

public final class ErrorMessages {
    private ErrorMessages() { }

    public static final String ALREADY_INITIALIZED    = "%s has already been initialized";
    public static final String NOT_INITIALIZED        = "%s is not initialized";
    public static final String NOT_NULL               = "%s must not be null";
    public static final String UNKNOWN_FROM_CITY      = "Unknown from city: %s";
    public static final String NO_DISTANCE_DEFINED    = "No distance defined between %s and %s";
    public static final String NO_PACKAGE_WITH_ID = "No package with ID: %d";
    public static final String NO_ROUTE_WITH_ID   = "No route with ID: %d";
    public static final String NO_TRUCK_WITH_ID   = "No truck with ID: %d";
}
