package com.company.logistics.models.contracts;

public interface Truck extends Identifyable, Printable {
    String getName();

    double getCapacityKg();

    double getMaxRangeKm();

    void assignToRoute();

    boolean isAssignedToRoute();
    void unassignFromToRoute();
}
