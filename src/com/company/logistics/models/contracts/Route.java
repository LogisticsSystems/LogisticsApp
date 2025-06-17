package com.company.logistics.models.contracts;

import com.company.logistics.enums.City;

import java.time.LocalDateTime;
import java.util.List;

public interface Route extends Identifyable, Printable{

    //TODO
    LocalDateTime getDepartureTime();
    void assignPackage(DeliveryPackage pkg);
    void assignTruck(Truck truck);
    List<City> getLocations();
}


