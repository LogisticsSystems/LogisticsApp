package com.company.logistics.core.contracts;

import com.company.logistics.enums.City;
import com.company.logistics.models.contracts.DeliveryPackage;
import com.company.logistics.models.contracts.Route;
import com.company.logistics.models.contracts.Truck;

import java.time.LocalDateTime;
import java.util.List;

public interface LogisticsRepository {
    List<DeliveryPackage> getPackages();

    List<Truck> getTrucks();

    List<Route> getRoutes();

    DeliveryPackage createPackage(String contactInfo, double weight, City startLocation, City endLocation);

    Route createRoute(List<City> locations, LocalDateTime departureTime);

    void assignPackageToRoute(int packageId, int routeId);

    void assignTruckToRoute(int truckId, int routeId);

    List<Route> findRoutes(City startLocation, City endLocation);
}
