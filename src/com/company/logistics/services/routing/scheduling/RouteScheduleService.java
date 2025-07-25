package com.company.logistics.services.routing.scheduling;

import com.company.logistics.exceptions.InvalidUserInputException;
import com.company.logistics.services.speeds.contract.SpeedModel;
import com.company.logistics.enums.City;
import com.company.logistics.infrastructure.DistanceMap;
import com.company.logistics.utils.ErrorMessages;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RouteScheduleService {
    private static final int MINUTES_IN_HOUR = 60;

    private final SpeedModel speedModel;

    public RouteScheduleService(SpeedModel speedModel) {
        this.speedModel = speedModel;
    }

    public List<LocalDateTime> computeSchedule(List<City> stops, LocalDateTime departure) {
        List<LocalDateTime> schedule = new ArrayList<>();
        LocalDateTime current = departure;
        schedule.add(current);

        for (int i = 0; i < stops.size() - 1; i++) {
            City from = stops.get(i);
            City to   = stops.get(i + 1);

            int   distanceKm = DistanceMap.getInstance().getDistance(from, to);
            double avgSpeed  = speedModel.getSpeed(from, to, current);
            long  minutes    = Math.round((distanceKm / avgSpeed) * MINUTES_IN_HOUR);

            current = current.plusMinutes(minutes);
            schedule.add(current);
        }
        return schedule;
    }

    public LocalDateTime getEtaForCity(City city, List<City> stops, List<LocalDateTime> schedule) {
        int idx = stops.lastIndexOf(city);
        if (idx < 0) {
            throw new InvalidUserInputException(String.format(ErrorMessages.CITY_NOT_ON_ROUTE, city));
        }
        return schedule.get(idx);
    }
}
