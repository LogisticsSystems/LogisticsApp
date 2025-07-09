package com.company.logistics.services.assignment.strategy.implementation;

import com.company.logistics.enums.PackageStatus;
import com.company.logistics.models.contracts.DeliveryPackage;
import com.company.logistics.models.contracts.Route;
import com.company.logistics.models.contracts.Truck;
import com.company.logistics.repositories.contracts.RouteRepository;
import com.company.logistics.repositories.contracts.TruckRepository;
import com.company.logistics.services.assignment.strategy.TruckRemovalStrategy;
import com.company.logistics.utils.ValidationHelper;

public class DefaultTruckRemovalStrategy implements TruckRemovalStrategy {

    private final RouteRepository routeRepository;
    private final TruckRepository truckRepository;

    public DefaultTruckRemovalStrategy(RouteRepository routeRepository,
                                       TruckRepository truckRepository) {
        this.routeRepository = routeRepository;
        this.truckRepository = truckRepository;
    }

    @Override
    public void removeTruck(int truckId, int routeId) {
        Truck truck = truckRepository.findTruckById(truckId);
        Route route = routeRepository.findRouteById(routeId);

        ValidationHelper.validateTruckAssignedToRoute(truck,route);

        truck.unassignFromRoute();
        route.unassignTruck();

        changePackagesStatus(route);
    }

    private void changePackagesStatus(Route route) {
        route.getAssignedPackages().stream()
                .filter(pkg->pkg.getStatus() == PackageStatus.IN_TRANSIT)
                .forEach(DeliveryPackage::revertPackageStatus);
    }
}
