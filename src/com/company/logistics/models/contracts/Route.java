package com.company.logistics.models.contracts;

import com.company.logistics.enums.City;

import java.time.LocalDateTime;
import java.util.List;

public interface Route extends Identifyable, Printable{
    LocalDateTime getDepartureTime();

    List<City> getLocations();

    List<LocalDateTime> getSchedule();

    void setSchedule(List<LocalDateTime> schedule);

    void assignPackage(DeliveryPackage pkg);

    void assignTruck(Truck truck);

    List<DeliveryPackage> getAssignedPackages();

    List<Truck> getAssignedTrucks();

    void removePackage(int packageId);
    void removeTrucks();
}


