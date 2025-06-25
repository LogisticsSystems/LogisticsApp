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
        ValidationHelper.validateDoubleNonNegative(weightKg, "Weight(kg)");
        ValidationHelper.valideStringLenght(contactInfo, CONTACT_INFO_MIN_LENGTH, CONTACT_INFO_MAX_LENGTH, "ContactInfo");

        this.id            = id;
        this.startLocation = startLocation;
        this.endLocation   = endLocation;
        this.weightKg      = weightKg;
        this.contactInfo   = contactInfo;
        this.status        = PackageStatus.UNASSIGNED;
    }

    @Override public int    getId()              { return id; }
    @Override public City   getStartLocation()  { return startLocation; }
    @Override public City   getEndLocation()    { return endLocation; }
    @Override public double getWeightKg()       { return weightKg; }
    @Override public String getContactInfo()    { return contactInfo; }
    @Override public PackageStatus getStatus()    { return status; }

    @Override public LocalDateTime getExpectedArrival()    { return expectedArrival; }
    @Override public void setExpectedArrival(LocalDateTime eta) {
        this.expectedArrival = eta;
    }


    //TODO ordinal
    @Override
    public void advancePackageStatus() {
        PackageStatus nextStatus;
        PackageStatus currentStatus = getStatus();

        PackageStatus[] statuses = PackageStatus.values();


        switch (this.status) {
            case PackageStatus.UNASSIGNED:
                nextStatus = PackageStatus.PENDING;
                break;
            case PackageStatus.PENDING:
                nextStatus = PackageStatus.IN_TRANSIT;
                break;
            case PackageStatus.IN_TRANSIT:
                nextStatus = PackageStatus.DELIVERED;
                break;
            default:
                throw new IllegalArgumentException(String.format(PrintConstants.PACKAGE_ALREADY_DELIVERED,
                        this.id,
                        this.status));
        }

        this.status = nextStatus;
        System.out.printf(PrintConstants.PACKAGE_STATUS_UPDATE,
                this.id,
                this.status);
    }

    @Override
    public void revertPackageStatus() {
        PackageStatus nextStatus;

        switch (this.status) {
            case PackageStatus.PENDING:
                nextStatus = PackageStatus.UNASSIGNED;
                break;
            case PackageStatus.IN_TRANSIT:
                nextStatus = PackageStatus.PENDING;
                break;
            case PackageStatus.DELIVERED:
                nextStatus = PackageStatus.IN_TRANSIT;
                break;
            default:
                throw new IllegalArgumentException(String.format(PrintConstants.PACKAGE_ALREADY_DELIVERED,
                        this.id,
                        this.status));
        }

        this.status = nextStatus;
        System.out.printf(PrintConstants.PACKAGE_STATUS_UPDATE,
                this.id,
                this.status);
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
