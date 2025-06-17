package com.company.logistics.models.delivery;

import com.company.logistics.enums.City;
import com.company.logistics.models.contracts.DeliveryPackage;
import com.company.logistics.models.contracts.Route;

public class DeliveryPackageImpl implements DeliveryPackage {

    //TODO

    public DeliveryPackageImpl(int id,
                               City startLocation,
                               City endLocation,
                               double weightKg,
                               String contactInfo) {

    }

    @Override
    public int getId() {
        return 0;
    }

    @Override
    public void assignToRoute(Route route) {

    }
}
