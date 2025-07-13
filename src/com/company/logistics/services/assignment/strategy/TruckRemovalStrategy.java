package com.company.logistics.services.assignment.strategy;

import java.util.List;

public interface TruckRemovalStrategy {

    List<Integer> removeTruck(int truckId, int routeId);
}
