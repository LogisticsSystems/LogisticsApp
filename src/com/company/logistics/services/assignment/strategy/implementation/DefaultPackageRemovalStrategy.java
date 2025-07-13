package com.company.logistics.services.assignment.strategy.implementation;

import com.company.logistics.dto.PackageSnapshot;
import com.company.logistics.enums.PackageStatus;
import com.company.logistics.models.contracts.DeliveryPackage;
import com.company.logistics.models.contracts.Route;
import com.company.logistics.repositories.contracts.PackageRepository;
import com.company.logistics.repositories.contracts.RouteRepository;
import com.company.logistics.services.assignment.strategy.PackageRemovalStrategy;
import com.company.logistics.utils.ValidationHelper;

public class DefaultPackageRemovalStrategy implements PackageRemovalStrategy {

    private final PackageRepository packageRepository;
    private final RouteRepository routeRepository;

    public DefaultPackageRemovalStrategy(PackageRepository packageRepository,
                                         RouteRepository routeRepository) {
        this.packageRepository = packageRepository;
        this.routeRepository   = routeRepository;
    }


    @Override
    public PackageSnapshot removePackage(int packageId, int routeId) {
        DeliveryPackage deliveryPackage = packageRepository.findPackageById(packageId);
        Route route = routeRepository.findRouteById(routeId);

        ValidationHelper.validatePackageInRoute(deliveryPackage,route);

        route.removePackage(packageId);
        updatePackageStatus(deliveryPackage);

        return new PackageSnapshot(
                deliveryPackage.getId(),
                deliveryPackage.getStatus(),
                deliveryPackage.getExpectedArrival(),
                ""
        );
    }

    private void updatePackageStatus(DeliveryPackage deliveryPackage) {
        while(deliveryPackage.getStatus() != PackageStatus.UNASSIGNED){
            deliveryPackage.revertPackageStatus();
        }
    }
}
