package com.company.logistics.utils;

public final class PrintConstants {
    private PrintConstants() { }

    // Route printing
    public static final String ROUTE_HEADER        = "Route %d:%n";
    public static final String ROUTE_STOP_TEMPLATE = " %d. %s at %s%n";
    public static final String ROUTE_TRUCK_LINE    = " Assigned Truck IDs: %s%n";
    public static final String ROUTE_PACKAGES_LINE = " Assigned Package IDs: %s%n";

    // Package printing
    public static final String PACKAGE_HEADER          = "Package %d:%n";
    public static final String PACKAGE_ROUTE_LINE      = "  Assigned to Route: %s%n";
    public static final String PACKAGE_ETA_LINE        = "  Expected Arrival: %s%n";
    public static final String PACKAGE_BASIC_TEMPLATE  = "  From %s → %s, %,.2fkg, Contact: %s%n";

    // Truck printing
    public static final String TRUCK_HEADER               = "Truck %d:%n";
    public static final String TRUCK_NAME_LINE            = "  Model: %s%n";
    public static final String TRUCK_CAPACITY_LINE        = "  Capacity: %.2f kg%n";
    public static final String TRUCK_RANGE_LINE           = "  Max Range: %.2f km%n";
    public static final String TRUCK_STATUS_ASSIGNED      = "  Status: Assigned to a route%n";
    public static final String TRUCK_STATUS_UNASSIGNED    = "  Status: Unassigned%n";
}
