package com.company.logistics.tests.services.assignment;

import com.company.logistics.dto.PackageSnapshot;
import com.company.logistics.services.assignment.AssignmentService;
import com.company.logistics.services.assignment.strategy.PackageAssignmentStrategy;
import com.company.logistics.services.assignment.strategy.PackageRemovalStrategy;
import com.company.logistics.services.assignment.strategy.TruckAssignmentStrategy;
import com.company.logistics.services.assignment.strategy.TruckRemovalStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class AssignmentServiceTest {
    private PackageAssignmentStrategy mockPackageStrategy;
    private PackageRemovalStrategy mockPackageRemoval;
    private TruckAssignmentStrategy mockTruckStrategy;
    private TruckRemovalStrategy mockTruckRemoval;
    private AssignmentService service;

    @BeforeEach
    public void setUp() {
        mockPackageStrategy = mock(PackageAssignmentStrategy.class);
        mockPackageRemoval  = mock(PackageRemovalStrategy.class);
        mockTruckStrategy   = mock(TruckAssignmentStrategy.class);
        mockTruckRemoval    = mock(TruckRemovalStrategy.class);

        service = new AssignmentService(
                mockPackageStrategy,
                mockTruckStrategy,
                mockTruckRemoval,
                mockPackageRemoval
        );
    }

    @Test
    public void assignPackageToRoute_Should_DelegateToStrategyAndReturnSnapshot() {
        int pkgId = 1;
        int routeId = 2;
        PackageSnapshot expected = new PackageSnapshot(pkgId, null, LocalDateTime.now(),"");

        when(mockPackageStrategy.assignPackage(pkgId, routeId)).thenReturn(expected);

        PackageSnapshot actual = service.assignPackageToRoute(pkgId, routeId);

        assertEquals(expected, actual);
        verify(mockPackageStrategy, times(1)).assignPackage(pkgId, routeId);
    }

    @Test
    public void assignTruckToRoute_Should_DelegateToStrategyAndReturnList() {
        int truckId = 3;
        int routeId = 4;
        List<Integer> expected = List.of(10, 20);

        when(mockTruckStrategy.assignTruck(truckId, routeId)).thenReturn(expected);

        List<Integer> actual = service.assignTruckToRoute(truckId, routeId);

        assertEquals(expected, actual);
        verify(mockTruckStrategy, times(1)).assignTruck(truckId, routeId);
    }

    @Test
    public void removeTruckFromRoute_Should_DelegateToStrategyAndReturnList() {
        int truckId = 5;
        int routeId = 6;
        List<Integer> expected = List.of(15, 25);

        when(mockTruckRemoval.removeTruck(truckId, routeId)).thenReturn(expected);

        List<Integer> actual = service.removeTruckFromRoute(truckId, routeId);

        assertEquals(expected, actual);
        verify(mockTruckRemoval, times(1)).removeTruck(truckId, routeId);
    }

    @Test
    public void removePackageFromRoute_Should_DelegateToStrategyAndReturnSnapshot() {
        int packageId = 7;
        int routeId = 8;
        PackageSnapshot expected = new PackageSnapshot(packageId, null, LocalDateTime.now(),"");

        when(mockPackageRemoval.removePackage(packageId, routeId)).thenReturn(expected);

        PackageSnapshot actual = service.removePackageFromRoute(packageId, routeId);

        assertEquals(expected, actual);
        verify(mockPackageRemoval, times(1)).removePackage(packageId, routeId);
    }
}
