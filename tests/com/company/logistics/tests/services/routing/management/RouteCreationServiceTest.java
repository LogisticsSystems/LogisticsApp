package com.company.logistics.tests.services.routing.management;

import com.company.logistics.enums.City;
import com.company.logistics.models.contracts.Route;
import com.company.logistics.repositories.contracts.RouteRepository;
import com.company.logistics.services.routing.management.RouteCreationService;
import com.company.logistics.services.routing.scheduling.RouteScheduleService;
import com.company.logistics.services.speeds.SpeedModelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class RouteCreationServiceTest {
    private RouteRepository mockRouteRepository;
    private SpeedModelService mockSpeedModelService;
    private RouteScheduleService mockScheduler;
    private RouteCreationService service;

    @BeforeEach
    public void setUp() {
        mockRouteRepository = mock(RouteRepository.class);
        mockSpeedModelService = mock(SpeedModelService.class);
        mockScheduler = mock(RouteScheduleService.class);

        when(mockSpeedModelService.getRouteScheduler()).thenReturn(mockScheduler);

        service = new RouteCreationService(mockRouteRepository, mockSpeedModelService);
    }

    @Test
    public void createRoute_Should_CreateRouteAndAssignSchedule() {
        // Arrange
        List<City> cities = List.of(City.SYD, City.MEL, City.BRI);
        LocalDateTime departure = LocalDateTime.of(2024, 7, 1, 8, 0);

        Route mockRoute = mock(Route.class);
        List<LocalDateTime> mockSchedule = List.of(
                departure,
                departure.plusHours(5),
                departure.plusHours(10)
        );

        when(mockRouteRepository.createRoute(cities, departure)).thenReturn(mockRoute);
        when(mockScheduler.computeSchedule(cities, departure)).thenReturn(mockSchedule);

        // Act
        Route result = service.createRoute(cities, departure);

        // Assert
        assertEquals(mockRoute, result);
        verify(mockRouteRepository).createRoute(cities, departure);
        verify(mockScheduler).computeSchedule(cities, departure);
        verify(mockRoute).setSchedule(mockSchedule);
    }
}
