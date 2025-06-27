package com.company.logistics.models.contracts;

import com.company.logistics.enums.City;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface Route extends Identifyable, Printable {
    LocalDateTime getDepartureTime();

    List<City> getLocations();

    List<LocalDateTime> getSchedule();

    void setSchedule(List<LocalDateTime> schedule);

    void assignPackage(DeliveryPackage pkg);

    void assignTruck(Truck truck);

    List<DeliveryPackage> getAssignedPackages();

    Optional<Truck> getAssignedTruck();

    void removePackage(int packageId);

    void unassignTruck();
}


