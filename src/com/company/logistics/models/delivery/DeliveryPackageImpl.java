package com.company.logistics.models.delivery;

import com.company.logistics.enums.City;
import com.company.logistics.models.contracts.DeliveryPackage;
import com.company.logistics.utils.PrintConstants;
import com.company.logistics.utils.ValidationHelper;

import java.time.LocalDateTime;
import java.util.Objects;

public class DeliveryPackageImpl implements DeliveryPackage {

    private static final int CONTACT_INFO_MIN_LENGTH = 5;
    private static final int CONTACT_INFO_MAX_LENGTH = 57;

    private final int id;
    private final City startLocation;
    private final City endLocation;
    private final double weightKg;
    private final String contactInfo;

    private boolean isAssignedToRoute = false;
    private boolean isAssignedToTruck = false;
    private LocalDateTime expectedArrival;

    public DeliveryPackageImpl(int id,
                               City startLocation,
                               City endLocation,
                               double weightKg,
                               String contactInfo) {
        ValidationHelper.validateIntPositive(id, "Package ID");
        ValidationHelper.validateNotNull(startLocation, "StartLocation");
        ValidationHelper.validateNotNull(endLocation,   "EndLocation");
        ValidationHelper.validateDoubleNonNegative(weightKg, "Weight(kg)");
        ValidationHelper.valideStringLenght(contactInfo, CONTACT_INFO_MIN_LENGTH, CONTACT_INFO_MAX_LENGTH, "ContactInfo");

        this.id            = id;
        this.startLocation = startLocation;
        this.endLocation   = endLocation;
        this.weightKg      = weightKg;
        this.contactInfo   = contactInfo;
    }

    @Override public int    getId()              { return id; }
    @Override public City   getStartLocation()  { return startLocation; }
    @Override public City   getEndLocation()    { return endLocation; }
    @Override public double getWeightKg()       { return weightKg; }
    @Override public String getContactInfo()    { return contactInfo; }

    @Override public boolean isAssignedToRoute() { return isAssignedToRoute; }
    @Override public void    assignToRoute()     { this.isAssignedToRoute = true; }

    @Override public boolean isAssignedToTruck() { return isAssignedToTruck; }
    @Override public void    assignToTruck()     { this.isAssignedToTruck = true; }

    @Override public LocalDateTime getExpectedArrival()    { return expectedArrival; }
    @Override public void setExpectedArrival(LocalDateTime eta) {
        this.expectedArrival = eta;
    }

    @Override
    public String print() {
        StringBuilder sb = new StringBuilder();

        sb.append(String.format(PrintConstants.PACKAGE_HEADER, id))
                .append(String.format(PrintConstants.PACKAGE_BASIC_TEMPLATE, startLocation, endLocation, weightKg, contactInfo));

        if (isAssignedToRoute() && expectedArrival != null) {
            sb.append(String.format(PrintConstants.PACKAGE_ETA_LINE, expectedArrival));
        }

        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DeliveryPackageImpl that = (DeliveryPackageImpl) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
