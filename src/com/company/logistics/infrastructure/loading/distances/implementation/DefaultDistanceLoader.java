package com.company.logistics.infrastructure.loading.distances.implementation;

import com.company.logistics.enums.City;
import com.company.logistics.infrastructure.loading.distances.contracts.DistanceLoader;
import com.company.logistics.infrastructure.loading.distances.helper.DistanceMapHelper;

import java.util.EnumMap;
import java.util.Map;

public class DefaultDistanceLoader implements DistanceLoader {
    @Override
    public Map<City, Map<City, Integer>> loadDistances() {
        Map<City, Map<City, Integer>> distances = new EnumMap<>(City.class);

        DistanceMapHelper.addSymmetric(distances, City.SYD, City.MEL, 877);
        DistanceMapHelper.addSymmetric(distances, City.SYD, City.ADL, 1376);
        DistanceMapHelper.addSymmetric(distances, City.SYD, City.ASP, 2762);
        DistanceMapHelper.addSymmetric(distances, City.SYD, City.BRI, 909);
        DistanceMapHelper.addSymmetric(distances, City.SYD, City.DAR, 3935);
        DistanceMapHelper.addSymmetric(distances, City.SYD, City.PER, 4016);

        DistanceMapHelper.addSymmetric(distances, City.MEL, City.ADL, 725);
        DistanceMapHelper.addSymmetric(distances, City.MEL, City.ASP, 2255);
        DistanceMapHelper.addSymmetric(distances, City.MEL, City.BRI, 1765);
        DistanceMapHelper.addSymmetric(distances, City.MEL, City.DAR, 3752);
        DistanceMapHelper.addSymmetric(distances, City.MEL, City.PER, 3509);

        DistanceMapHelper.addSymmetric(distances, City.ADL, City.ASP, 1530);
        DistanceMapHelper.addSymmetric(distances, City.ADL, City.BRI, 1927);
        DistanceMapHelper.addSymmetric(distances, City.ADL, City.DAR, 3027);
        DistanceMapHelper.addSymmetric(distances, City.ADL, City.PER, 2785);

        DistanceMapHelper.addSymmetric(distances, City.ASP, City.BRI, 2993);
        DistanceMapHelper.addSymmetric(distances, City.ASP, City.DAR, 1497);
        DistanceMapHelper.addSymmetric(distances, City.ASP, City.PER, 2481);

        DistanceMapHelper.addSymmetric(distances, City.BRI, City.DAR, 3426);
        DistanceMapHelper.addSymmetric(distances, City.BRI, City.PER, 4311);

        DistanceMapHelper.addSymmetric(distances, City.DAR, City.PER, 4025);

        return distances;
    }
}
