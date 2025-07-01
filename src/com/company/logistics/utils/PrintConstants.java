package com.company.logistics.utils;

public final class PrintConstants {
    private PrintConstants() { }

    // Route printing
    public static final String ROUTE_HEADER        = "Route %d:%n";
    public static final String ROUTE_STOP_TEMPLATE = " %s (%s)";
    public static final String ROUTE_TRUCK_LINE    = " Assigned Truck ID: %s%n";
    public static final String ROUTE_PACKAGES_LINE = " Assigned Package IDs: %s%n";
    public static final String NO_ROUTE_CARRIES_PACKAGES_LINE = "No route carries package %d";

    // Package printing
    public static final String PACKAGE_HEADER          = "Package %d:%n";
    public static final String PACKAGE_ROUTE_LINE      = "  Assigned to Route: %s%n";
    public static final String PACKAGE_ETA_LINE        = "  Expected Arrival: %s%n";
    public static final String PACKAGE_BASIC_TEMPLATE  = "  From %s → %s, %,.2fkg, Contact: %s%n  Package status: %s%n";
    public static final String PACKAGE_ALREADY_AT      = "Package %d is already %s.";
    public static final String PACKAGE_STATUS_UPDATE   = "Package %d is now in status %s.%n";

    // Truck printing
    public static final String TRUCK_HEADER               = "Truck %d:%n";
    public static final String TRUCK_NAME_LINE            = "  Model: %s%n";
    public static final String TRUCK_CAPACITY_LINE        = "  Capacity: %.2f kg%n";
    public static final String TRUCK_RANGE_LINE           = "  Max Range: %.2f km%n";
    public static final String TRUCK_STATUS_ASSIGNED      = "  Status: Assigned to a route%n";
    public static final String TRUCK_STATUS_UNASSIGNED    = "  Status: Unassigned%n";

    //Others
    public static final String LINE_BREAK                 = "==========";
    public static final String DATE_TIME_FORMAT           = "dd-MMM-yyyy h:mm a";

}
