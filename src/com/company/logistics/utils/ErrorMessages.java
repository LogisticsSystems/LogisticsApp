package com.company.logistics.utils;

public final class ErrorMessages {
    private ErrorMessages() { }

    // --- Initialization -------------------------------------------------
    public static final String ALREADY_INITIALIZED       = "%s has already been initialized";
    public static final String NOT_INITIALIZED           = "%s is not initialized";

    // --- Null / General validation -------------------------------------------------
    public static final String NOT_NULL                  = "%s must not be null";
    public static final String NEGATIVE_DOUBLE           = "%s must be non negative";
    public static final String NON_POSITIVE_INT          = "%s must be non negative";
    public static final String NUMBER_NOT_IN_RANGE       = "%s must be between %d and %d.";
    public static final String MIN_LIST_SIZE             = "%s must contain at least %d elements";
    public static final String EXACT_LIST_SIZE           = "%s must contain exactly %d elements";
    public static final String SYMBOLS_PATTERN_ERR       = "%s contains invalid symbols!";
    public static final String START_END_NOT_SAME_ERROR  = "Start and End cities cannot be the same.";

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
    public static final String UNKNOWN_SPEED_MODEL       = "Unknown speed model: %s. Current available models are CONSTANT, SEASONAL, SINUSOIDAL";

    // --- Assignment -------------------------------------------------
    public static final String ALREADY_ASSIGNED          = "%s is already assigned to %s.";
    public static final String CITY_NOT_ON_ROUTE         = "City %s not on route";
    public static final String PACKAGE_NOT_OT_ROUTE      = "Package with ID: %d is not on route %d.";
    public static final String TRUCK_NOT_ON_ROUTE        = "Truck wiht ID: %d is not on route %d.";

    // --- Speed model max routes -------------------------------------------------
    public static final String SPEED_MODEL_MAX_ROUTES_EXCEED = "Cannot change speed model with %d routes; please limit to %d or fewer.";

    // --- Truck/Route capacity/range guards ----------------------------
    public static final String TRUCK_ALREADY_ASSIGNED           = "Route already has truck %d assigned to it";
    public static final String TRUCK_LOAD_EXCEEDS_CAPACITY      = "Cannot assign Truck %d to Route %d: load %.0fkg exceeds capacity %.0fkg. %nPlease assign a bigger truck if available, or create a new route.";
    public static final String ROUTE_LOAD_EXCEEDS_CAPACITY      = "Cannot assign Package %d to Route %d: load %.0fkg exceeds capacity %.0fkg of the currently assigned %s truck. %nPlease assign a bigger truck if available, or create a new route.";
    public static final String ROUTE_MAX_LOAD_EXCEEDS_CAPACITY  = "Cannot assign Package %d to Route %d: load %.0fkg exceeds maximum capacity of %.0fkg.%nPlease create a new route.";
    public static final String ROUTE_DISTANCE_EXCEEDS_RANGE     = "Cannot assign Truck %d to Route %d: route distance %.0fkm exceeds range %.0fkm.";
    public static final String NON_UNIQUE_INTERMEDIATE_STOPS    = "Route may not visit a city twice, unless it is the start and end point: %s";

    // --- User login/logout ----------------------------
    public static final String NO_LOGGED_IN_USER = "There is no logged in user.";
    public final static String NO_SUCH_USER = "There is no user with username %s!";
    public final static String INCORRECT_PASSWORD = "The password was incorrect, please try again!";
    public final static String USER_ALREADY_EXIST = "User %s already exist. Choose a different username!";

    // --- File errors ----------------------------
    public static final String INFORMATION_CURRENTLY_UNAVAILABLE = "Information currently unavailable.";

}
