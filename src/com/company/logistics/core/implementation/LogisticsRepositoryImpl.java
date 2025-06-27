package com.company.logistics.core.implementation;

import com.company.logistics.core.contracts.LogisticsRepository;
import com.company.logistics.core.services.routing.RouteScheduleService;
import com.company.logistics.enums.City;
import com.company.logistics.enums.PackageStatus;
import com.company.logistics.infrastructure.loading.vehicles.contracts.VehicleLoader;
import com.company.logistics.models.contracts.DeliveryPackage;
import com.company.logistics.models.contracts.Route;
import com.company.logistics.models.contracts.Truck;
import com.company.logistics.models.delivery.DeliveryPackageImpl;
import com.company.logistics.models.delivery.RouteImpl;
import com.company.logistics.utils.ErrorMessages;
import com.company.logistics.utils.ValidationHelper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class LogisticsRepositoryImpl implements LogisticsRepository {
    private final AtomicInteger nextId = new AtomicInteger(1);

    private final RouteScheduleService routeScheduleService;
    private final List<DeliveryPackage> packages = new ArrayList<>();
    private final List<Route> routes   = new ArrayList<>();
    private final List<Truck> trucks;

    public LogisticsRepositoryImpl(VehicleLoader vehicleLoader, RouteScheduleService routeScheduleService) {
        this.trucks = new ArrayList<>(vehicleLoader.loadVehicles());
        this.routeScheduleService = routeScheduleService;
    }

    @Override
    public List<DeliveryPackage> getPackages() {
        return new ArrayList<>(packages);
    }

    @Override
    public List<Truck> getTrucks() {
        return new ArrayList<>(trucks);
    }

    @Override
    public List<Route> getRoutes() {
        return routes.stream()
                .sorted(Comparator.comparing(Route::getDepartureTime))
                .collect(Collectors.toList());
    }

    @Override
    public DeliveryPackage createPackage(String contactInfo,
                                         double weight,
                                         City startLocation,
                                         City endLocation) {
        DeliveryPackage pack = new DeliveryPackageImpl(
                nextId.get(),
                startLocation,
                endLocation,
                weight,
                contactInfo
        );
        nextId.incrementAndGet();
        packages.add(pack);
        return pack;
    }

    @Override
    public Route createRoute(List<City> locations, LocalDateTime departureTime) {
        Route route = new RouteImpl(nextId.get(), locations, departureTime);
        nextId.getAndIncrement();

        List<LocalDateTime> schedule = routeScheduleService.computeSchedule(locations, departureTime);
        route.setSchedule(schedule);

        routes.add(route);
        return route;
    }

    @Override
    public void assignPackageToRoute(int packageId, int routeId) {
        DeliveryPackage pack = findPackageById(packageId);
        Route route = findRouteById(routeId);

        // only unassigned packaged may be added
        ValidationHelper.validatePackageStatus(pack, PackageStatus.UNASSIGNED);

        // location-compatibility
        ValidationHelper.validatePackageRouteCompatibility(
                pack.getStartLocation(),
                pack.getEndLocation(),
                route.getLocations()
        );

        //TODO: validate package weight does not exceed max truck capacity

        // attach it...
        route.assignPackage(pack);

        // UNASSIGNED → PENDING
        pack.advancePackageStatus();

        boolean truckAlreadyAssigned = route.getAssignedTruck().isPresent();

        // …and if there was already a truck, go PENDING → IN_TRANSIT immediately
        if (truckAlreadyAssigned) {
            pack.advancePackageStatus();
        }

        LocalDateTime eta = routeScheduleService.getEtaForCity(pack.getEndLocation(), route.getLocations(), route.getSchedule());
        pack.setExpectedArrival(eta);
    }

    @Override
    public void assignTruckToRoute(int truckId, int routeId) {
        Truck truck = findTruckById(truckId);
        Route route = findRouteById(routeId);

        // 1) validate weight
        ValidationHelper.validateTotalLoadWithinCapacity(
                route.getAssignedPackages(),
                truck.getCapacityKg(),
                truckId,
                routeId
        );

        // 2) validate range
        ValidationHelper.validateRouteRangeWithin(
                route.getLocations(),
                truck.getMaxRangeKm(),
                truckId,
                routeId
        );

        // 3) do the assignment
        route.assignTruck(truck);
        truck.assignToRoute();

        // 4) flip PENDING → IN_TRANSIT
        route.getAssignedPackages().stream()
                .filter(p -> p.getStatus() == PackageStatus.PENDING)
                .forEach(DeliveryPackage::advancePackageStatus);
    }

    @Override
    public List<Route> findRoutes(City startLocation, City endLocation) {
        ValidationHelper.validateNotNull(startLocation, "startLocation");
        ValidationHelper.validateNotNull(endLocation, "endLocation");
        return routes.stream()
                .filter(r -> {
                    List<City> locs = r.getLocations();
                    int from = locs.indexOf(startLocation);
                    int to   = locs.indexOf(endLocation);
                    return from >= 0 && to > from;
                })
                .collect(Collectors.toList());
    }

    @Override
    public DeliveryPackage findPackageById(int id) {
        return packages.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format(ErrorMessages.NO_PACKAGE_WITH_ID, id)));
    }

    private Route findRouteById(int id) {
        return routes.stream()
                .filter(r -> r.getId() == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format(ErrorMessages.NO_ROUTE_WITH_ID, id)));
    }

    private Truck findTruckById(int id) {
        return trucks.stream()
                .filter(t -> t.getId() == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException( String.format(ErrorMessages.NO_TRUCK_WITH_ID, id)));
    }

    @Override
    public Route findRouteByPackageId(int packageId) {
        DeliveryPackage pkg = findPackageById(packageId);
        return getRoutes().stream()
                .filter(r -> r.getAssignedPackages().contains(pkg))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("No route carries package %d", packageId)));
    }
}
