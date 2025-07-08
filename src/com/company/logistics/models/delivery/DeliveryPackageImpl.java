package com.company.logistics.models.delivery;

import com.company.logistics.enums.City;
import com.company.logistics.enums.PackageStatus;
import com.company.logistics.models.contracts.DeliveryPackage;
import com.company.logistics.utils.PrintConstants;
import com.company.logistics.utils.ValidationHelper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class DeliveryPackageImpl implements DeliveryPackage {

    private static final int CONTACT_INFO_MIN_LENGTH = 5;
    private static final int CONTACT_INFO_MAX_LENGTH = 57;
    private static final int MAX_WEIGHT = 42000;

    private final int id;
    private final City startLocation;
    private final City endLocation;
    private final double weightKg;
    private final String contactInfo;
    private PackageStatus status;
    private LocalDateTime expectedArrival;

    public DeliveryPackageImpl(int id,
                               City startLocation,
                               City endLocation,
                               double weightKg,
                               String contactInfo) {
        ValidationHelper.validateIntPositive(id, "Package ID");
        ValidationHelper.validateNotNull(startLocation, "StartLocation");
        ValidationHelper.validateNotNull(endLocation,   "EndLocation");
        ValidationHelper.validateDoubleRange(weightKg, 0, MAX_WEIGHT, "Weight(kg)");
        ValidationHelper.validateStringLength(contactInfo, CONTACT_INFO_MIN_LENGTH, CONTACT_INFO_MAX_LENGTH, "ContactInfo");

        this.id            = id;
        this.startLocation = startLocation;
        this.endLocation   = endLocation;
        this.weightKg      = weightKg;
        this.contactInfo   = contactInfo;
        this.status        = PackageStatus.UNASSIGNED;
    }

    @Override public int    getId()             { return id; }
    @Override public City   getStartLocation()  { return startLocation; }
    @Override public City   getEndLocation()    { return endLocation; }
    @Override public double getWeightKg()       { return weightKg; }
    @Override public String getContactInfo()    { return contactInfo; }
    @Override public PackageStatus getStatus()  { return status; }

    @Override
    public void advancePackageStatus() {
        int idx = status.ordinal();
        PackageStatus[] vals = PackageStatus.values();

        if (idx >= vals.length-1) {
            throw new IllegalStateException(String.format(PrintConstants.PACKAGE_ALREADY_AT, id, status));
        }

        status = vals[idx+1];
        System.out.printf(PrintConstants.PACKAGE_STATUS_UPDATE, id, status);
    }

    @Override
    public void revertPackageStatus() {
        int idx = status.ordinal();
        if (idx <= 0) {
            throw new IllegalStateException(String.format(PrintConstants.PACKAGE_ALREADY_AT, id, status));
        }

        status = PackageStatus.values()[idx-1];
        System.out.printf(PrintConstants.PACKAGE_STATUS_UPDATE, id, status);
    }



    @Override public LocalDateTime getExpectedArrival()    { return expectedArrival; }
    @Override public void setExpectedArrival(LocalDateTime eta) {
        this.expectedArrival = eta;
    }

    @Override
    public String print() {
        StringBuilder sb = new StringBuilder();

        sb.append(String.format(PrintConstants.PACKAGE_HEADER, id))
                .append(String.format(PrintConstants.PACKAGE_BASIC_TEMPLATE,
                        startLocation,
                        endLocation,
                        weightKg,
                        contactInfo,
                        status));

        if (status == PackageStatus.IN_TRANSIT && expectedArrival != null) {
            sb.append(String.format(PrintConstants.PACKAGE_ETA_LINE,
                    DateTimeFormatter.ofPattern(PrintConstants.DATE_TIME_FORMAT).format(expectedArrival)));
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
