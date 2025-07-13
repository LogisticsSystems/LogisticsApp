package com.company.logistics.tests.services.routing.computing;

import com.company.logistics.enums.City;
import com.company.logistics.models.contracts.DeliveryPackage;
import com.company.logistics.models.contracts.Route;
import com.company.logistics.repositories.contracts.RouteRepository;
import com.company.logistics.services.routing.computing.RouteRecalculatorService;
import com.company.logistics.services.routing.scheduling.RouteScheduleService;
import com.company.logistics.services.speeds.SpeedModelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;

public class RouteRecalculatorServiceTest {
    private RouteRepository mockRouteRepository;
    private SpeedModelService mockSpeedModelService;
    private RouteScheduleService mockScheduler;
    private Route mockRoute;
    private DeliveryPackage mockPackage1;
    private DeliveryPackage mockPackage2;

    private RouteRecalculatorService service;

    @BeforeEach
    public void setUp() {
        mockRouteRepository = mock(RouteRepository.class);
        mockSpeedModelService = mock(SpeedModelService.class);
        mockScheduler = mock(RouteScheduleService.class);
        mockRoute = mock(Route.class);
        mockPackage1 = mock(DeliveryPackage.class);
        mockPackage2 = mock(DeliveryPackage.class);

        service = new RouteRecalculatorService(mockRouteRepository, mockSpeedModelService);
    }

    @Test
    public void recomputeAll_Should_UpdateSchedule_And_ETA_ForPackages() {
        // Arrange
        LocalDateTime departure = LocalDateTime.of(2024, 1, 1, 8, 0);
        List<City> locations = List.of(City.SYD, City.MEL, City.BRI);
        List<LocalDateTime> newSchedule = List.of(
                departure,
                departure.plusHours(2),
                departure.plusHours(4)
        );

        when(mockSpeedModelService.getRouteScheduler()).thenReturn(mockScheduler);
        when(mockRouteRepository.getRoutes()).thenReturn(List.of(mockRoute));
        when(mockRoute.getLocations()).thenReturn(locations);
        when(mockRoute.getDepartureTime()).thenReturn(departure);
        when(mockRoute.getAssignedPackages()).thenReturn(List.of(mockPackage1, mockPackage2));
        when(mockScheduler.computeSchedule(locations, departure)).thenReturn(newSchedule);
        when(mockPackage1.getEndLocation()).thenReturn(City.BRI);
        when(mockPackage2.getEndLocation()).thenReturn(City.MEL);
        when(mockScheduler.getEtaForCity(City.BRI, locations, newSchedule)).thenReturn(newSchedule.get(2));
        when(mockScheduler.getEtaForCity(City.MEL, locations, newSchedule)).thenReturn(newSchedule.get(1));

        // Act
        service.recomputeAll();

        // Assert
        verify(mockRoute).setSchedule(newSchedule);
        verify(mockPackage1).setExpectedArrival(newSchedule.get(2));
        verify(mockPackage2).setExpectedArrival(newSchedule.get(1));
    }
}
