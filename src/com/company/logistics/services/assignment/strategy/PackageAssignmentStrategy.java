package com.company.logistics.services.assignment.strategy;

import com.company.logistics.models.contracts.DeliveryPackage;

public interface PackageAssignmentStrategy {

    DeliveryPackage assignPackage(int pkgId, int routeId);

}
