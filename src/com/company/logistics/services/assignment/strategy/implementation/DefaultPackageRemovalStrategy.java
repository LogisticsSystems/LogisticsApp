package com.company.logistics.services.assignment.strategy.implementation;

import com.company.logistics.core.contracts.LogisticsRepository;
import com.company.logistics.enums.PackageStatus;
import com.company.logistics.models.contracts.DeliveryPackage;
import com.company.logistics.models.contracts.Route;
import com.company.logistics.services.assignment.strategy.PackageRemovalStrategy;

public class DefaultPackageRemovalStrategy implements PackageRemovalStrategy {

    private final LogisticsRepository repository;


    private DeliveryPackage deliveryPackage;
    private Route route;

    public DefaultPackageRemovalStrategy(LogisticsRepository repository) {
        this.repository = repository;
    }


    @Override
    public void removePackage(int packageId, int routeId) {
        deliveryPackage=repository.findPackageById(packageId);
        route= repository.findRouteById(routeId);

        route.removePackage(packageId);
        updatePackageStatus(deliveryPackage);
    }

    private void updatePackageStatus(DeliveryPackage deliveryPackage) {
        while(deliveryPackage.getStatus() == PackageStatus.UNASSIGNED){
            deliveryPackage.revertPackageStatus();
        }
    }
}
