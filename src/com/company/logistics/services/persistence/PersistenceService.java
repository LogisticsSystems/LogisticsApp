package com.company.logistics.services.persistence;

import com.company.logistics.enums.SpeedModelType;
import com.company.logistics.models.contracts.DeliveryPackage;
import com.company.logistics.models.contracts.Route;
import com.company.logistics.models.contracts.Truck;
import com.company.logistics.repositories.contracts.PackageRepository;
import com.company.logistics.repositories.contracts.RouteRepository;
import com.company.logistics.repositories.contracts.TruckRepository;
import com.company.logistics.repositories.implementation.PackageRepositoryImpl;
import com.company.logistics.repositories.implementation.RouteRepositoryImpl;
import com.company.logistics.repositories.implementation.TruckRepositoryImpl;
import com.company.logistics.services.speeds.SpeedModelService;
import com.company.logistics.services.speeds.contract.SpeedModel;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class PersistenceService {
    private final PackageRepository packageRepo;
    private final RouteRepository routeRepo;
    private final TruckRepository truckRepo;
    private final SpeedModelService speedModelService;

    public PersistenceService(
            PackageRepository packageRepo,
            RouteRepository   routeRepo,
            TruckRepository   truckRepo,
            SpeedModelService speedModelService
    ) {
        this.packageRepo       = packageRepo;
        this.routeRepo         = routeRepo;
        this.truckRepo         = truckRepo;
        this.speedModelService = speedModelService;
    }

    public void save(Path binaryFile, Path textFile) throws IOException {
        saveBinary(binaryFile);
        saveTextDump(textFile);
    }

    private void saveBinary(Path file) throws IOException {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(Files.newOutputStream(file))) {
            outputStream.writeObject(packageRepo);
            outputStream.writeObject(routeRepo);
            outputStream.writeObject(truckRepo);

            SpeedModelType speedModelType = convertSpeedModelToItsType(speedModelService);
            outputStream.writeObject(speedModelType);
        }
    }

    private void saveTextDump(Path file) throws IOException {
        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(file)) {
            bufferedWriter.write("=== PACKAGES ===\n");
            for (DeliveryPackage p : packageRepo.getPackages()) {
                bufferedWriter.write(p.print());
                bufferedWriter.write("\n\n");
            }

            bufferedWriter.write("=== ROUTES ===\n");
            for (Route r : routeRepo.getRoutes()) {
                bufferedWriter.write(r.print());
                bufferedWriter.write("\n\n");
            }

            bufferedWriter.write("=== SPEED MODEL ===\n");
            bufferedWriter.write(speedModelService.getSpeedModel().getClass().getSimpleName());
            bufferedWriter.write("\n\n");

            bufferedWriter.write("=== TRUCKS ===\n");
            for (Truck t : truckRepo.getTrucks()) {
                bufferedWriter.write(t.print());
                bufferedWriter.write("\n");
            }
        }
    }

    @SuppressWarnings("unchecked")
    public void load(Path binaryFile) throws IOException, ClassNotFoundException {
        try (ObjectInputStream inputStream = new ObjectInputStream(Files.newInputStream(binaryFile))) {
            PackageRepositoryImpl loadedPackageRepository = (PackageRepositoryImpl) inputStream.readObject();
            RouteRepositoryImpl loadedRouteRepository     = (RouteRepositoryImpl)   inputStream.readObject();
            TruckRepositoryImpl loadedTruckRepository     = (TruckRepositoryImpl)   inputStream.readObject();
            SpeedModelType loadedSpeedModelType           = (SpeedModelType)        inputStream.readObject();

            // clear & repopulate each of the repos
            PackageRepositoryImpl livePackageRepository = (PackageRepositoryImpl) packageRepo;
            livePackageRepository.clearAll();
            loadedPackageRepository.getPackages().forEach(livePackageRepository::addPackage);
            livePackageRepository.setNextId(loadedPackageRepository.getNextId());

            RouteRepositoryImpl liveRouteRepository = (RouteRepositoryImpl) routeRepo;
            liveRouteRepository.clearAll();
            loadedRouteRepository.getRoutes().forEach(liveRouteRepository::addRoute);
            liveRouteRepository.setNextId(loadedRouteRepository.getNextId());

            TruckRepositoryImpl liveTruckRepository = (TruckRepositoryImpl) truckRepo;
            liveTruckRepository.clearAll();
            loadedTruckRepository  .getTrucks().forEach(liveTruckRepository::addTruck);

            // restore speed model
            speedModelService.changeModel(loadedSpeedModelType);
        }
    }

    private SpeedModelType convertSpeedModelToItsType(SpeedModelService speedModelService) {
        return SpeedModelType.valueOf(
                speedModelService.getSpeedModel()
                        .getClass()
                        .getSimpleName()
                        .replace("SpeedModel", "")
                        .toUpperCase()
        );
    }

}
