package com.company.logistics.repositories.contracts;

import com.company.logistics.enums.City;
import com.company.logistics.models.contracts.Route;

import java.time.LocalDateTime;
import java.util.List;

public interface RouteRepository {

    List<Route> getRoutes();

    Route createRoute(List<City> locations, LocalDateTime departureTime);

    List<Route> findRoutes(City startLocation, City endLocation);

    Route findRouteById(int id);

    Route findRouteByPackageId(int packageId);
}
