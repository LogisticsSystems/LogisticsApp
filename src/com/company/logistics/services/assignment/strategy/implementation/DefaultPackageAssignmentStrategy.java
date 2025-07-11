package com.company.logistics.services.assignment.strategy.implementation;

import com.company.logistics.dto.PackageSnapshot;
import com.company.logistics.repositories.contracts.PackageRepository;
import com.company.logistics.repositories.contracts.RouteRepository;
import com.company.logistics.services.assignment.strategy.PackageAssignmentStrategy;
import com.company.logistics.services.speeds.SpeedModelService;
import com.company.logistics.enums.PackageStatus;
import com.company.logistics.models.contracts.DeliveryPackage;
import com.company.logistics.models.contracts.Route;
import com.company.logistics.models.contracts.Truck;
import com.company.logistics.utils.Calculations;
import com.company.logistics.utils.ErrorMessages;
import com.company.logistics.utils.ValidationHelper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DefaultPackageAssignmentStrategy implements PackageAssignmentStrategy {
    private static final double MAX_TRUCK_CAPACITY = 42000;

    private final PackageRepository packageRepository;
    private final RouteRepository routeRepository;
    private final SpeedModelService speedModelService;

    private DeliveryPackage pack;
    private Route route;
    private Truck truck;

    public DefaultPackageAssignmentStrategy(
            PackageRepository packageRepository,
            RouteRepository routeRepository,
            SpeedModelService speedModelService
    ) {
        this.packageRepository = packageRepository;
        this.routeRepository   = routeRepository;
        this.speedModelService = speedModelService;
    }

    @Override
    public PackageSnapshot assignPackage(int packageId, int routeId) {
        pack  = packageRepository.findPackageById(packageId);
        route = routeRepository.findRouteById(routeId);
        if (route.getAssignedTruck().isPresent()) {
            truck = route.getAssignedTruck().get();
        }

        validatePackage();

        route.assignPackage(pack);
        updatePackageStatus();
        setEtaToPackage();

        return new PackageSnapshot(
                pack.getId(),
                pack.getStatus(),
                pack.getExpectedArrival(),
                ""
        );
    }

    private void updatePackageStatus() {
        pack.advancePackageStatus();

        if (route.getAssignedTruck().isPresent()) {
            pack.advancePackageStatus();
        }
    }

    private void validatePackage() {
        ValidationHelper.validatePackageStatus(pack, PackageStatus.UNASSIGNED);

        ValidationHelper.validatePackageRouteCompatibility(
                pack.getStartLocation(),
                pack.getEndLocation(),
                route.getLocations()
        );

        validateWeightWithNewPackage();
    }

    private void validateWeightWithNewPackage() {

        List<DeliveryPackage> packs = new ArrayList<>(route.getAssignedPackages());
        packs.add(pack);

        if (route.getAssignedTruck().isPresent()) {
            ValidationHelper.validateTotalLoadWithinCapacity(
                    packs,
                    truck.getCapacityKg(),
                    String.format(ErrorMessages.ROUTE_LOAD_EXCEEDS_CAPACITY,
                            pack.getId(),
                            route.getId(),
                            Calculations.calculateTotalLoad(packs),
                            truck.getCapacityKg(),
                            truck.getName())
            );
        } else {
            ValidationHelper.validateTotalLoadWithinCapacity(
                    packs,
                    MAX_TRUCK_CAPACITY,
                    String.format(ErrorMessages.ROUTE_MAX_LOAD_EXCEEDS_CAPACITY,
                            pack.getId(),
                            route.getId(),
                            Calculations.calculateTotalLoad(packs),
                            MAX_TRUCK_CAPACITY)
            );
        }
    }

    private void setEtaToPackage() {
        LocalDateTime eta = speedModelService.getRouteScheduler().getEtaForCity(
                pack.getEndLocation(),
                route.getLocations(),
                route.getSchedule()
        );
        pack.setExpectedArrival(eta);
    }
}
