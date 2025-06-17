package com.company.logistics.infrastructure.loading.distances.contracts;

import com.company.logistics.enums.City;

import java.util.Map;

public interface DistanceLoader {

    Map<City, Map<City, Integer>> loadDistances();

}
