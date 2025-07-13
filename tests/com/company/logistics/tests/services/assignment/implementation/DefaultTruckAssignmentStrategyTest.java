package com.company.logistics.tests.services.assignment.implementation;

import com.company.logistics.enums.City;
import com.company.logistics.enums.PackageStatus;
import com.company.logistics.exceptions.InvalidUserInputException;
import com.company.logistics.infrastructure.DistanceMap;
import com.company.logistics.infrastructure.loading.distances.contracts.DistanceLoader;
import com.company.logistics.models.contracts.Truck;
import com.company.logistics.models.delivery.DeliveryPackageImpl;
import com.company.logistics.models.delivery.RouteImpl;
import com.company.logistics.repositories.contracts.RouteRepository;
import com.company.logistics.repositories.contracts.TruckRepository;
import com.company.logistics.services.assignment.strategy.implementation.DefaultTruckAssignmentStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DefaultTruckAssignmentStrategyTest {
    private RouteRepository mockRouteRepo;
    private TruckRepository mockTruckRepo;
    private DefaultTruckAssignmentStrategy strategy;

    private LocalDateTime departure;
    private LocalDateTime eta;

    @BeforeEach
    public void setup() throws NoSuchFieldException, IllegalAccessException {
        Field instance = DistanceMap.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null, null);

        DistanceLoader mockLoader = mock(DistanceLoader.class);
        Map<City, Map<City, Integer>> mockDistances = new EnumMap<>(City.class);
        Map<City, Integer> toMel = new EnumMap<>(City.class);
        toMel.put(City.MEL, 800);
        mockDistances.put(City.SYD, toMel);

        when(mockLoader.loadDistances()).thenReturn(mockDistances);
        DistanceMap.initialize(mockLoader);

        mockRouteRepo = mock(RouteRepository.class);
        mockTruckRepo = mock(TruckRepository.class);
        strategy = new DefaultTruckAssignmentStrategy(mockRouteRepo, mockTruckRepo);

        departure = LocalDateTime.of(2024, 1, 1, 8, 0);
        eta = departure.plusHours(2);
    }

    @Test
    public void assignTruck_Should_AssignSuccessfully_WhenValid() {
        // Arrange
        RouteImpl route = new RouteImpl(10, List.of(City.SYD, City.MEL), departure);
        route.setSchedule(List.of(departure, eta));

        Truck truck = mock(Truck.class);
        when(truck.getMaxRangeKm()).thenReturn(1000.0);
        when(truck.getCapacityKg()).thenReturn(42000.0);

        when(mockRouteRepo.findRouteById(10)).thenReturn(route);
        when(mockTruckRepo.findTruckById(5)).thenReturn(truck);

        // Act
        List<Integer> changedIds = strategy.assignTruck(5, 10);

        // Assert
        assertTrue(route.getAssignedTruck().isPresent());
        assertEquals(truck, route.getAssignedTruck().get());
        verify(truck).assignToRoute();
        assertTrue(changedIds.isEmpty());
    }

    @Test
    public void assignTruck_Should_Throw_WhenTruckAlreadyAssigned() {
        RouteImpl route = new RouteImpl(10, List.of(City.SYD, City.MEL), departure);
        Truck assignedTruck = mock(Truck.class);
        when(assignedTruck.getId()).thenReturn(99);
        route.assignTruck(assignedTruck);

        Truck newTruck = mock(Truck.class);
        when(newTruck.getId()).thenReturn(5);

        when(mockRouteRepo.findRouteById(10)).thenReturn(route);
        when(mockTruckRepo.findTruckById(5)).thenReturn(newTruck);

        assertThrows(InvalidUserInputException.class, () -> strategy.assignTruck(5, 10));
    }

    @Test
    public void assignTruck_Should_AdvanceStatus_OfPendingPackages() {
        // Arrange
        DeliveryPackageImpl pkg1 = new DeliveryPackageImpl(1, City.SYD, City.MEL, 1000, "Alpha_1");
        pkg1.advancePackageStatus();

        DeliveryPackageImpl pkg2 = new DeliveryPackageImpl(2, City.SYD, City.MEL, 2000, "Beta_2");
        pkg2.advancePackageStatus();

        RouteImpl route = new RouteImpl(10, List.of(City.SYD, City.MEL), departure);
        route.setSchedule(List.of(departure, eta));
        route.assignPackage(pkg1);
        route.assignPackage(pkg2);

        Truck truck = mock(Truck.class);
        when(truck.getMaxRangeKm()).thenReturn(1000.0);
        when(truck.getCapacityKg()).thenReturn(42000.0);

        when(mockRouteRepo.findRouteById(10)).thenReturn(route);
        when(mockTruckRepo.findTruckById(5)).thenReturn(truck);

        // Act
        List<Integer> changedIds = strategy.assignTruck(5, 10);

        // Assert
        assertEquals(PackageStatus.IN_TRANSIT, pkg1.getStatus());
        assertEquals(PackageStatus.IN_TRANSIT, pkg2.getStatus());
        assertEquals(List.of(1, 2), changedIds);
    }

    @Test
    public void assignTruck_Should_Throw_WhenRangeTooShort() {
        RouteImpl route = new RouteImpl(10, List.of(City.SYD, City.MEL), departure);
        Truck truck = mock(Truck.class);
        when(truck.getMaxRangeKm()).thenReturn(700.0);

        when(mockRouteRepo.findRouteById(10)).thenReturn(route);
        when(mockTruckRepo.findTruckById(5)).thenReturn(truck);

        assertThrows(InvalidUserInputException.class, () -> strategy.assignTruck(5, 10));
    }

    @Test
    public void assignTruck_Should_Throw_WhenLoadExceedsCapacity() {
        DeliveryPackageImpl heavy1 = new DeliveryPackageImpl(1, City.SYD, City.MEL, 23000.0, "Heavy");
        DeliveryPackageImpl heavy2 = new DeliveryPackageImpl(1, City.SYD, City.MEL, 23000.0, "Heavy");
        heavy1.advancePackageStatus();
        heavy2.advancePackageStatus();

        RouteImpl route = new RouteImpl(10, List.of(City.SYD, City.MEL), departure);
        route.setSchedule(List.of(departure, eta));
        route.assignPackage(heavy1);
        route.assignPackage(heavy2);

        Truck truck = mock(Truck.class);
        when(truck.getMaxRangeKm()).thenReturn(1000.0);
        when(truck.getCapacityKg()).thenReturn(42000.0);

        when(mockRouteRepo.findRouteById(10)).thenReturn(route);
        when(mockTruckRepo.findTruckById(5)).thenReturn(truck);

        assertThrows(InvalidUserInputException.class, () -> strategy.assignTruck(5, 10));
    }
}
