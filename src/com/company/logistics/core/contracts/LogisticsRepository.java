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

    List<Route> findRoutes(City startLocation, City endLocation);

    DeliveryPackage findPackageById(int id);

    Route findRouteById(int id);

    Truck findTruckById(int id);

    Route findRouteByPackageId(int packageId);

}
