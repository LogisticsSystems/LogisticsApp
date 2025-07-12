package com.company.logistics.tests.infrastructure.loading.speeds.implementation;

import com.company.logistics.enums.City;
import com.company.logistics.infrastructure.loading.speeds.implementation.DefaultSpeedLoader;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DefaultSpeedLoaderTest {
    @Test
    public void loadBaseSpeeds_Should_ReturnCorrectSymmetricSpeeds() {
        DefaultSpeedLoader loader = new DefaultSpeedLoader();
        Map<City, Map<City, Double>> speeds = loader.loadBaseSpeeds();

        assertEquals(90.0, speeds.get(City.SYD).get(City.MEL));
        assertEquals(90.0, speeds.get(City.MEL).get(City.SYD));

        assertEquals(85.0, speeds.get(City.SYD).get(City.ADL));
        assertEquals(85.0, speeds.get(City.ADL).get(City.SYD));

        assertEquals(80.0, speeds.get(City.SYD).get(City.ASP));
        assertEquals(80.0, speeds.get(City.ASP).get(City.SYD));

        assertEquals(88.0, speeds.get(City.SYD).get(City.BRI));
        assertEquals(88.0, speeds.get(City.BRI).get(City.SYD));

        assertEquals(75.0, speeds.get(City.SYD).get(City.DAR));
        assertEquals(75.0, speeds.get(City.DAR).get(City.SYD));

        assertEquals(78.0, speeds.get(City.SYD).get(City.PER));
        assertEquals(78.0, speeds.get(City.PER).get(City.SYD));

        assertEquals(88.0, speeds.get(City.MEL).get(City.ADL));
        assertEquals(88.0, speeds.get(City.ADL).get(City.MEL));

        assertEquals(82.0, speeds.get(City.MEL).get(City.ASP));
        assertEquals(82.0, speeds.get(City.ASP).get(City.MEL));

        assertEquals(83.0, speeds.get(City.MEL).get(City.BRI));
        assertEquals(83.0, speeds.get(City.BRI).get(City.MEL));

        assertEquals(76.0, speeds.get(City.MEL).get(City.DAR));
        assertEquals(76.0, speeds.get(City.DAR).get(City.MEL));

        assertEquals(79.0, speeds.get(City.MEL).get(City.PER));
        assertEquals(79.0, speeds.get(City.PER).get(City.MEL));

        assertEquals(84.0, speeds.get(City.ADL).get(City.ASP));
        assertEquals(84.0, speeds.get(City.ASP).get(City.ADL));

        assertEquals(80.0, speeds.get(City.ADL).get(City.BRI));
        assertEquals(80.0, speeds.get(City.BRI).get(City.ADL));

        assertEquals(78.0, speeds.get(City.ADL).get(City.DAR));
        assertEquals(78.0, speeds.get(City.DAR).get(City.ADL));

        assertEquals(77.0, speeds.get(City.ADL).get(City.PER));
        assertEquals(77.0, speeds.get(City.PER).get(City.ADL));

        assertEquals(86.0, speeds.get(City.ASP).get(City.BRI));
        assertEquals(86.0, speeds.get(City.BRI).get(City.ASP));

        assertEquals(74.0, speeds.get(City.ASP).get(City.DAR));
        assertEquals(74.0, speeds.get(City.DAR).get(City.ASP));

        assertEquals(76.0, speeds.get(City.ASP).get(City.PER));
        assertEquals(76.0, speeds.get(City.PER).get(City.ASP));

        assertEquals(73.0, speeds.get(City.BRI).get(City.DAR));
        assertEquals(73.0, speeds.get(City.DAR).get(City.BRI));

        assertEquals(75.0, speeds.get(City.BRI).get(City.PER));
        assertEquals(75.0, speeds.get(City.PER).get(City.BRI));

        assertEquals(72.0, speeds.get(City.DAR).get(City.PER));
        assertEquals(72.0, speeds.get(City.PER).get(City.DAR));
    }
}
