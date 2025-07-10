package com.company.logistics.services.assignment.strategy;

import java.util.List;

public interface TruckAssignmentStrategy {

    List<Integer> assignTruck(int truckId, int routeId);

}
