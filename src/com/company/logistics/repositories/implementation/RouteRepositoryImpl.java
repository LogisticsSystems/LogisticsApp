package com.company.logistics.repositories.implementation;

import com.company.logistics.enums.City;
import com.company.logistics.exceptions.InvalidUserInputException;
import com.company.logistics.models.contracts.DeliveryPackage;
import com.company.logistics.models.contracts.Route;
import com.company.logistics.models.delivery.RouteImpl;
import com.company.logistics.repositories.contracts.RouteRepository;
import com.company.logistics.utils.ErrorMessages;
import com.company.logistics.utils.PrintConstants;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class RouteRepositoryImpl implements RouteRepository {
    private final AtomicInteger nextId = new AtomicInteger(1);
    private final Map<Integer, Route> routes = new HashMap<>();

    @Override
    public List<Route> getRoutes() {
        return routes.values().stream()
                .sorted(Comparator.comparing(Route::getDepartureTime))
                .collect(Collectors.toList());
    }

    @Override
    public Route createRoute(List<City> locations, LocalDateTime departureTime) {
        Route route = new RouteImpl(nextId.get(), locations, departureTime);
        routes.put(nextId.get(), route);
        nextId.getAndIncrement();
        return route;
    }

    @Override
    public List<Route> findRoutes(City startLocation, City endLocation) {
        return routes.values().stream()
                .filter(r -> {
                    List<City> locs = r.getLocations();
                    int from = locs.indexOf(startLocation);
                    int to   = locs.indexOf(endLocation);
                    return from >= 0 && to > from;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Route findRouteById(int id) {
        Route route = routes.get(id);
        if (route == null) {
            throw new InvalidUserInputException(String.format(ErrorMessages.NO_ROUTE_WITH_ID, id));
        }
        return route;
    }

    @Override
    public Route findRouteByPackageId(int packageId) {
        for (Route route : routes.values()) {
            for (DeliveryPackage pkg : route.getAssignedPackages()) {
                if (pkg.getId() == packageId) {
                    return route;
                }
            }
        }
        throw new InvalidUserInputException(
                String.format(PrintConstants.NO_ROUTE_CARRIES_PACKAGES_LINE, packageId)
        );
    }
}
