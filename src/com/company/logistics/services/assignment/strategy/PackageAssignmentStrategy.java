package com.company.logistics.services.assignment.strategy;

import com.company.logistics.dto.PackageSnapshot;

public interface PackageAssignmentStrategy {

    PackageSnapshot assignPackage(int pkgId, int routeId);

}
