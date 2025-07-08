package com.company.logistics.services.assignment.strategy.implementation;

import com.company.logistics.core.contracts.LogisticsRepository;
import com.company.logistics.enums.PackageStatus;
import com.company.logistics.models.contracts.DeliveryPackage;
import com.company.logistics.models.contracts.Route;
import com.company.logistics.models.contracts.Truck;
import com.company.logistics.services.assignment.strategy.TruckRemovalStrategy;

public class DefaultTruckRemovalStrategy implements TruckRemovalStrategy {

    private final LogisticsRepository repository;

    private Route route;
    private Truck truck;

    public DefaultTruckRemovalStrategy(LogisticsRepository repository) {
        this.repository = repository;
    }

    @Override
    public void removeTruck(int truckId, int routeId) {
        truck= repository.findTruckById(truckId);
        route= repository.findRouteById(routeId);

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
