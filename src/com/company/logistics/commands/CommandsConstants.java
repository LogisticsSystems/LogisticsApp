package com.company.logistics.commands;

public class CommandsConstants {
    public static final String PACKAGE_CREATED_MESSAGE = "Package was created with ID %d.";
    public static final String INVALID_CITY_MESSAGE = "City %s does not exist.";
    public static final String ASSIGNED_PACKAGE_TO_ROUTE = "%s %d was assigned to route %d. New status: %s";
    public static final String NEW_TRUCK_FOUND = "The current truck could not fit the package!\nA larger truck %d " +
            "was assigned to route %d, and package: %d was successfully assigned.";
    public static final String NEW_ROUTE_CREATED = "The package %d exceeds the maximum truck capacity.\n" +
            "A new route %d was created and the package was assigned to it.";
    public static final String COMPATIBLE_ROUTE_FOUND = "Due to exceeded load capacity,\n package %d was assigned to " +
            "a compatible Route %d with sufficient load allowance.";
    public static final String EMPTY_MESSAGE = "";
    public static final String ASSIGNED_TRUCK_ROUTE      = "%s %d was assigned to route %d";
    public static final String NO_MATCHING_ROUTES_MESSAGE = "No routes found between %s and %s.";
    public static final String NONE_FOUND_MESSAGE = "No %s found.";
    public static final String SPEED_MODEL_SWITCH  = "Speed model switched to %s";
    public static final String REMOVED_FROM_ROUTE = "%s %d was removed from route %d. New status: %s";
    public static final String REMOVED_TRUCK_FROM_ROUTE = "%s %d was removed from route %d.";
    public static final String INVALID_STATUS_MESSEGE = "Status %s does not exist.";
    public static final String NO_PACKAGES_WITH_STATUS = "No packages with status %s found.";
    public static final String NO_ROUTES_WITHOUT_TRUCKS = "No routes without trucks found.";
}
