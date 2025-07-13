package com.company.logistics.tests.services.assignment.implementation;

import com.company.logistics.dto.PackageSnapshot;
import com.company.logistics.enums.City;
import com.company.logistics.enums.PackageStatus;
import com.company.logistics.exceptions.InvalidUserInputException;
import com.company.logistics.models.contracts.Truck;
import com.company.logistics.models.delivery.DeliveryPackageImpl;
import com.company.logistics.models.delivery.RouteImpl;
import com.company.logistics.repositories.contracts.PackageRepository;
import com.company.logistics.repositories.contracts.RouteRepository;
import com.company.logistics.services.assignment.strategy.implementation.DefaultPackageAssignmentStrategy;
import com.company.logistics.services.routing.scheduling.RouteScheduleService;
import com.company.logistics.services.speeds.SpeedModelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class DefaultPackageAssignmentStrategyTest {

    private PackageRepository mockPackageRepo;
    private RouteRepository mockRouteRepo;
    private SpeedModelService mockSpeedService;
    private RouteScheduleService mockScheduler;

    private DefaultPackageAssignmentStrategy strategy;
    private LocalDateTime departure;
    private LocalDateTime eta;

    @BeforeEach
    public void setup() {
        mockPackageRepo = mock(PackageRepository.class);
        mockRouteRepo = mock(RouteRepository.class);
        mockSpeedService = mock(SpeedModelService.class);
        mockScheduler = mock(RouteScheduleService.class);
        strategy = new DefaultPackageAssignmentStrategy(mockPackageRepo, mockRouteRepo, mockSpeedService);

        departure = LocalDateTime.of(2024, 1, 1, 8, 0);
        eta = departure.plusHours(2);

        when(mockSpeedService.getRouteScheduler()).thenReturn(mockScheduler);
        when(mockScheduler.getEtaForCity(eq(City.MEL), any(), any())).thenReturn(eta);
    }

    @Test
    public void assignPackage_Should_AssignCorrectly_WithoutTruck() {
        // Arrange
        DeliveryPackageImpl deliveryPackage = new DeliveryPackageImpl(1, City.SYD, City.MEL, 1000.0, "John Doe");
        RouteImpl route = new RouteImpl(10, List.of(City.SYD, City.MEL), departure);
        route.setSchedule(List.of(departure, eta));

        when(mockPackageRepo.findPackageById(1)).thenReturn(deliveryPackage);
        when(mockRouteRepo.findRouteById(10)).thenReturn(route);

        // Act
        PackageSnapshot snapshot = strategy.assignPackage(1, 10);

        // Assert
        assertEquals(1, snapshot.id());
        assertEquals(PackageStatus.PENDING, snapshot.status());
        assertEquals(eta, snapshot.eta());
        assertEquals(1, route.getAssignedPackages().size());
    }

    @Test
    public void assignPackage_Should_AssignCorrectly_WithTruck() {
        // Arrange
        DeliveryPackageImpl deliveryPackage = new DeliveryPackageImpl(1, City.SYD, City.MEL, 1000.0, "Jane Smith");
        RouteImpl route = new RouteImpl(10, List.of(City.SYD, City.MEL), departure);
        route.setSchedule(List.of(departure, eta));

        Truck truck = mock(Truck.class);
        when(truck.getCapacityKg()).thenReturn(42000.0);

        route.assignTruck(truck);

        when(mockPackageRepo.findPackageById(1)).thenReturn(deliveryPackage);
        when(mockRouteRepo.findRouteById(10)).thenReturn(route);

        // Act
        PackageSnapshot snapshot = strategy.assignPackage(1, 10);

        // Assert
        assertEquals(1, snapshot.id());
        assertEquals(PackageStatus.IN_TRANSIT, snapshot.status());
        assertEquals(eta, snapshot.eta());
        assertEquals(1, route.getAssignedPackages().size());
        assertEquals(PackageStatus.IN_TRANSIT, deliveryPackage.getStatus());
    }

    @Test
    public void assignPackage_Should_Throw_WhenPackageNotUnassigned() {
        DeliveryPackageImpl pkg = new DeliveryPackageImpl(1, City.SYD, City.MEL, 1000.0, "Contact");
        pkg.advancePackageStatus();

        RouteImpl route = new RouteImpl(10, List.of(City.SYD, City.MEL), departure);
        route.setSchedule(List.of(departure, eta));

        when(mockPackageRepo.findPackageById(1)).thenReturn(pkg);
        when(mockRouteRepo.findRouteById(10)).thenReturn(route);

        assertThrows(InvalidUserInputException.class, () -> strategy.assignPackage(1, 10));
    }

    @Test
    public void assignPackage_Should_Throw_WhenRouteIncompatibleWithPackage() {
        DeliveryPackageImpl pkg = new DeliveryPackageImpl(1, City.SYD, City.MEL, 1000.0, "Contact");
        RouteImpl route = new RouteImpl(10, List.of(City.BRI, City.PER), departure);
        route.setSchedule(List.of(departure, eta));

        when(mockPackageRepo.findPackageById(1)).thenReturn(pkg);
        when(mockRouteRepo.findRouteById(10)).thenReturn(route);

        assertThrows(InvalidUserInputException.class, () -> strategy.assignPackage(1, 10));
    }

    @Test
    public void assignPackage_Should_Throw_WhenExceedingTruckCapacity() {
        DeliveryPackageImpl pkg = new DeliveryPackageImpl(1, City.SYD, City.MEL, 30000.0, "Contact");
        RouteImpl route = new RouteImpl(10, List.of(City.SYD, City.MEL), departure);
        route.setSchedule(List.of(departure, eta));

        Truck mockTruck = mock(Truck.class);
        when(mockTruck.getCapacityKg()).thenReturn(25000.0);
        route.assignTruck(mockTruck);

        when(mockPackageRepo.findPackageById(1)).thenReturn(pkg);
        when(mockRouteRepo.findRouteById(10)).thenReturn(route);

        assertThrows(InvalidUserInputException.class, () -> strategy.assignPackage(1, 10));
    }

    @Test
    public void assignPackage_Should_Throw_WhenExceedingMaxRouteCapacityWithoutTruck() {
        // Arrange
        DeliveryPackageImpl firstPkg = new DeliveryPackageImpl(1, City.SYD, City.MEL, 30000.0, "First");
        DeliveryPackageImpl secondPkg = new DeliveryPackageImpl(2, City.SYD, City.MEL, 15000.0, "Second");

        RouteImpl route = new RouteImpl(10, List.of(City.SYD, City.MEL), departure);
        route.setSchedule(List.of(departure, eta));
        route.assignPackage(firstPkg);

        when(mockPackageRepo.findPackageById(2)).thenReturn(secondPkg);
        when(mockRouteRepo.findRouteById(10)).thenReturn(route);

        // Act + Assert
        assertThrows(InvalidUserInputException.class, () -> strategy.assignPackage(2, 10));
    }
}
