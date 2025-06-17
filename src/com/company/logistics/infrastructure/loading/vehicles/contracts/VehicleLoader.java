package com.company.logistics.infrastructure.loading.vehicles.contracts;

import com.company.logistics.models.contracts.Truck;

import java.util.List;

public interface VehicleLoader {

    List<Truck> loadVehicles();

}
