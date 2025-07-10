package com.company.logistics.commands;

public class CommandsConstants {
    public static final String PACKAGE_CREATED_MESSAGE = "Package was created with ID %d.";
    public static final String USER_CREATED_MESSAGE = "User %s was created.";
    public static final String USER_REMOVED_MESSAGE = "User %s was deleted.";
    public static final String INVALID_CITY_MESSAGE = "City %s does not exist.";
    public static final String INVALID_ROLE_MESSAGE = "Role %s does not exist.";
    public static final String ASSIGNED_PACKAGE_TO_ROUTE = "%s %d was assigned to route %d. New status: %s";
    public static final String ASSIGNED_TRUCK_ROUTE      = "%s %d was assigned to route %d";
    public static final String NO_MATCHING_ROUTES_MESSAGE = "No routes found between %s and %s.";
    public static final String NONE_FOUND_MESSAGE = "No %s found.";
    public static final String SPEED_MODEL_SWITCH  = "Speed model switched to %s";
    public static final String REMOVED_FROM_ROUTE = "%s %d was removed from route %d. New status: %s";
    public static final String REMOVED_TRUCK_FROM_ROUTE = "%s %d was removed from route %d.";
    public static final String INVALID_STATUS_MESSEGE = "Status %s does not exist.";
    public static final String NO_PACKAGES_WITH_STATUS = "No packages with status %s found.";
    public static final String NO_USERS_WITH_ROLE = "No users with role %s found.";
    public static final String NO_ROUTES_WITHOUT_TRUCKS = "No routes without trucks found.";
    public static final String COMMAND_UNAVAILABLE_FOR_USER = "This command is not available for user role %s.";
    public static final String COMMAND_UNAVAILABLE_FOR_NO_USER = "User must be logged in to execute this command.";
}
