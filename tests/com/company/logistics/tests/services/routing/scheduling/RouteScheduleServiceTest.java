package com.company.logistics.tests.services.routing.scheduling;

import com.company.logistics.enums.City;
import com.company.logistics.exceptions.InvalidUserInputException;
import com.company.logistics.infrastructure.DistanceMap;
import com.company.logistics.infrastructure.loading.distances.contracts.DistanceLoader;
import com.company.logistics.services.routing.scheduling.RouteScheduleService;
import com.company.logistics.services.speeds.contract.SpeedModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RouteScheduleServiceTest {
    private DistanceLoader mockDistanceLoader;
    private SpeedModel mockSpeedModel;
    private LocalDateTime departure;

    @BeforeEach
    public void setUp() throws Exception {
        Field instance = DistanceMap.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null, null);

        mockDistanceLoader = mock(DistanceLoader.class);
        mockSpeedModel = mock(SpeedModel.class);
        departure = LocalDateTime.of(2024, 1, 1, 8, 0);

        Map<City, Map<City, Integer>> distances = new EnumMap<>(City.class);
        Map<City, Integer> toMap = new EnumMap<>(City.class);
        toMap.put(City.MEL, 600);
        distances.put(City.SYD, toMap);

        Map<City, Integer> toMap2 = new EnumMap<>(City.class);
        toMap2.put(City.ADL, 700);
        distances.put(City.MEL, toMap2);

        when(mockDistanceLoader.loadDistances()).thenReturn(distances);
        DistanceMap.initialize(mockDistanceLoader);
    }

    @Test
    public void computeSchedule_Should_CalculateAccurateTimes() {
        when(mockSpeedModel.getSpeed(City.SYD, City.MEL, departure)).thenReturn(100.0);
        LocalDateTime afterFirstLeg = departure.plusMinutes((long) ((600.0 / 100.0) * 60));

        when(mockSpeedModel.getSpeed(City.MEL, City.ADL, afterFirstLeg)).thenReturn(70.0);

        RouteScheduleService scheduler = new RouteScheduleService(mockSpeedModel);
        List<City> route = Arrays.asList(City.SYD, City.MEL, City.ADL);

        List<LocalDateTime> schedule = scheduler.computeSchedule(route, departure);

        assertEquals(3, schedule.size());
        assertEquals(departure, schedule.get(0));
        assertEquals(afterFirstLeg, schedule.get(1));

        long secondLegMinutes = Math.round((700.0 / 70.0) * 60);
        assertEquals(afterFirstLeg.plusMinutes(secondLegMinutes), schedule.get(2));
    }

    @Test
    public void getEtaForCity_Should_ReturnCorrectArrivalTime() {
        RouteScheduleService scheduler = new RouteScheduleService(mockSpeedModel);

        List<City> route = Arrays.asList(City.SYD, City.MEL, City.ADL);
        List<LocalDateTime> schedule = Arrays.asList(
                departure,
                departure.plusHours(6),
                departure.plusHours(16)
        );

        LocalDateTime eta = scheduler.getEtaForCity(City.MEL, route, schedule);
        assertEquals(departure.plusHours(6), eta);
    }

    @Test
    public void getEtaForCity_Should_Throw_When_CityNotInRoute() {
        RouteScheduleService scheduler = new RouteScheduleService(mockSpeedModel);

        List<City> route = Arrays.asList(City.SYD, City.MEL);
        List<LocalDateTime> schedule = Arrays.asList(
                departure,
                departure.plusHours(6)
        );

        assertThrows(InvalidUserInputException.class,
                () -> scheduler.getEtaForCity(City.PER, route, schedule));
    }
}
