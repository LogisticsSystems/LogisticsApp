package com.company.logistics.core.services.assignment.strategy.implementation;

import com.company.logistics.core.contracts.LogisticsRepository;
import com.company.logistics.core.services.assignment.strategy.PackageAssignmentStrategy;
import com.company.logistics.core.services.routing.scheduling.RouteScheduleService;
import com.company.logistics.core.services.speeds.SpeedModelService;
import com.company.logistics.enums.PackageStatus;
import com.company.logistics.models.contracts.DeliveryPackage;
import com.company.logistics.models.contracts.Route;
import com.company.logistics.utils.ValidationHelper;

import java.time.LocalDateTime;

public class DefaultPackageAssignmentStrategy implements PackageAssignmentStrategy {
    private final LogisticsRepository repository;
    private final SpeedModelService speedModelService;

    public DefaultPackageAssignmentStrategy(
            LogisticsRepository repository,
            SpeedModelService speedModelService
    ) {
        this.repository = repository;
        this.speedModelService = speedModelService;
    }

    @Override
    public void assignPackage(int packageId, int routeId) {
        DeliveryPackage pack  = repository.findPackageById(packageId);
        Route           route = repository.findRouteById(routeId);

        // only unassigned packages may be added
        ValidationHelper.validatePackageStatus(pack, PackageStatus.UNASSIGNED);

        // location-compatibility
        ValidationHelper.validatePackageRouteCompatibility(
                pack.getStartLocation(),
                pack.getEndLocation(),
                route.getLocations()
        );

        // attach it...
        route.assignPackage(pack);

        // UNASSIGNED → PENDING
        pack.advancePackageStatus();

        // if there was already a truck, go PENDING → IN_TRANSIT immediately
        if (route.getAssignedTruck().isPresent()) {
            pack.advancePackageStatus();
        }

        // compute and set ETA
        LocalDateTime eta = speedModelService.getRouteScheduler().getEtaForCity(
                pack.getEndLocation(),
                route.getLocations(),
                route.getSchedule()
        );
        pack.setExpectedArrival(eta);
    }

}
