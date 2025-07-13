package com.company.logistics.tests.core.factory;

import com.company.logistics.core.contracts.Engine;
import com.company.logistics.core.factory.EngineFactory;
import com.company.logistics.infrastructure.DistanceMap;
import com.company.logistics.infrastructure.SpeedMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

public class EngineFactoryTest {

    @BeforeEach
    public void resetInfrastructureSingletons() throws Exception {
        // Reset DistanceMap singleton
        Field distanceInstance = DistanceMap.class.getDeclaredField("instance");
        distanceInstance.setAccessible(true);
        distanceInstance.set(null, null);

        // Reset SpeedMap singleton
        Field speedInstance = SpeedMap.class.getDeclaredField("instance");
        speedInstance.setAccessible(true);
        speedInstance.set(null, null);
    }

    @Test
    public void create_Should_ReturnValidEngineInstance() {
        // Act
        Engine engine = EngineFactory.create();

        // Assert
        assertNotNull(engine);
        assertEquals("EngineImpl", engine.getClass().getSimpleName());
    }

    @Test
    public void create_Should_InitializeDistanceMapAndSpeedMap() {
        // Act
        EngineFactory.create();

        // Assert
        assertNotNull(DistanceMap.getInstance(), "DistanceMap should be initialized");
        assertNotNull(SpeedMap.getInstance(), "SpeedMap should be initialized");
    }
}
