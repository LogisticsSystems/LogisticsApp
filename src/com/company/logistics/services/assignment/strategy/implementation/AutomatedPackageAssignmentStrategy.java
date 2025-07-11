package com.company.logistics.services.assignment.strategy.implementation;

import com.company.logistics.commands.CommandsConstants;
import com.company.logistics.dto.PackageSnapshot;
import com.company.logistics.enums.PackageStatus;
import com.company.logistics.exceptions.InvalidUserInputException;
import com.company.logistics.models.contracts.DeliveryPackage;
import com.company.logistics.models.contracts.Route;
import com.company.logistics.models.contracts.Truck;
import com.company.logistics.repositories.contracts.PackageRepository;
import com.company.logistics.repositories.contracts.RouteRepository;
import com.company.logistics.repositories.contracts.TruckRepository;
import com.company.logistics.services.assignment.strategy.PackageAssignmentStrategy;
import com.company.logistics.services.routing.management.RouteCreationService;
import com.company.logistics.services.speeds.SpeedModelService;
import com.company.logistics.utils.Calculations;
import com.company.logistics.utils.ErrorMessages;
import com.company.logistics.utils.ValidationHelper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class AutomatedPackageAssignmentStrategy implements PackageAssignmentStrategy {
    private static final double MAX_TRUCK_CAPACITY = 42000;

    private final TruckRepository truckRepository;
    private final PackageRepository packageRepository;
    private final RouteRepository routeRepository;
    private final SpeedModelService speedModelService;
    private final RouteCreationService routeCreationService;



    public AutomatedPackageAssignmentStrategy(
            TruckRepository truckRepository,
            PackageRepository packageRepository,
            RouteRepository routeRepository,
            SpeedModelService speedModelService,
            RouteCreationService routeCreationService
    ) {
        this.truckRepository   = truckRepository;
        this.packageRepository = packageRepository;
        this.routeRepository   = routeRepository;
        this.speedModelService = speedModelService;
        this.routeCreationService = routeCreationService;
    }

    //----- Public -----------------

    @Override
    public PackageSnapshot assignPackage(int packageId, int routeId) {
        DeliveryPackage packToAssign = packageRepository.findPackageById(packageId);
        Route currRoute = routeRepository.findRouteById(routeId);

        validatePackage(currRoute, packToAssign);
        return tryAssignPackage(packToAssign, currRoute);

    }

    //----- Core -----------------

    private PackageSnapshot tryAssignPackage(DeliveryPackage packToAssign, Route currRoute) {

        List<DeliveryPackage> packs = new ArrayList<>(currRoute.getAssignedPackages());
        packs.add(packToAssign);
        //if the route has a truck
        if (currRoute.getAssignedTruck().isPresent()) {
            Truck truck = currRoute.getAssignedTruck().get();

            try {
                ValidationHelper.validateTotalLoadWithinCapacity(
                        packs,
                        truck.getCapacityKg(),
                        String.format(ErrorMessages.ROUTE_LOAD_EXCEEDS_CAPACITY,
                                packToAssign.getId(),
                                currRoute.getId(),
                                Calculations.calculateTotalLoad(packs),
                                truck.getCapacityKg(),
                                truck.getName())
                );

                return handleAssignition(currRoute, packToAssign, CommandsConstants.EMPTY_MESSAGE);
            }catch (InvalidUserInputException e) {
                return handleNewTruck(Calculations.calculateTotalLoad(packs), packToAssign, currRoute, truck);
            }

        } else {

            try {
                ValidationHelper.validateTotalLoadWithinCapacity(
                        packs,
                        MAX_TRUCK_CAPACITY,
                        String.format(ErrorMessages.ROUTE_MAX_LOAD_EXCEEDS_CAPACITY,
                                packToAssign.getId(),
                                currRoute.getId(),
                                Calculations.calculateTotalLoad(packs),
                                MAX_TRUCK_CAPACITY)
                );
                return handleAssignition(currRoute, packToAssign, CommandsConstants.EMPTY_MESSAGE);
            }catch (InvalidUserInputException e) {
                return handleOverflowPackage(currRoute, packToAssign);
            }
        }
    }

    //----- Truck Logic -----------------

    private PackageSnapshot handleNewTruck(double totalWeight, DeliveryPackage packToAssign, Route currRoute, Truck truck) {
        Optional<Truck> alternative = findBiggerTruckIfAvailable(totalWeight, currRoute);

        if(alternative.isPresent()){
            alternative.get().assignToRoute();
            currRoute.assignTruck(alternative.get());
            truck.unassignFromRoute();

            return handleAssignition(currRoute, packToAssign, String.format(CommandsConstants.NEW_TRUCK_FOUND,
                    alternative.get().getId(),currRoute.getId(),packToAssign.getId()));
        }else {
            return handleOverflowPackage(currRoute, packToAssign);
        }
    }

    private Optional<Truck> findBiggerTruckIfAvailable(double packagesWeight, Route currRoute){

        return truckRepository.getTrucks().stream()
                .filter(t->!t.isAssignedToRoute())
                .filter(t->t.getCapacityKg()>=packagesWeight)
                .sorted(Comparator.comparingDouble(Truck :: getCapacityKg))
                .filter(t -> {
                    try {
                        ValidationHelper.validateRouteRangeWithin(
                                currRoute.getLocations(),
                                t.getMaxRangeKm(),
                                t.getId(),
                                currRoute.getId()
                        );
                        return true;
                    }catch (InvalidUserInputException e){
                        return false;
                    }
                })
                .findFirst();
    }

    //----- Route Overflow Logic -----------------

    private PackageSnapshot handleOverflowPackage(Route currRoute, DeliveryPackage packToAssign) {
        Optional<Route> availableRoute=checkCompatibleRoutes(currRoute, packToAssign);

        if(availableRoute.isPresent()){
            return handleAssignition(availableRoute.get(), packToAssign,
                    String.format(CommandsConstants.COMPATIBLE_ROUTE_FOUND,
                            packToAssign.getId(), availableRoute.get().getId()));
        }

        Route newRoute = routeCreationService.createRoute(
                currRoute.getLocations(),
                currRoute.getDepartureTime()
        );

        return handleAssignition(newRoute, packToAssign,
                String.format(CommandsConstants.NEW_ROUTE_CREATED,
                        packToAssign.getId(), newRoute.getId()));

    }


    private Optional<Route> checkCompatibleRoutes(Route currRoute, DeliveryPackage packToAssign){
        return routeRepository.getRoutes().stream()
                .filter(r -> isRouteCompatibleWithPackage(packToAssign, r))
                .filter(r -> !r.equals(currRoute))
                .filter(r -> {
                    List<DeliveryPackage> simulatedList = new ArrayList<>(r.getAssignedPackages());
                    simulatedList.add(packToAssign);

                    double totalWeight = Calculations.calculateTotalLoad(simulatedList);
                    double maxCapacity = r.getAssignedTruck()
                            .map(Truck::getCapacityKg)
                            .orElse(MAX_TRUCK_CAPACITY);

                    return totalWeight <= maxCapacity;
                })
                .min(Comparator.comparingDouble(
                        r -> Calculations.calculateTotalLoad(r.getAssignedPackages())));
    }

    private static boolean isRouteCompatibleWithPackage(DeliveryPackage pack, Route route) {
        try {
            ValidationHelper.validatePackageRouteCompatibility(
                    pack.getStartLocation(),
                    pack.getEndLocation(),
                    route.getLocations()
            );
            return true;
        } catch (InvalidUserInputException e) {
            return false;
        }
    }

    //----- Validations -----------------

    private void validatePackage(Route routeToCheck, DeliveryPackage packToAssign) {
        ValidationHelper.validatePackageStatus(packToAssign, PackageStatus.UNASSIGNED);
        ValidationHelper.validatePackageRouteCompatibility(
                packToAssign.getStartLocation(),
                packToAssign.getEndLocation(),
                routeToCheck.getLocations()
        );


    }


//----- Package Assignation Helpers -----------------

    private PackageSnapshot handleAssignition(Route route, DeliveryPackage packToAssign, String message){
        route.assignPackage(packToAssign);
        updatePackageStatus(route,packToAssign);
        setEtaToPackage(route, packToAssign);

        return new PackageSnapshot(
                packToAssign.getId(),
                packToAssign.getStatus(),
                packToAssign.getExpectedArrival(),
                message
        );
    }


    private void updatePackageStatus(Route currRoute, DeliveryPackage packToAssign) {
        packToAssign.advancePackageStatus();
        if (currRoute.getAssignedTruck().isPresent()) {
            packToAssign.advancePackageStatus();
        }
    }

    private void setEtaToPackage(Route currRoute, DeliveryPackage packToAssign) {
        LocalDateTime eta = speedModelService.getRouteScheduler().getEtaForCity(
                packToAssign.getEndLocation(),
                currRoute.getLocations(),
                currRoute.getSchedule()
        );
        packToAssign.setExpectedArrival(eta);
    }
}
