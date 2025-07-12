package com.company.logistics.tests.infrastructure;

import com.company.logistics.enums.City;
import com.company.logistics.exceptions.InvalidUserInputException;
import com.company.logistics.infrastructure.DistanceMap;
import com.company.logistics.infrastructure.loading.distances.contracts.DistanceLoader;
import com.company.logistics.utils.ErrorMessages;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DistanceMapTest {
    private DistanceLoader mockLoader;
    private Map<City, Map<City, Integer>> mockDistances;

    @BeforeEach
    public void setUp() throws Exception {
        // Reset singleton manually via reflection
        java.lang.reflect.Field instance = DistanceMap.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null, null);

        mockLoader = mock(DistanceLoader.class);

        // Example distance map: SYD -> MEL = 800
        Map<City, Integer> toMap = new EnumMap<>(City.class);
        toMap.put(City.MEL, 800);

        mockDistances = new EnumMap<>(City.class);
        mockDistances.put(City.SYD, toMap);

        when(mockLoader.loadDistances()).thenReturn(mockDistances);
    }

    @Test
    public void initialize_Should_SetInstance_When_ValidLoaderProvided() {
        assertDoesNotThrow(() -> DistanceMap.initialize(mockLoader));
    }

    @Test
    public void getInstance_Should_Throw_When_NotInitialized() {
        InvalidUserInputException ex = assertThrows(
                InvalidUserInputException.class,
                DistanceMap::getInstance
        );
        assertEquals(
                String.format(ErrorMessages.NOT_INITIALIZED, "DistanceMap"),
                ex.getMessage()
        );
    }

    @Test
    public void initialize_Should_Throw_When_AlreadyInitialized() {
        DistanceMap.initialize(mockLoader);
        InvalidUserInputException ex = assertThrows(
                InvalidUserInputException.class,
                () -> DistanceMap.initialize(mockLoader)
        );
        assertEquals(
                String.format(ErrorMessages.ALREADY_INITIALIZED, "DistanceMap"),
                ex.getMessage()
        );
    }

    @Test
    public void getDistance_Should_ReturnDistance_When_ValidCitiesProvided() {
        DistanceMap.initialize(mockLoader);
        DistanceMap instance = DistanceMap.getInstance();

        int result = instance.getDistance(City.SYD, City.MEL);

        assertEquals(800, result);
    }

    @Test
    public void getDistance_Should_Throw_When_FromCityMissing() {
        DistanceMap.initialize(mockLoader);
        DistanceMap instance = DistanceMap.getInstance();

        InvalidUserInputException ex = assertThrows(
                InvalidUserInputException.class,
                () -> instance.getDistance(City.BRI, City.MEL)
        );

        assertEquals(
                String.format(ErrorMessages.UNKNOWN_FROM_CITY, City.BRI),
                ex.getMessage()
        );
    }

    @Test
    public void getDistance_Should_Throw_When_ToCityMissing() {
        DistanceMap.initialize(mockLoader);
        DistanceMap instance = DistanceMap.getInstance();

        InvalidUserInputException ex = assertThrows(
                InvalidUserInputException.class,
                () -> instance.getDistance(City.SYD, City.BRI)
        );

        assertEquals(
                String.format(ErrorMessages.NO_DISTANCE_DEFINED, City.SYD, City.BRI),
                ex.getMessage()
        );
    }

    @Test
    public void isValidCity_Should_ReturnTrue_When_CityExists() {
        DistanceMap.initialize(mockLoader);
        DistanceMap instance = DistanceMap.getInstance();

        assertTrue(instance.isValidCity(City.SYD));
    }

    @Test
    public void isValidCity_Should_ReturnFalse_When_CityDoesNotExist() {
        DistanceMap.initialize(mockLoader);
        DistanceMap instance = DistanceMap.getInstance();

        assertFalse(instance.isValidCity(City.BRI));
    }

    @Test
    public void isValidCity_Should_Throw_When_CityIsNull() {
        DistanceMap.initialize(mockLoader);
        DistanceMap instance = DistanceMap.getInstance();

        InvalidUserInputException ex = assertThrows(
                InvalidUserInputException.class,
                () -> instance.isValidCity(null)
        );

        assertEquals(
                String.format(ErrorMessages.NOT_NULL, "City"),
                ex.getMessage()
        );
    }
}
