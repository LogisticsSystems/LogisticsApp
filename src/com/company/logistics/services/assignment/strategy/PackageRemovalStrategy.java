package com.company.logistics.services.assignment.strategy;

import com.company.logistics.models.contracts.DeliveryPackage;

public interface PackageRemovalStrategy{

    DeliveryPackage removePackage(int packageId, int routeId);
}
