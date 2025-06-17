package com.company.logistics.models.delivery;

import com.company.logistics.enums.City;
import com.company.logistics.models.contracts.DeliveryPackage;
import com.company.logistics.models.contracts.Route;
import com.company.logistics.models.contracts.Truck;

import java.time.LocalDateTime;
import java.util.List;

public class RouteImpl implements Route {

    public RouteImpl(int id, List<City> locations, LocalDateTime departureTime) {
    }

    @Override
    public int getId() { return 0; }

    @Override
    public LocalDateTime getDepartureTime() {return LocalDateTime.now(); }

    @Override
    public void assignPackage(DeliveryPackage pkg) {

    }

    @Override
    public void assignTruck(Truck truck) {

    }

    @Override
    public List<City> getLocations() {
        return List.of();
    }

}
