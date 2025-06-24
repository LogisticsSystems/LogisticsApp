package com.company.logistics.commands;

public class CommandsConstants {
    public static final String PACKAGE_CREATED_MESSAGE = "Package was created with ID %d.";
    public static final String INVALID_CITY_MESSAGE = "City %s does not exist.";
    public static final String ASSIGNED_TO_ROUTE_MESSAGE = "%s %d was assigned to route %d.";
    public static final String NO_MATCHING_ROUTES_MESSAGE = "No routes found between %s and %s.";
    public static final String NONE_FOUND_MESSAGE = "No %s found.";
    public static final String SPEED_MODEL_SWITCH  = "Speed model switched to %s";
    public static final String ALTERNATIVE_TRUCK_ASSIGNED= "Requested truck %d exceeded capacity;\n" +
            "Аssigned alternative truck %d to route %d.";
    public static final String NEW_ROUTE_AND_TRUCKS_ASSIGNED = "The selected truck (ID: %d) could not carry all assigned packages.\n" +
            "A new route (ID: %d) was created for the overflow.\n" +
            "Truck %d was assigned to the original route, and truck %d to the new one.\n";
}
