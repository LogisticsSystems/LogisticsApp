package com.company.logistics.tests.infrastructure.loading.distances.implementation;

import com.company.logistics.enums.City;
import com.company.logistics.infrastructure.loading.distances.implementation.DefaultDistanceLoader;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DefaultDistanceLoaderTest {
    @Test
    public void loadDistances_Should_ContainCorrectSymmetricValues() {
        DefaultDistanceLoader loader = new DefaultDistanceLoader();
        Map<City, Map<City, Integer>> distances = loader.loadDistances();

        assertEquals(877, distances.get(City.SYD).get(City.MEL));
        assertEquals(877, distances.get(City.MEL).get(City.SYD));

        assertEquals(1376, distances.get(City.SYD).get(City.ADL));
        assertEquals(1376, distances.get(City.ADL).get(City.SYD));

        assertEquals(2762, distances.get(City.SYD).get(City.ASP));
        assertEquals(2762, distances.get(City.ASP).get(City.SYD));

        assertEquals(909, distances.get(City.SYD).get(City.BRI));
        assertEquals(909, distances.get(City.BRI).get(City.SYD));

        assertEquals(3935, distances.get(City.SYD).get(City.DAR));
        assertEquals(3935, distances.get(City.DAR).get(City.SYD));

        assertEquals(4016, distances.get(City.SYD).get(City.PER));
        assertEquals(4016, distances.get(City.PER).get(City.SYD));

        assertEquals(725, distances.get(City.MEL).get(City.ADL));
        assertEquals(725, distances.get(City.ADL).get(City.MEL));

        assertEquals(2255, distances.get(City.MEL).get(City.ASP));
        assertEquals(2255, distances.get(City.ASP).get(City.MEL));

        assertEquals(1765, distances.get(City.MEL).get(City.BRI));
        assertEquals(1765, distances.get(City.BRI).get(City.MEL));

        assertEquals(3752, distances.get(City.MEL).get(City.DAR));
        assertEquals(3752, distances.get(City.DAR).get(City.MEL));

        assertEquals(3509, distances.get(City.MEL).get(City.PER));
        assertEquals(3509, distances.get(City.PER).get(City.MEL));

        assertEquals(1530, distances.get(City.ADL).get(City.ASP));
        assertEquals(1530, distances.get(City.ASP).get(City.ADL));

        assertEquals(1927, distances.get(City.ADL).get(City.BRI));
        assertEquals(1927, distances.get(City.BRI).get(City.ADL));

        assertEquals(3027, distances.get(City.ADL).get(City.DAR));
        assertEquals(3027, distances.get(City.DAR).get(City.ADL));

        assertEquals(2785, distances.get(City.ADL).get(City.PER));
        assertEquals(2785, distances.get(City.PER).get(City.ADL));

        assertEquals(2993, distances.get(City.ASP).get(City.BRI));
        assertEquals(2993, distances.get(City.BRI).get(City.ASP));

        assertEquals(1497, distances.get(City.ASP).get(City.DAR));
        assertEquals(1497, distances.get(City.DAR).get(City.ASP));

        assertEquals(2481, distances.get(City.ASP).get(City.PER));
        assertEquals(2481, distances.get(City.PER).get(City.ASP));

        assertEquals(3426, distances.get(City.BRI).get(City.DAR));
        assertEquals(3426, distances.get(City.DAR).get(City.BRI));

        assertEquals(4311, distances.get(City.BRI).get(City.PER));
        assertEquals(4311, distances.get(City.PER).get(City.BRI));

        assertEquals(4025, distances.get(City.DAR).get(City.PER));
        assertEquals(4025, distances.get(City.PER).get(City.DAR));
    }
}
