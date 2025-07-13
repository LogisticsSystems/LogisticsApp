package com.company.logistics.repositories.contracts;

import com.company.logistics.enums.City;
import com.company.logistics.models.contracts.DeliveryPackage;

import java.util.List;

public interface PackageRepository {

    List<DeliveryPackage> getPackages();

    DeliveryPackage createPackage(String contactInfo, double weight, City startLocation, City endLocation);

    DeliveryPackage findPackageById(int id);
}
