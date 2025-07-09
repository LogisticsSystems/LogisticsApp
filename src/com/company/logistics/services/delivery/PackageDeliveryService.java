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

    public DeliveryPackage deliverPackage(int packageId) {
        DeliveryPackage pkg = repo.findPackageById(packageId);

        ValidationHelper.validatePackageStatus(pkg, PackageStatus.IN_TRANSIT);

        Route route = repo.findRouteByPackageId(packageId);

        pkg.advancePackageStatus();
        route.removePackage(packageId);

        if (route.getAssignedPackages().isEmpty()) {
            route.getAssignedTruck().ifPresent(truck -> {
                truck.unassignFromRoute();  // clear truck’s flag
                route.unassignTruck();      // clear route’s reference
            });
        }

        return pkg;
    }

}
