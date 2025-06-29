package com.company.logistics.core.services.assignment;

import com.company.logistics.core.contracts.LogisticsRepository;
import com.company.logistics.core.services.routing.scheduling.RouteScheduleService;
import com.company.logistics.core.services.speeds.SpeedModelService;
import com.company.logistics.enums.PackageStatus;
import com.company.logistics.models.contracts.DeliveryPackage;
import com.company.logistics.models.contracts.Route;
import com.company.logistics.models.contracts.Truck;
import com.company.logistics.utils.ValidationHelper;

public class AssignmentService {
    private final LogisticsRepository repository;
    private final SpeedModelService speedModelService;

    public AssignmentService(LogisticsRepository repository,
                             SpeedModelService speedModelService) {
        this.repository = repository;
        this.speedModelService = speedModelService;
    }

    /** Assigns a package to a route, enforces compatibility & status changes. */
    public void assignPackageToRoute(int pkgId, int routeId) {
        DeliveryPackage pkg = repository.findPackageById(pkgId);
        Route route = repository.findRouteById(routeId);

        ValidationHelper.validatePackageRouteCompatibility(
                pkg.getStartLocation(), pkg.getEndLocation(), route.getLocations()
        );

        // UNASSIGNED → PENDING
        pkg.advancePackageStatus();

        // if there's already a truck, go PENDING → IN_TRANSIT
        if (route.getAssignedTruck().isPresent()) pkg.advancePackageStatus();

        route.assignPackage(pkg);

        // set ETA with the current scheduler
        RouteScheduleService scheduler = speedModelService.getRouteScheduler();
        pkg.setExpectedArrival(
                scheduler.getEtaForCity(
                        pkg.getEndLocation(), route.getLocations(), route.getSchedule()
                )
        );
    }

    /** Assigns a truck, checks load & range, flips any PENDING packages to IN_TRANSIT. */
    public void assignTruckToRoute(int truckId, int routeId) {
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
