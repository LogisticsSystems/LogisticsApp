package com.company.logistics.repositories.implementation;

import com.company.logistics.exceptions.InvalidUserInputException;
import com.company.logistics.infrastructure.loading.vehicles.contracts.VehicleLoader;
import com.company.logistics.models.contracts.Truck;
import com.company.logistics.repositories.contracts.TruckRepository;
import com.company.logistics.utils.ErrorMessages;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TruckRepositoryImpl implements TruckRepository {
    private final Map<Integer, Truck> trucks = new HashMap<>();

    public TruckRepositoryImpl(VehicleLoader vehicleLoader) {
        vehicleLoader.loadVehicles().forEach(truck -> trucks.put(truck.getId(), truck));
    }

    @Override
    public List<Truck> getTrucks() {
        return trucks.values().stream()
                .sorted(Comparator.comparingInt(Truck::getId))
                .collect(Collectors.toList());
    }

    @Override
    public Truck findTruckById(int id) {
        Truck truck = trucks.get(id);
        if (truck == null) {
            throw new InvalidUserInputException(String.format(ErrorMessages.NO_TRUCK_WITH_ID, id));
        }
        return truck;
    }
}
