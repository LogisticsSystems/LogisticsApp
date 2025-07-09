package com.company.logistics.services.assignment.strategy.implementation;

import com.company.logistics.exceptions.InvalidUserInputException;
import com.company.logistics.repositories.contracts.RouteRepository;
import com.company.logistics.repositories.contracts.TruckRepository;
import com.company.logistics.services.assignment.strategy.TruckAssignmentStrategy;
import com.company.logistics.enums.PackageStatus;
import com.company.logistics.models.contracts.DeliveryPackage;
import com.company.logistics.models.contracts.Route;
import com.company.logistics.models.contracts.Truck;
import com.company.logistics.utils.Calculations;
import com.company.logistics.utils.ErrorMessages;
import com.company.logistics.utils.ValidationHelper;

public class DefaultTruckAssignmentStrategy implements TruckAssignmentStrategy {
    private final RouteRepository routeRepository;
    private final TruckRepository truckRepository;

    private Route route;
    private Truck truck;

    public DefaultTruckAssignmentStrategy(RouteRepository routeRepository,
                                          TruckRepository truckRepository) {
        this.routeRepository = routeRepository;
        this.truckRepository = truckRepository;
    }


    @Override
    public void assignTruck(int truckId, int routeId) {
        truck = truckRepository.findTruckById(truckId);
        route = routeRepository.findRouteById(routeId);

        validateTruck();

        route.assignTruck(truck);
        truck.assignToRoute();

        route.getAssignedPackages().stream()
                .filter(pkg -> pkg.getStatus() == PackageStatus.PENDING)
                .forEach(DeliveryPackage::advancePackageStatus);
    }

    private void validateTruck() {
        if (route.getAssignedTruck().isPresent()) {
            throw new InvalidUserInputException(String.format(ErrorMessages.TRUCK_ALREADY_ASSIGNED, route.getAssignedTruck().get().getId()));
        }

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
                    String.format(ErrorMessages.TRUCK_LOAD_EXCEEDS_CAPACITY,
                            truck.getId(),
                            route.getId(),
                            Calculations.calculateTotalLoad(route.getAssignedPackages()),
                            truck.getCapacityKg())
            );
        }
    }

}
