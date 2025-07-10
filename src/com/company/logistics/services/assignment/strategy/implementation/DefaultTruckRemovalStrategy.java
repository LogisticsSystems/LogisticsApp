package com.company.logistics.services.assignment.strategy.implementation;

import com.company.logistics.enums.PackageStatus;
import com.company.logistics.models.contracts.DeliveryPackage;
import com.company.logistics.models.contracts.Route;
import com.company.logistics.models.contracts.Truck;
import com.company.logistics.repositories.contracts.RouteRepository;
import com.company.logistics.repositories.contracts.TruckRepository;
import com.company.logistics.services.assignment.strategy.TruckRemovalStrategy;
import com.company.logistics.utils.ValidationHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DefaultTruckRemovalStrategy implements TruckRemovalStrategy {

    private final RouteRepository routeRepository;
    private final TruckRepository truckRepository;

    public DefaultTruckRemovalStrategy(RouteRepository routeRepository,
                                       TruckRepository truckRepository) {
        this.routeRepository = routeRepository;
        this.truckRepository = truckRepository;
    }

    @Override
    public List<Integer> removeTruck(int truckId, int routeId) {
        Truck truck = truckRepository.findTruckById(truckId);
        Route route = routeRepository.findRouteById(routeId);

        ValidationHelper.validateTruckAssignedToRoute(truck, route);

        truck.unassignFromRoute();
        route.unassignTruck();

        List<Integer> changedIds = new ArrayList<>();
        for (DeliveryPackage pkg : route.getAssignedPackages()) {
            if (pkg.getStatus() == PackageStatus.IN_TRANSIT) {
                pkg.revertPackageStatus();
                changedIds.add(pkg.getId());
            }
        }
        return Collections.unmodifiableList(changedIds);
    }
}
