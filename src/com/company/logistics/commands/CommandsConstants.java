package com.company.logistics.commands;

public class CommandsConstants {
    // Package
    public static final String PACKAGE_CREATED_MESSAGE = "Package was created with ID %d.";
    public static final String ASSIGNED_PACKAGE_TO_ROUTE = "%s %d was assigned to route %d. New status: %s";
    public static final String NO_PACKAGES_WITH_STATUS = "No packages with status %s found.";
    public static final String PACKAGE_DELIVERED_MESSAGE = "Package %d has been delivered. Status: %s";
    public static final String NEW_TRUCK_FOUND = "The current truck could not fit the package!\nA larger truck %d " +
            "was assigned to route %d, and package: %d was successfully assigned.";
    public static final String NEW_ROUTE_CREATED = "The package %d exceeds the maximum truck capacity.\n" +
            "A new route %d was created and the package was assigned to it.";
    public static final String COMPATIBLE_ROUTE_FOUND = "Due to exceeded load capacity,\n package %d was assigned to " +
            "a compatible Route %d with sufficient load allowance.";
    public static final String EMPTY_MESSAGE = "";

    // Truck
    public static final String ASSIGNED_TRUCK_ROUTE      = "%s %d was assigned to route %d";
    public static final String REMOVED_TRUCK_FROM_ROUTE = "%s %d was removed from route %d.";

    // Route
    public static final String ROUTE_CREATED_MESSAGE = "Route was created with ID %d.";
    public static final String NO_MATCHING_ROUTES_MESSAGE = "No routes found between %s and %s.";
    public static final String REMOVED_FROM_ROUTE = "%s %d was removed from route %d. New status: %s";
    public static final String NO_ROUTES_WITHOUT_TRUCKS = "No routes without trucks found.";

    // User
    public static final String USER_CREATED_MESSAGE = "User %s was created.";
    public static final String USER_REMOVED_MESSAGE = "User %s was deleted.";
    public static final String NO_USERS_WITH_ROLE = "No users with role %s found.";
    public static final String COMMAND_UNAVAILABLE_FOR_USER = "This command is not available for user role %s.";
    public static final String COMMAND_UNAVAILABLE_FOR_NO_USER = "User must be logged in to execute this command.";
    public static final String CURRENTLY_LOGGED_IN = "You cannot remove user %s, because they are currently logged in.";

    // Log in adn out
    public final static String USER_LOGGED_IN = "User %s successfully logged in!";
    public final static String USER_LOGGED_IN_ALREADY = "User %s is logged in! Please log out first!";
    public final static String USER_LOGGED_OUT = "User %s successfully logged out!";
    public final static String NO_LOGGED_IN_USER = "No user logged in.";

    // General
    public static final String NONE_FOUND_MESSAGE = "No %s found.";

    //Speed Model
    public static final String SPEED_MODEL_SWITCH  = "Speed model switched to %s";
}
