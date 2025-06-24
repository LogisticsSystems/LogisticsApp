package com.company.logistics.core.implementation;

import com.company.logistics.commands.CommandsConstants;
import com.company.logistics.core.contracts.LogisticsRepository;
import com.company.logistics.core.services.routing.RouteScheduleService;
import com.company.logistics.enums.City;
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
import java.util.Optional;
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

        if(pack.isAssignedToRoute()) { throw new IllegalArgumentException(String.format(
                ErrorMessages.ALREADY_ASSIGNED, "Package", "route")); }

        //Validate that if a truck is assigned, the package won't exceed the truck capacity
        if(!route.getAssignedTrucks().isEmpty()){
            Truck truck=route.getAssignedTrucks().get(0);
            ValidationHelper.validateTruckCapacity(truck,pack.getWeightKg());
        }


        ValidationHelper.validatePackageRouteCompatibility(
                pack.getStartLocation(),
                pack.getEndLocation(),
                route.getLocations()
        );

        route.assignPackage(pack);
        pack.assignToRoute();

        LocalDateTime eta = routeScheduleService.getEtaForCity(pack.getEndLocation(), route.getLocations(), route.getSchedule());
        pack.setExpectedArrival(eta);
    }

    @Override
    public String assignTruckToRoute(int truckId, int routeId) {
        //Set up
        Truck truck = findTruckById(truckId);
        Route route = findRouteById(routeId);
        double packagesWeight=getRoutePackagesWeight(route);


        if(truck.isAssignedToRoute()){ throw new IllegalArgumentException(String.format(
                ErrorMessages.ALREADY_ASSIGNED, "Truck", "route")); }

        //Checks if the route distance exceeds the truck maximum range
        ValidationHelper.validateTruckRange(truck,route);

        //If capacity is exceeded find a truck with more capacity
        try {
            //normal case when the truck has a capacity
            ValidationHelper.validateTruckCapacity(truck, packagesWeight);
            route.assignTruck(truck);
            truck.assignToRoute();
            return String.format(CommandsConstants.ASSIGNED_TO_ROUTE_MESSAGE, "Truck", truckId, routeId);
        } catch (IllegalArgumentException e){
            //When the truck doesn't have enough capacity, it finds a bigger truck
            Optional<Truck> alternative=findBiggerTruckIfAvailable(packagesWeight);
            if(alternative.isPresent()){
                route.assignTruck(alternative.get());
                alternative.get().assignToRoute();
                return String.format(CommandsConstants.ALTERNATIVE_TRUCK_ASSIGNED,truckId, alternative.get().getId(), routeId);
            }else{
                //If no bigger truck is found it creates a new route with the same locations
                //and adds to it the overflow packages.
                Route newRoute=createRoute(route.getLocations(),route.getDepartureTime());
                double totalWeight=getRoutePackagesWeight(route);
                for(DeliveryPackage pkg:route.getAssignedPackages()){
                    if(totalWeight<=truck.getCapacityKg()){
                        break;
                    }
                    route.removePackage(pkg);
                    newRoute.assignPackage(pkg);
                    totalWeight-=pkg.getWeightKg();
                }
                route.assignTruck(truck);
                truck.assignToRoute();

                //Assign truck to the new route
                Optional<Truck> secondTruck=findBiggerTruckIfAvailable(getRoutePackagesWeight(newRoute));
                if(secondTruck.isPresent()){
                    newRoute.assignTruck(secondTruck.get());
                    secondTruck.get().assignToRoute();
                    return String.format(CommandsConstants.NEW_ROUTE_AND_TRUCKS_ASSIGNED
                            ,routeId
                            ,newRoute.getId()
                            ,truckId
                            ,secondTruck.get().getId());
                }

            }
        }
        return "";

    }




    private double getRoutePackagesWeight(Route route){
        return route.getAssignedPackages().stream()
                .mapToDouble(DeliveryPackage::getWeightKg)
                .sum();
    }

    private Optional<Truck> findBiggerTruckIfAvailable(double packagesWeight){
        return getTrucks().stream()
                .filter(t->!t.isAssignedToRoute())//find available trucks
                .filter(t->t.getCapacityKg()>=packagesWeight)
                .findFirst();//find the first truck that can load the packages
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

    private DeliveryPackage findPackageById(int id) {
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
}
