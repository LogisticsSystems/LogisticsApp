package com.company.logistics.tests.services.speeds;

import com.company.logistics.enums.SpeedModelType;
import com.company.logistics.services.routing.scheduling.RouteScheduleService;
import com.company.logistics.services.speeds.SpeedModelService;
import com.company.logistics.services.speeds.contract.SpeedModel;
import com.company.logistics.services.speeds.implementation.ConstantSpeedModel;
import com.company.logistics.services.speeds.implementation.SeasonalSpeedModel;
import com.company.logistics.services.speeds.implementation.SinusoidalSpeedModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class SpeedModelServiceTest {
    private SpeedModel mockModel;

    @BeforeEach
    public void setUp() {
        mockModel = mock(SpeedModel.class);
    }

    @Test
    public void constructor_Should_SetInitialSpeedModel() {
        SpeedModelService service = new SpeedModelService(mockModel);
        assertEquals(mockModel, service.getSpeedModel());
    }

    @Test
    public void changeModel_Should_SetConstantSpeedModel_WhenTypeIsConstant() {
        SpeedModelService service = new SpeedModelService(mockModel);
        service.changeModel(SpeedModelType.CONSTANT);

        assertInstanceOf(ConstantSpeedModel.class, service.getSpeedModel());
    }

    @Test
    public void changeModel_Should_SetSeasonalSpeedModel_WhenTypeIsSeasonal() {
        SpeedModelService service = new SpeedModelService(mockModel);
        service.changeModel(SpeedModelType.SEASONAL);

        assertInstanceOf(SeasonalSpeedModel.class, service.getSpeedModel());
    }

    @Test
    public void changeModel_Should_SetSinusoidalSpeedModel_WhenTypeIsSinusoidal() {
        SpeedModelService service = new SpeedModelService(mockModel);
        service.changeModel(SpeedModelType.SINUSOIDAL);

        assertInstanceOf(SinusoidalSpeedModel.class, service.getSpeedModel());
    }

    @Test
    public void getRouteScheduler_Should_ReturnRouteScheduleService_WithCurrentModel() {
        SpeedModelService service = new SpeedModelService(mockModel);
        RouteScheduleService scheduler = service.getRouteScheduler();

        assertNotNull(scheduler);
    }
}
