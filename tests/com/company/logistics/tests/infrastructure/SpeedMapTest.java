package com.company.logistics.tests.infrastructure;

import com.company.logistics.enums.City;
import com.company.logistics.exceptions.InvalidUserInputException;
import com.company.logistics.infrastructure.SpeedMap;
import com.company.logistics.infrastructure.loading.speeds.contracts.SpeedLoader;
import com.company.logistics.utils.ErrorMessages;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SpeedMapTest {
    private SpeedLoader mockLoader;
    private Map<City, Map<City, Double>> mockSpeeds;

    @BeforeEach
    public void setUp() throws Exception {
        // Reset singleton manually via reflection
        java.lang.reflect.Field instance = SpeedMap.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null, null);

        mockLoader = mock(SpeedLoader.class);

        // Example speeds: SYD -> MEL = 95.0
        Map<City, Double> toMap = new EnumMap<>(City.class);
        toMap.put(City.MEL, 95.0);

        mockSpeeds = new EnumMap<>(City.class);
        mockSpeeds.put(City.SYD, toMap);

        when(mockLoader.loadBaseSpeeds()).thenReturn(mockSpeeds);
    }

    @Test
    public void initialize_Should_SetInstance_When_ValidLoaderProvided() {
        assertDoesNotThrow(() -> SpeedMap.initialize(mockLoader));
    }

    @Test
    public void getInstance_Should_Throw_When_NotInitialized() {
        InvalidUserInputException ex = assertThrows(
                InvalidUserInputException.class,
                SpeedMap::getInstance
        );

        assertEquals(
                String.format(ErrorMessages.NOT_INITIALIZED, "SpeedMap"),
                ex.getMessage()
        );
    }

    @Test
    public void initialize_Should_Throw_When_AlreadyInitialized() {
        SpeedMap.initialize(mockLoader);

        InvalidUserInputException ex = assertThrows(
                InvalidUserInputException.class,
                () -> SpeedMap.initialize(mockLoader)
        );

        assertEquals(
                String.format(ErrorMessages.ALREADY_INITIALIZED, "SpeedMap"),
                ex.getMessage()
        );
    }

    @Test
    public void getBaseSpeed_Should_ReturnSpeed_When_ValidCitiesProvided() {
        SpeedMap.initialize(mockLoader);
        SpeedMap instance = SpeedMap.getInstance();

        double result = instance.getBaseSpeed(City.SYD, City.MEL);

        assertEquals(95.0, result);
    }

    @Test
    public void getBaseSpeed_Should_Throw_When_FromCityMissing() {
        SpeedMap.initialize(mockLoader);
        SpeedMap instance = SpeedMap.getInstance();

        InvalidUserInputException ex = assertThrows(
                InvalidUserInputException.class,
                () -> instance.getBaseSpeed(City.BRI, City.MEL)
        );

        assertEquals(
                String.format(ErrorMessages.UNKNOWN_FROM_CITY, City.BRI),
                ex.getMessage()
        );
    }

    @Test
    public void getBaseSpeed_Should_Throw_When_ToCityMissing() {
        SpeedMap.initialize(mockLoader);
        SpeedMap instance = SpeedMap.getInstance();

        InvalidUserInputException ex = assertThrows(
                InvalidUserInputException.class,
                () -> instance.getBaseSpeed(City.SYD, City.BRI)
        );

        assertEquals(
                String.format(ErrorMessages.NO_SPEED_DEFINED, City.SYD, City.BRI),
                ex.getMessage()
        );
    }
}
