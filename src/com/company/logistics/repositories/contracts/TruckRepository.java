package com.company.logistics.repositories.contracts;

import com.company.logistics.models.contracts.Truck;

import java.util.List;

public interface TruckRepository {

    List<Truck> getTrucks();

    Truck findTruckById(int id);
}
