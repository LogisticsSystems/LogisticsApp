package com.company.logistics.services.speeds.contract;

import com.company.logistics.enums.City;

import java.time.LocalDateTime;

public interface SpeedModel {

    double getSpeed(City from, City to, LocalDateTime departure);
}
