package com.company.logistics.tests.services.delivery;

import com.company.logistics.dto.PackageSnapshot;
import com.company.logistics.enums.City;
import com.company.logistics.enums.PackageStatus;
import com.company.logistics.exceptions.InvalidUserInputException;
import com.company.logistics.models.contracts.Truck;
import com.company.logistics.models.delivery.DeliveryPackageImpl;
import com.company.logistics.models.delivery.RouteImpl;
import com.company.logistics.repositories.contracts.PackageRepository;
import com.company.logistics.repositories.contracts.RouteRepository;
import com.company.logistics.services.delivery.PackageDeliveryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PackageDeliveryServiceTest {
    private PackageRepository mockPackageRepo;
    private RouteRepository mockRouteRepo;
    private PackageDeliveryService deliveryService;

    private LocalDateTime departure;
    private LocalDateTime eta;

    @BeforeEach
    public void setup() {
        mockPackageRepo = mock(PackageRepository.class);
        mockRouteRepo = mock(RouteRepository.class);
        deliveryService = new PackageDeliveryService(mockPackageRepo, mockRouteRepo);

        departure = LocalDateTime.of(2024, 1, 1, 8, 0);
        eta = departure.plusHours(2);
    }

    @Test
    public void deliverPackage_Should_Succeed_AndUnassignTruck_WhenLastPackage() {
        // Arrange
        DeliveryPackageImpl pkg = new DeliveryPackageImpl(1, City.SYD, City.MEL, 1000, "ABCDEF");
        pkg.advancePackageStatus();
        pkg.advancePackageStatus();

        RouteImpl route = new RouteImpl(10, List.of(City.SYD, City.MEL), departure);
        route.setSchedule(List.of(departure, eta));
        route.assignPackage(pkg);

        Truck truck = mock(Truck.class);
        route.assignTruck(truck);

        when(mockPackageRepo.findPackageById(1)).thenReturn(pkg);
        when(mockRouteRepo.findRouteByPackageId(1)).thenReturn(route);

        // Act
        PackageSnapshot snapshot = deliveryService.deliverPackage(1);

        // Assert
        assertEquals(PackageStatus.DELIVERED, snapshot.status());
        assertEquals(0, route.getAssignedPackages().size());
        assertTrue(route.getAssignedTruck().isEmpty());
        verify(truck).unassignFromRoute();
    }

    @Test
    public void deliverPackage_Should_Succeed_WithoutUnassigningTruck_WhenOtherPackagesRemain() {
        // Arrange
        DeliveryPackageImpl delivered = new DeliveryPackageImpl(1, City.SYD, City.MEL, 1000, "ABCDEF");
        delivered.advancePackageStatus();
        delivered.advancePackageStatus();

        DeliveryPackageImpl stillInTransit = new DeliveryPackageImpl(2, City.SYD, City.MEL, 1000, "ABCDEF");
        stillInTransit.advancePackageStatus();
        stillInTransit.advancePackageStatus();

        RouteImpl route = new RouteImpl(10, List.of(City.SYD, City.MEL), departure);
        route.setSchedule(List.of(departure, eta));
        route.assignPackage(delivered);
        route.assignPackage(stillInTransit);

        Truck truck = mock(Truck.class);
        route.assignTruck(truck);

        when(mockPackageRepo.findPackageById(1)).thenReturn(delivered);
        when(mockRouteRepo.findRouteByPackageId(1)).thenReturn(route);

        // Act
        PackageSnapshot snapshot = deliveryService.deliverPackage(1);

        // Assert
        assertEquals(PackageStatus.DELIVERED, snapshot.status());
        assertEquals(1, route.getAssignedPackages().size());
        assertTrue(route.getAssignedTruck().isPresent());
        verify(truck, never()).unassignFromRoute();
    }

    @Test
    public void deliverPackage_Should_Throw_WhenStatusIsNotInTransit() {
        // Arrange
        DeliveryPackageImpl pkg = new DeliveryPackageImpl(1, City.SYD, City.MEL, 1000, "ABCDEF");

        when(mockPackageRepo.findPackageById(1)).thenReturn(pkg);

        // Assert
        assertThrows(InvalidUserInputException.class, () -> deliveryService.deliverPackage(1));
    }
}
