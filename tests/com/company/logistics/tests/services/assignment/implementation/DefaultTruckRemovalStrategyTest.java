package com.company.logistics.tests.services.assignment.implementation;

import com.company.logistics.enums.City;
import com.company.logistics.enums.PackageStatus;
import com.company.logistics.exceptions.InvalidUserInputException;
import com.company.logistics.models.contracts.Truck;
import com.company.logistics.models.delivery.DeliveryPackageImpl;
import com.company.logistics.models.delivery.RouteImpl;
import com.company.logistics.repositories.contracts.RouteRepository;
import com.company.logistics.repositories.contracts.TruckRepository;
import com.company.logistics.services.assignment.strategy.implementation.DefaultTruckRemovalStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DefaultTruckRemovalStrategyTest {
    private RouteRepository mockRouteRepo;
    private TruckRepository mockTruckRepo;
    private DefaultTruckRemovalStrategy strategy;

    private LocalDateTime departure;
    private LocalDateTime eta;

    @BeforeEach
    public void setup() {
        mockRouteRepo = mock(RouteRepository.class);
        mockTruckRepo = mock(TruckRepository.class);
        strategy = new DefaultTruckRemovalStrategy(mockRouteRepo, mockTruckRepo);

        departure = LocalDateTime.of(2024, 1, 1, 8, 0);
        eta = departure.plusHours(2);
    }

    @Test
    public void removeTruck_Should_UnassignSuccessfully_AndRevertPackages() {
        // Arrange
        DeliveryPackageImpl pkg1 = new DeliveryPackageImpl(1, City.SYD, City.MEL, 1000.0, "Alpha3");
        pkg1.advancePackageStatus();
        pkg1.advancePackageStatus();

        DeliveryPackageImpl pkg2 = new DeliveryPackageImpl(2, City.SYD, City.MEL, 1000.0, "Beta35");
        pkg2.advancePackageStatus();
        pkg2.advancePackageStatus();

        RouteImpl route = new RouteImpl(10, List.of(City.SYD, City.MEL), departure);
        route.setSchedule(List.of(departure, eta));
        route.assignPackage(pkg1);
        route.assignPackage(pkg2);

        Truck truck = mock(Truck.class);
        route.assignTruck(truck);

        when(mockTruckRepo.findTruckById(5)).thenReturn(truck);
        when(mockRouteRepo.findRouteById(10)).thenReturn(route);

        // Act
        List<Integer> changedIds = strategy.removeTruck(5, 10);

        // Assert
        assertFalse(route.getAssignedTruck().isPresent());
        verify(truck).unassignFromRoute();
        assertEquals(PackageStatus.PENDING, pkg1.getStatus());
        assertEquals(PackageStatus.PENDING, pkg2.getStatus());
        assertEquals(List.of(1, 2), changedIds);
    }

    @Test
    public void removeTruck_Should_Throw_WhenTruckNotAssignedToRoute() {
        // Arrange
        Truck truck = mock(Truck.class);
        RouteImpl route = new RouteImpl(10, List.of(City.SYD, City.MEL), departure);

        when(mockTruckRepo.findTruckById(5)).thenReturn(truck);
        when(mockRouteRepo.findRouteById(10)).thenReturn(route);

        // Assert
        assertThrows(InvalidUserInputException.class, () -> strategy.removeTruck(5, 10));
    }

    @Test
    public void removeTruck_Should_NotChangeStatus_IfPackagesNotInTransit() {
        // Arrange
        DeliveryPackageImpl pkg1 = new DeliveryPackageImpl(1, City.SYD, City.MEL, 1000.0, "Gamma3");
        pkg1.advancePackageStatus();

        RouteImpl route = new RouteImpl(10, List.of(City.SYD, City.MEL), departure);
        route.setSchedule(List.of(departure, eta));
        route.assignPackage(pkg1);

        Truck truck = mock(Truck.class);
        route.assignTruck(truck);

        when(mockTruckRepo.findTruckById(5)).thenReturn(truck);
        when(mockRouteRepo.findRouteById(10)).thenReturn(route);

        // Act
        List<Integer> changedIds = strategy.removeTruck(5, 10);

        // Assert
        assertEquals(PackageStatus.PENDING, pkg1.getStatus());
        assertTrue(changedIds.isEmpty());
        verify(truck).unassignFromRoute();
    }
}
