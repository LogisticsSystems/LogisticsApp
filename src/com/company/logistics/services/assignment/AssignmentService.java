package com.company.logistics.services.assignment;

import com.company.logistics.dto.PackageSnapshot;
import com.company.logistics.models.contracts.DeliveryPackage;
import com.company.logistics.services.assignment.strategy.PackageAssignmentStrategy;
import com.company.logistics.services.assignment.strategy.PackageRemovalStrategy;
import com.company.logistics.services.assignment.strategy.TruckAssignmentStrategy;
import com.company.logistics.services.assignment.strategy.TruckRemovalStrategy;

import java.util.List;

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

    public PackageSnapshot assignPackageToRoute(int pkgId, int routeId) {
        return packageAssignmentStrategy.assignPackage(pkgId, routeId);
    }


    public List<Integer> assignTruckToRoute(int truckId, int routeId) {
        return truckAssignmentStrategy.assignTruck(truckId, routeId);
    }


    public List<Integer> removeTruckFromRoute(int truckId, int routeId){
        return truckRemovalStrategy.removeTruck(truckId,routeId);
    }


    public PackageSnapshot removePackageFromRoute(int packageId, int routeId){
        return packageRemovalStrategy.removePackage(packageId,routeId);
    }
}
