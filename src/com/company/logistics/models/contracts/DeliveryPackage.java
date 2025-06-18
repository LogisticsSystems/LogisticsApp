package com.company.logistics.models.contracts;

import com.company.logistics.enums.City;

import java.time.LocalDateTime;

public interface DeliveryPackage extends Identifyable, Printable {
    City getStartLocation();
    City getEndLocation();
    double getWeightKg();
    String getContactInfo();

    boolean isAssignedToRoute();
    void assignToRoute();

    boolean isAssignedToTruck();
    void assignToTruck();

    LocalDateTime getExpectedArrival();
    void setExpectedArrival(LocalDateTime eta);
}
