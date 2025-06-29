package com.company.logistics.core.services.assignment;

import com.company.logistics.core.services.assignment.strategy.PackageAssignmentStrategy;
import com.company.logistics.core.services.assignment.strategy.TruckAssignmentStrategy;

public class AssignmentService {
    private final PackageAssignmentStrategy packageAssignmentStrategy;
    private final TruckAssignmentStrategy truckAssignmentStrategy;

    public AssignmentService(PackageAssignmentStrategy packageAssignmentStrategy,
                             TruckAssignmentStrategy truckAssignmentStrategy) {
        this.packageAssignmentStrategy = packageAssignmentStrategy;
        this.truckAssignmentStrategy = truckAssignmentStrategy;
    }

    public void assignPackageToRoute(int pkgId, int routeId) {
        packageAssignmentStrategy.assignPackage(pkgId, routeId);
    }


    public void assignTruckToRoute(int truckId, int routeId) {
        truckAssignmentStrategy.assignTruck(truckId, routeId);
    }
}
