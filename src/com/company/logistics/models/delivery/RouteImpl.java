package com.company.logistics.models.delivery;

import com.company.logistics.enums.City;
import com.company.logistics.models.contracts.DeliveryPackage;
import com.company.logistics.models.contracts.Route;
import com.company.logistics.models.contracts.Truck;
import com.company.logistics.utils.PrintConstants;
import com.company.logistics.utils.ValidationHelper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class RouteImpl implements Route {
    private static final int MIN_LOCATIONS = 2;

    private final int id;
    private final List<City> locations;
    private final LocalDateTime departureTime;
    private final List<DeliveryPackage> assignedPackages = new ArrayList<>();
    private final List<Truck> assignedTrucks = new ArrayList<>();

    private List<LocalDateTime> schedule = List.of();

    public RouteImpl(int id, List<City> locations, LocalDateTime departureTime) {
        ValidationHelper.validateIntPositive(id, "Route ID");
        ValidationHelper.validateListSizeAtLeast(locations, "Locations", MIN_LOCATIONS);
        ValidationHelper.validateNotNull(departureTime, "departureTime");

        this.id            = id;
        this.locations     = List.copyOf(locations);
        this.departureTime = departureTime;
    }

    @Override public int getId()                    { return id; }
    @Override public LocalDateTime getDepartureTime() { return departureTime; }
    @Override public List<City> getLocations()      { return List.copyOf(locations); }

    @Override public List<LocalDateTime> getSchedule() { return schedule; }
    @Override public void setSchedule(List<LocalDateTime> schedule) {
        ValidationHelper.validateListSizeEquals(schedule, "Schedule", locations.size());
        this.schedule = List.copyOf(schedule);
    }

    @Override public void assignPackage(DeliveryPackage pkg) {
        ValidationHelper.validateNotNull(pkg, "Package");
        assignedPackages.add(pkg);
    }

    @Override public void assignTruck(Truck truck) {
        ValidationHelper.validateNotNull(truck, "Truck");
        assignedTrucks.add(truck);
    }

    @Override public List<DeliveryPackage> getAssignedPackages() {
        return List.copyOf(assignedPackages);
    }
    @Override public List<Truck> getAssignedTrucks() {
        return List.copyOf(assignedTrucks);
    }

    @Override
    public String print() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(PrintConstants.ROUTE_HEADER, id));

        List<City> stops     = getLocations();
        List<LocalDateTime> etas = getSchedule();

        for (int i = 0; i < stops.size(); i++) {
            sb.append(String.format(PrintConstants.ROUTE_STOP_TEMPLATE, i + 1, stops.get(i), etas.get(i)));
        }

        if (!assignedTrucks.isEmpty()) {
            String truckIds = assignedTrucks.stream()
                    .map(t -> String.valueOf(t.getId()))
                    .collect(Collectors.joining(", "));
            sb.append(String.format(PrintConstants.ROUTE_TRUCK_LINE, truckIds));
        }

        if (!assignedPackages.isEmpty()) {
            String pkgIds = assignedPackages.stream()
                    .map(p -> String.valueOf(p.getId()))
                    .collect(Collectors.joining(", "));
            sb.append(String.format(PrintConstants.ROUTE_PACKAGES_LINE, pkgIds));
        }

        return sb.toString();
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RouteImpl route = (RouteImpl) o;
        return id == route.id;
    }

    @Override public int hashCode() {
        return Objects.hashCode(id);
    }
}
