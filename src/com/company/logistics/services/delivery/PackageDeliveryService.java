package com.company.logistics.services.delivery;

import com.company.logistics.enums.PackageStatus;
import com.company.logistics.models.contracts.DeliveryPackage;
import com.company.logistics.models.contracts.Route;
import com.company.logistics.repositories.contracts.PackageRepository;
import com.company.logistics.repositories.contracts.RouteRepository;
import com.company.logistics.utils.ValidationHelper;

public class PackageDeliveryService {
    private final PackageRepository packageRepository;
    private final RouteRepository routeRepository;

    public PackageDeliveryService(PackageRepository packageRepository,
                                  RouteRepository routeRepository) {
        this.packageRepository = packageRepository;
        this.routeRepository   = routeRepository;
    }

    public DeliveryPackage deliverPackage(int packageId) {
        DeliveryPackage pkg = packageRepository.findPackageById(packageId);

        ValidationHelper.validatePackageStatus(pkg, PackageStatus.IN_TRANSIT);

        Route route = routeRepository.findRouteByPackageId(packageId);

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
