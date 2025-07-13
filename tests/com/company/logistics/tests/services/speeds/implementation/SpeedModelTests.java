package com.company.logistics.tests.services.speeds.implementation;

import com.company.logistics.enums.City;
import com.company.logistics.infrastructure.SpeedMap;
import com.company.logistics.infrastructure.loading.speeds.contracts.SpeedLoader;
import com.company.logistics.services.speeds.contract.SpeedModel;
import com.company.logistics.services.speeds.implementation.ConstantSpeedModel;
import com.company.logistics.services.speeds.implementation.SeasonalSpeedModel;
import com.company.logistics.services.speeds.implementation.SinusoidalSpeedModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.EnumMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SpeedModelTests {

    @BeforeEach
    public void resetSpeedMapSingleton() throws Exception {
        Field instance = SpeedMap.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null, null);

        SpeedLoader loader = mock(SpeedLoader.class);
        Map<City, Map<City, Double>> mockSpeeds = new EnumMap<>(City.class);
        Map<City, Double> toMap = new EnumMap<>(City.class);
        toMap.put(City.MEL, 100.0);
        mockSpeeds.put(City.SYD, toMap);

        when(loader.loadBaseSpeeds()).thenReturn(mockSpeeds);
        SpeedMap.initialize(loader);
    }

    @Test
    public void constantSpeedModel_Should_ReturnBaseSpeed() {
        SpeedModel model = new ConstantSpeedModel();
        double result = model.getSpeed(City.SYD, City.MEL, LocalDateTime.now());
        assertEquals(100.0, result);
    }

    @Test
    public void seasonalSpeedModel_Should_ApplySummerFactor() {
        SpeedModel model = new SeasonalSpeedModel();
        LocalDateTime summer = LocalDateTime.of(2024, Month.JANUARY, 15, 12, 0);
        double result = model.getSpeed(City.SYD, City.MEL, summer);
        assertEquals(110.0, result, 0.0001);
    }

    @Test
    public void seasonalSpeedModel_Should_ApplyWinterFactor() {
        SpeedModel model = new SeasonalSpeedModel();
        LocalDateTime winter = LocalDateTime.of(2024, Month.JUNE, 15, 12, 0);
        double result = model.getSpeed(City.SYD, City.MEL, winter);
        assertEquals(85.0, result, 0.0001);
    }

    @Test
    public void seasonalSpeedModel_Should_ApplyDefaultFactor() {
        SpeedModel model = new SeasonalSpeedModel();
        LocalDateTime spring = LocalDateTime.of(2024, Month.APRIL, 15, 12, 0);
        double result = model.getSpeed(City.SYD, City.MEL, spring);
        assertEquals(100.0, result, 0.0001);
    }

    @Test
    public void sinusoidalSpeedModel_Should_ReturnHigherSpeed_OnPeakDay() {
        SpeedModel model = new SinusoidalSpeedModel();
        LocalDateTime peak = LocalDateTime.of(2024, 1, 26, 12, 0);
        double result = model.getSpeed(City.SYD, City.MEL, peak);
        assertTrue(result > 100.0);
    }

    @Test
    public void sinusoidalSpeedModel_Should_ReturnLowerSpeed_WhenOutOfPhase() {
        SpeedModel model = new SinusoidalSpeedModel();
        LocalDateTime trough = LocalDateTime.of(2024, 7, 18, 12, 0);
        double result = model.getSpeed(City.SYD, City.MEL, trough);
        assertTrue(result < 100.0);
    }

    @Test
    public void sinusoidalSpeedModel_Should_ApplyCorrectSinusoidalAdjustment_OnPeakDay() {
        SpeedModel model = new SinusoidalSpeedModel();
        LocalDateTime peakDay = LocalDateTime.of(2024, 1, 26, 12, 0); // Day 26
        double expectedSpeed = calculateSinusoidalSpeed(100.0, peakDay);
        double actualSpeed = model.getSpeed(City.SYD, City.MEL, peakDay);
        assertEquals(expectedSpeed, actualSpeed, 0.0001);
    }

    @Test
    public void sinusoidalSpeedModel_Should_ApplyCorrectSinusoidalAdjustment_OnRandomDay() {
        SpeedModel model = new SinusoidalSpeedModel();
        LocalDateTime randomDay = LocalDateTime.of(2024, 4, 15, 12, 0); // Day 106
        double expectedSpeed = calculateSinusoidalSpeed(100.0, randomDay);
        double actualSpeed = model.getSpeed(City.SYD, City.MEL, randomDay);
        assertEquals(expectedSpeed, actualSpeed, 0.0001);
    }

    private double calculateSinusoidalSpeed(double base, LocalDateTime date) {
        int CYCLE_DAYS = 365;
        double AMPLITUDE = 10.0;
        int PEAK_DAY = 26;
        int CYCLE_SPLIT = 4;
        double PHASE_SHIFT = PEAK_DAY - ((double) CYCLE_DAYS / CYCLE_SPLIT);

        int dayOfYear = date.getDayOfYear();
        double theta = 2 * Math.PI * (dayOfYear - PHASE_SHIFT) / CYCLE_DAYS;
        return base + (AMPLITUDE * Math.sin(theta));
    }
}
