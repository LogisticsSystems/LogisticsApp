package com.company.logistics.core.services.assignment.strategy.implementation;

import com.company.logistics.core.contracts.LogisticsRepository;
import com.company.logistics.core.services.assignment.strategy.TruckAssignmentStrategy;
import com.company.logistics.enums.PackageStatus;
import com.company.logistics.models.contracts.DeliveryPackage;
import com.company.logistics.models.contracts.Route;
import com.company.logistics.models.contracts.Truck;
import com.company.logistics.utils.Calculations;
import com.company.logistics.utils.ErrorMessages;
import com.company.logistics.utils.ValidationHelper;

public class DefaultTruckAssignmentStrategy implements TruckAssignmentStrategy {
    private final LogisticsRepository repository;

    private Route route;
    private Truck truck;

    public DefaultTruckAssignmentStrategy(LogisticsRepository repository) {
        this.repository = repository;
    }


    @Override
    public void assignTruck(int truckId, int routeId) {
        truck = repository.findTruckById(truckId);
        route = repository.findRouteById(routeId);

        validateTruck();

        route.assignTruck(truck);
        truck.assignToRoute();

        route.getAssignedPackages().stream()
                .filter(pkg -> pkg.getStatus() == PackageStatus.PENDING)
                .forEach(DeliveryPackage::advancePackageStatus);
    }

    private void validateTruck() {
        ValidationHelper.validateRouteRangeWithin(
                route.getLocations(),
                truck.getMaxRangeKm(),
                truck.getId(),
                route.getId()
        );

        if (!route.getAssignedPackages().isEmpty()) {
            ValidationHelper.validateTotalLoadWithinCapacity(
                    route.getAssignedPackages(),
                    truck.getCapacityKg(),
                    truck.getId(),
                    route.getId(),
                    String.format(ErrorMessages.TRUCK_LOAD_EXCEEDS_CAPACITY,
                            truck.getId(),
                            route.getId(),
                            Calculations.calculateTotalLoad(route.getAssignedPackages()),
                            truck.getCapacityKg())
            );
        }



    }

}
