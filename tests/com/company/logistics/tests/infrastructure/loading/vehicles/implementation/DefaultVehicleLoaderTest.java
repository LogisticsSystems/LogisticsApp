package com.company.logistics.tests.infrastructure.loading.vehicles.implementation;

import com.company.logistics.infrastructure.loading.vehicles.implementation.DefaultVehicleLoader;
import com.company.logistics.models.contracts.Truck;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DefaultVehicleLoaderTest {
    @Test
    public void loadVehicles_Should_ReturnCorrectNumberOfTrucks() {
        DefaultVehicleLoader loader = new DefaultVehicleLoader();
        List<Truck> trucks = loader.loadVehicles();

        // Total trucks: 10 (Scania) + 15 (Man) + 15 (Actros) = 40
        assertEquals(40, trucks.size());
    }

    @Test
    public void loadVehicles_Should_ContainCorrectScaniaTrucks() {
        DefaultVehicleLoader loader = new DefaultVehicleLoader();
        List<Truck> trucks = loader.loadVehicles();

        for (int i = 0; i <= 9; i++) {
            Truck truck = trucks.get(i);
            assertEquals(1001 + i, truck.getId());
            assertEquals("Scania", truck.getName());
            assertEquals(42000.0, truck.getCapacityKg());
            assertEquals(8000.0, truck.getMaxRangeKm());
        }
    }

    @Test
    public void loadVehicles_Should_ContainCorrectManTrucks() {
        DefaultVehicleLoader loader = new DefaultVehicleLoader();
        List<Truck> trucks = loader.loadVehicles();

        for (int i = 10; i <= 24; i++) {
            Truck truck = trucks.get(i);
            assertEquals(1011 + (i - 10), truck.getId());
            assertEquals("Man", truck.getName());
            assertEquals(37000.0, truck.getCapacityKg());
            assertEquals(10000.0, truck.getMaxRangeKm());
        }
    }

    @Test
    public void loadVehicles_Should_ContainCorrectActrosTrucks() {
        DefaultVehicleLoader loader = new DefaultVehicleLoader();
        List<Truck> trucks = loader.loadVehicles();

        for (int i = 25; i < 40; i++) {
            Truck truck = trucks.get(i);
            assertEquals(1026 + (i - 25), truck.getId());
            assertEquals("Actros", truck.getName());
            assertEquals(26000.0, truck.getCapacityKg());
            assertEquals(13000.0, truck.getMaxRangeKm());
        }
    }
}
