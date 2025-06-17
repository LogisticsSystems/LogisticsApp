package com.company.logistics.infrastructure.loading.vehicles.implementation;

import com.company.logistics.infrastructure.loading.vehicles.contracts.VehicleLoader;
import com.company.logistics.models.contracts.Truck;
import com.company.logistics.models.vehicles.TruckImpl;

import java.util.ArrayList;
import java.util.List;

public class DefaultVehicleLoader implements VehicleLoader {

    @Override
    public List<Truck> loadVehicles() {
        List<Truck> trucks = new ArrayList<>();
        addRange(trucks, "Scania", 42000, 8000, 1001, 1010);
        addRange(trucks, "Man",    37000,10000, 1011,1025);
        addRange(trucks, "Actros", 26000,13000, 1026,1040);
        return trucks;
    }

    private void addRange(List<Truck> list, String name,
                          double capacity, double maxRange,
                          int startId, int endId) {
        for (int id = startId; id <= endId; id++) {
            list.add(new TruckImpl(id, name, capacity, maxRange));
        }
    }
}
