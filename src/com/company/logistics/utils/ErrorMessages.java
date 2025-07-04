package com.company.logistics.utils;

public final class ErrorMessages {
    private ErrorMessages() { }

    // --- Initialization -------------------------------------------------
    public static final String ALREADY_INITIALIZED       = "%s has already been initialized";
    public static final String NOT_INITIALIZED           = "%s is not initialized";

    // --- Null / General validation -------------------------------------------------
    public static final String NOT_NULL                  = "%s must not be null";
    public static final String NEGATIVE_DOUBLE           = "%s must be non negative";
    public static final String NON_POSITIVE_INT          = "%s must be positive";
    public static final String STRING_NOT_IN_RANGE       = "%s must be between %d and %d characters";
    public static final String NUMBER_NOT_IN_RANGE       = "%s must be between %d and %d.";
    public static final String MIN_LIST_SIZE             = "%s must contain at least %d elements";
    public static final String EXACT_LIST_SIZE           = "%s must contain exactly %d elements";

    // --- Parsing / Argument errors -------------------------------------------------
    public static final String INVALID_ARGUMENTS_COUNT   = "Invalid arguments count. Expected: %d, Given: %d.";
    public static final String INCORRECT_DATA_INPUT      = "%s must be a %s";
    public static final String INCORRECT_DATE_TIME_INPUT = "Invalid date/time format";
    public static final String INVALID_ENUM_VALUE        = "%s must be one of %s";
    public static final String PACKAGE_ROUTE_MISMATCH    = "Package start city %s must come before end city %s on the route";
    public static final String PACKAGE_STATUS_ERROR    = "Package should be in status %s for this command.";

    // --- Map lookups -------------------------------------------------
    public static final String UNKNOWN_FROM_CITY         = "Unknown from city: %s";
    public static final String NO_DISTANCE_DEFINED       = "No distance defined between %s and %s";
    public static final String NO_SPEED_DEFINED          = "No base speed defined between %s and %s";

    // --- Entity not found -------------------------------------------------
    public static final String NO_PACKAGE_WITH_ID        = "No package with ID: %d";
    public static final String NO_ROUTE_WITH_ID          = "No route with ID: %d";
    public static final String NO_TRUCK_WITH_ID          = "No truck with ID: %d";
    public static final String UNKNOWN_SPEED_MODEL       = "Unknown speed model: %s";

    // --- Assignment -------------------------------------------------
    public static final String ALREADY_ASSIGNED          = "%s is already assigned to %s.";
    public static final String CITY_NOT_ON_ROUTE         = "City %s not on route";

    // --- Speed model max routes -------------------------------------------------
    public static final String SPEED_MODEL_MAX_ROUTES_EXCEED = "Cannot change speed model with %d routes; please limit to %d or fewer.";

    // --- Truck/Route capacity/range guards ----------------------------
    public static final String TRUCK_LOAD_EXCEEDS_CAPACITY  = "Cannot assign Truck %d to Route %d: load %.0fkg exceeds capacity %.0fkg. %nPlease assign a bigger truck if available, or create a new route.";
    public static final String ROUTE_LOAD_EXCEEDS_CAPACITY  = "Cannot assign Package %d to Route %d: load %.0fkg exceeds capacity %.0fkg of the currently assigned %s truck. %nPlease assign a bigger truck if available, or create a new route.";
    public static final String ROUTE_DISTANCE_EXCEEDS_RANGE = "Cannot assign Truck %d to Route %d: route distance %.0fkm exceeds range %.0fkm.";
}
