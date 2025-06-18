package com.company.logistics.core.services.speeds;

import com.company.logistics.enums.City;

import java.time.LocalDateTime;

public interface SpeedService {

    double getSpeed(City from, City to, LocalDateTime departure);

}
