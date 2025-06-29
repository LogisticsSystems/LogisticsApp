package com.company.logistics.core.services.assignment.strategy.implementation;

import com.company.logistics.core.contracts.LogisticsRepository;
import com.company.logistics.core.services.assignment.strategy.TruckAssignmentStrategy;
import com.company.logistics.enums.PackageStatus;
import com.company.logistics.models.contracts.DeliveryPackage;
import com.company.logistics.models.contracts.Route;
import com.company.logistics.models.contracts.Truck;
import com.company.logistics.utils.ValidationHelper;

public class DefaultTruckAssignmentStrategy implements TruckAssignmentStrategy {
    private final LogisticsRepository repository;

    public DefaultTruckAssignmentStrategy(LogisticsRepository repository) {
        this.repository = repository;
    }


    @Override
    public void assignTruck(int truckId, int routeId) {
        Truck truck = repository.findTruckById(truckId);
        Route route = repository.findRouteById(routeId);

        ValidationHelper.validateTotalLoadWithinCapacity(
                route.getAssignedPackages(), truck.getCapacityKg(), truckId, routeId
        );
        ValidationHelper.validateRouteRangeWithin(
                route.getLocations(), truck.getMaxRangeKm(), truckId, routeId
        );

        route.assignTruck(truck);
        truck.assignToRoute();

        route.getAssignedPackages().stream()
                .filter(pkg -> pkg.getStatus() == PackageStatus.PENDING)
                .forEach(DeliveryPackage::advancePackageStatus);
    }

}
