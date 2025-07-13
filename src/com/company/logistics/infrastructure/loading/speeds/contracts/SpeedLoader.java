package com.company.logistics.infrastructure.loading.speeds.contracts;

import com.company.logistics.enums.City;

import java.util.Map;

public interface SpeedLoader {

    Map<City, Map<City, Double>> loadBaseSpeeds();
}
