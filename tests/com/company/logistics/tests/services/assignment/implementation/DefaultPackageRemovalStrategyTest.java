package com.company.logistics.tests.services.assignment.implementation;

import com.company.logistics.dto.PackageSnapshot;
import com.company.logistics.enums.City;
import com.company.logistics.enums.PackageStatus;
import com.company.logistics.exceptions.InvalidUserInputException;
import com.company.logistics.models.delivery.DeliveryPackageImpl;
import com.company.logistics.models.delivery.RouteImpl;
import com.company.logistics.repositories.contracts.PackageRepository;
import com.company.logistics.repositories.contracts.RouteRepository;
import com.company.logistics.services.assignment.strategy.implementation.DefaultPackageRemovalStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DefaultPackageRemovalStrategyTest {
    private PackageRepository mockPackageRepo;
    private RouteRepository mockRouteRepo;
    private DefaultPackageRemovalStrategy strategy;

    private LocalDateTime departure;
    private LocalDateTime eta;

    @BeforeEach
    public void setup() {
        mockPackageRepo = mock(PackageRepository.class);
        mockRouteRepo = mock(RouteRepository.class);
        strategy = new DefaultPackageRemovalStrategy(mockPackageRepo, mockRouteRepo);

        departure = LocalDateTime.of(2024, 1, 1, 8, 0);
        eta = departure.plusHours(2);
    }

    @Test
    public void removePackage_Should_RevertStatusAndRemoveFromRoute() {
        // Arrange
        DeliveryPackageImpl deliveryPackage = new DeliveryPackageImpl(1, City.SYD, City.MEL, 1000, "John_Snow");
        deliveryPackage.advancePackageStatus();

        RouteImpl route = new RouteImpl(10, List.of(City.SYD, City.MEL), departure);
        route.setSchedule(List.of(departure, eta));
        route.assignPackage(deliveryPackage);

        when(mockPackageRepo.findPackageById(1)).thenReturn(deliveryPackage);
        when(mockRouteRepo.findRouteById(10)).thenReturn(route);

        // Act
        PackageSnapshot snapshot = strategy.removePackage(1, 10);

        // Assert
        assertEquals(1, snapshot.id());
        assertEquals(PackageStatus.UNASSIGNED, snapshot.status());
        assertNull(snapshot.eta());
        assertTrue(route.getAssignedPackages().isEmpty());
    }

    @Test
    public void removePackage_Should_Throw_WhenPackageNotInRoute() {
        // Arrange
        DeliveryPackageImpl deliveryPackage = new DeliveryPackageImpl(1, City.SYD, City.MEL, 1000, "John_Snow");

        RouteImpl route = new RouteImpl(10, List.of(City.SYD, City.MEL), departure);
        route.setSchedule(List.of(departure, eta));

        when(mockPackageRepo.findPackageById(1)).thenReturn(deliveryPackage);
        when(mockRouteRepo.findRouteById(10)).thenReturn(route);

        // Assert
        assertThrows(InvalidUserInputException.class, () -> strategy.removePackage(1, 10));
    }

    @Test
    public void removePackage_Should_RevertInTransitPackage_AllTheWayToUnassigned() {
        // Arrange
        DeliveryPackageImpl deliveryPackage = new DeliveryPackageImpl(1, City.SYD, City.MEL, 1000, "John_Snow");
        deliveryPackage.advancePackageStatus();
        deliveryPackage.advancePackageStatus();

        RouteImpl route = new RouteImpl(10, List.of(City.SYD, City.MEL), departure);
        route.setSchedule(List.of(departure, eta));
        route.assignPackage(deliveryPackage);

        when(mockPackageRepo.findPackageById(1)).thenReturn(deliveryPackage);
        when(mockRouteRepo.findRouteById(10)).thenReturn(route);

        // Act
        PackageSnapshot snapshot = strategy.removePackage(1, 10);

        // Assert
        assertEquals(PackageStatus.UNASSIGNED, snapshot.status());
        assertTrue(route.getAssignedPackages().isEmpty());
    }
}
