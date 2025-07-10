package com.company.logistics.services.assignment.strategy;

import com.company.logistics.dto.PackageSnapshot;

public interface PackageRemovalStrategy{

    PackageSnapshot removePackage(int packageId, int routeId);
}
