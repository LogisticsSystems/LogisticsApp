package com.company.logistics.services.assignment;

import com.company.logistics.services.assignment.strategy.PackageAssignmentStrategy;
import com.company.logistics.services.assignment.strategy.PackageRemovalStrategy;
import com.company.logistics.services.assignment.strategy.TruckAssignmentStrategy;
import com.company.logistics.services.assignment.strategy.TruckRemovalStrategy;

public class AssignmentService {
    private final PackageAssignmentStrategy packageAssignmentStrategy;
    private final TruckAssignmentStrategy truckAssignmentStrategy;
    private final TruckRemovalStrategy truckRemovalStrategy;
    private final PackageRemovalStrategy packageRemovalStrategy;

    public AssignmentService(PackageAssignmentStrategy packageAssignmentStrategy,
                             TruckAssignmentStrategy truckAssignmentStrategy,
                             TruckRemovalStrategy truckRemovalStrategy,
                             PackageRemovalStrategy packageRemovalStrategy) {
        this.packageAssignmentStrategy = packageAssignmentStrategy;
        this.truckAssignmentStrategy = truckAssignmentStrategy;
        this.truckRemovalStrategy = truckRemovalStrategy;
        this.packageRemovalStrategy = packageRemovalStrategy;

    }

    public void assignPackageToRoute(int pkgId, int routeId) {
        packageAssignmentStrategy.assignPackage(pkgId, routeId);
    }


    public void assignTruckToRoute(int truckId, int routeId) {
        truckAssignmentStrategy.assignTruck(truckId, routeId);
    }


    public void removeTruckFromRoute(int truckId, int routeId){
        truckRemovalStrategy.removeTruck(truckId,routeId);
    }


    public void removePackageFromRoute(int packageId, int routeId){
        packageRemovalStrategy.removePackage(packageId,routeId);
    }
}
