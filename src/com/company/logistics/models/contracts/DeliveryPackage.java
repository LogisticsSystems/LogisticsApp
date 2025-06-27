package com.company.logistics.models.contracts;

import com.company.logistics.enums.City;
import com.company.logistics.enums.PackageStatus;

import java.time.LocalDateTime;

public interface DeliveryPackage extends Identifyable, Printable {
    City getStartLocation();
    City getEndLocation();
    double getWeightKg();
    String getContactInfo();

    PackageStatus getStatus();
    void advancePackageStatus();
    void revertPackageStatus();

    LocalDateTime getExpectedArrival();
    void setExpectedArrival(LocalDateTime eta);

}
