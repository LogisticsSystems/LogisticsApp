package com.company.logistics.models.vehicles;

import com.company.logistics.models.contracts.Truck;
import com.company.logistics.utils.PrintConstants;
import com.company.logistics.utils.ValidationHelper;

public class TruckImpl implements Truck {

    private static final int NAME_MIN_LENGTH = 2;
    private static final int NAME_MAX_LENGTH = 10;

    private final int id;
    private final String name;
    private final double capacityKg;
    private final double maxRangeKm;
    private boolean isAssignedToRoute;

    public TruckImpl(int id, String name, double capacityKg, double maxRangeKm) {
        ValidationHelper.validateStringLength(name, NAME_MIN_LENGTH, NAME_MAX_LENGTH, "Truck name");
        ValidationHelper.validateDoubleNonNegative(capacityKg,  "Truck capacity");
        ValidationHelper.validateDoubleNonNegative(maxRangeKm, "Truck max range");

        this.id                = id;
        this.name              = name;
        this.capacityKg        = capacityKg;
        this.maxRangeKm        = maxRangeKm;
        this.isAssignedToRoute = false;
    }

    @Override public int    getId()           { return id; }
    @Override public String getName()         { return name; }
    @Override public double getCapacityKg()   { return capacityKg; }
    @Override public double getMaxRangeKm()   { return maxRangeKm; }
    @Override public boolean isAssignedToRoute() { return isAssignedToRoute; }

    @Override
    public void unassignFromRoute() { isAssignedToRoute = false; }

    @Override
    public void assignToRoute() {
        isAssignedToRoute = true;
    }

    @Override
    public String print() {
        StringBuilder sb = new StringBuilder();

        sb.append(String.format(PrintConstants.TRUCK_HEADER, id))
                .append(String.format(PrintConstants.TRUCK_NAME_LINE, name))
                .append(String.format(PrintConstants.TRUCK_CAPACITY_LINE, capacityKg))
                .append(String.format(PrintConstants.TRUCK_RANGE_LINE, maxRangeKm))
                .append(isAssignedToRoute ? String.format(PrintConstants.TRUCK_STATUS_ASSIGNED) : String.format(PrintConstants.TRUCK_STATUS_UNASSIGNED));

        return sb.toString();
    }
}
