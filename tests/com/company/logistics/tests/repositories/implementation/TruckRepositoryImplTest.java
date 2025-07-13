package com.company.logistics.tests.repositories.implementation;

import com.company.logistics.exceptions.InvalidUserInputException;
import com.company.logistics.infrastructure.loading.vehicles.contracts.VehicleLoader;
import com.company.logistics.models.contracts.Truck;
import com.company.logistics.repositories.implementation.TruckRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TruckRepositoryImplTest {

    private TruckRepositoryImpl repo;
    private Truck truck1;
    private Truck truck2;

    @BeforeEach
    public void setup() {
        truck1 = mock(Truck.class);
        truck2 = mock(Truck.class);

        when(truck1.getId()).thenReturn(2);
        when(truck2.getId()).thenReturn(1);

        VehicleLoader mockLoader = mock(VehicleLoader.class);
        when(mockLoader.loadVehicles()).thenReturn(List.of(truck1, truck2));

        repo = new TruckRepositoryImpl(mockLoader);
    }

    @Test
    public void getTrucks_Should_ReturnSortedListById() {
        List<Truck> trucks = repo.getTrucks();

        assertEquals(2, trucks.size());
        assertEquals(1, trucks.get(0).getId());
        assertEquals(2, trucks.get(1).getId());
    }

    @Test
    public void findTruckById_Should_ReturnTruck_WhenExists() {
        Truck found = repo.findTruckById(2);
        assertEquals(truck1, found);
    }

    @Test
    public void findTruckById_Should_Throw_WhenNotFound() {
        assertThrows(InvalidUserInputException.class, () -> repo.findTruckById(999));
    }

    @Test
    public void clearAll_Should_EmptyRepository() {
        repo.clearAll();
        assertTrue(repo.getTrucks().isEmpty());
    }

    @Test
    public void addTruck_Should_IncludeTruckInResults() {
        Truck newTruck = mock(Truck.class);
        when(newTruck.getId()).thenReturn(5);

        repo.clearAll();
        repo.addTruck(newTruck);

        List<Truck> trucks = repo.getTrucks();
        assertEquals(1, trucks.size());
        assertEquals(5, trucks.get(0).getId());
    }
}
