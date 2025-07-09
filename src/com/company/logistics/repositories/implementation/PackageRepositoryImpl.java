package com.company.logistics.repositories.implementation;

import com.company.logistics.enums.City;
import com.company.logistics.exceptions.InvalidUserInputException;
import com.company.logistics.models.contracts.DeliveryPackage;
import com.company.logistics.models.contracts.Identifyable;
import com.company.logistics.models.delivery.DeliveryPackageImpl;
import com.company.logistics.repositories.contracts.PackageRepository;
import com.company.logistics.utils.ErrorMessages;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class PackageRepositoryImpl implements PackageRepository {
    private final AtomicInteger nextId = new AtomicInteger(1);
    private final Map<Integer, DeliveryPackage> packages = new HashMap<>();

    @Override
    public List<DeliveryPackage> getPackages() {
        return packages.values().stream()
                .sorted(Comparator.comparingInt(Identifyable::getId))
                .collect(Collectors.toList());
    }

    @Override
    public DeliveryPackage createPackage(String contactInfo, double weight, City startLocation, City endLocation) {
        DeliveryPackage pkg = new DeliveryPackageImpl(
                nextId.get(),
                startLocation,
                endLocation,
                weight,
                contactInfo
        );
        packages.put(nextId.get(), pkg);
        nextId.incrementAndGet();
        return pkg;
    }

    @Override
    public DeliveryPackage findPackageById(int id) {
        DeliveryPackage pkg = packages.get(id);
        if (pkg == null) {
            throw new InvalidUserInputException(String.format(ErrorMessages.NO_PACKAGE_WITH_ID, id));
        }
        return pkg;
    }
}
