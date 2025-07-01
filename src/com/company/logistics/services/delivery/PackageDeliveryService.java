package com.company.logistics.services.delivery;

import com.company.logistics.core.contracts.LogisticsRepository;
import com.company.logistics.enums.PackageStatus;
import com.company.logistics.models.contracts.DeliveryPackage;
import com.company.logistics.models.contracts.Route;
import com.company.logistics.utils.ValidationHelper;

public class PackageDeliveryService {
    private final LogisticsRepository repo;

    public PackageDeliveryService(LogisticsRepository repo) {
        this.repo = repo;
    }

    /**
     * Marks a package as delivered, removes it from its route,
     * and unassigns the truck if that was the last package on the route.
     */
    public DeliveryPackage deliverPackage(int packageId) {
        // 1) fetch the package
        DeliveryPackage pkg = repo.findPackageById(packageId);

        // 2) guard: only IN_TRANSIT packages can be delivered
        ValidationHelper.validatePackageStatus(pkg, PackageStatus.IN_TRANSIT);

        // 3) find the route that currently carries it
        Route route = repo.findRouteByPackageId(packageId);

        // 4) flip status to DELIVERED
        pkg.advancePackageStatus();

        // 5) detach pkg from the route
        route.removePackage(packageId);

        // 6) if no more packages on that route, unassign its truck
        if (route.getAssignedPackages().isEmpty()) {
            route.getAssignedTruck().ifPresent(truck -> {
                truck.unassignFromRoute();  // clear truck’s flag
                route.unassignTruck();      // clear route’s reference
            });
        }

        return pkg;
    }

}
