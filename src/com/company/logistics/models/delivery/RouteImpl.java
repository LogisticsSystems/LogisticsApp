package com.company.logistics.models.delivery;

import com.company.logistics.enums.City;
import com.company.logistics.models.contracts.DeliveryPackage;
import com.company.logistics.models.contracts.Route;
import com.company.logistics.models.contracts.Truck;
import com.company.logistics.utils.Calculations;
import com.company.logistics.utils.PrintConstants;
import com.company.logistics.utils.ValidationHelper;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class RouteImpl implements Route, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private static final int MIN_LOCATIONS = 2;

    private final int id;
    private final List<City> locations;
    private final LocalDateTime departureTime;
    private final List<DeliveryPackage> assignedPackages = new ArrayList<>();
    private Truck assignedTruck;

    private List<LocalDateTime> schedule = List.of();

    public RouteImpl(int id, List<City> locations, LocalDateTime departureTime) {
        ValidationHelper.validateIntPositive(id, "Route ID");
        ValidationHelper.validateListSizeAtLeast(locations, "Locations", MIN_LOCATIONS);
        ValidationHelper.validateNotNull(departureTime, "Departure time");
        ValidationHelper.validateUniqueIntermediateStops(locations);

        this.id            = id;
        this.locations     = List.copyOf(locations);
        this.departureTime = departureTime;
    }

    @Override public int getId()                      { return id; }
    @Override public LocalDateTime getDepartureTime() { return departureTime; }
    @Override public List<City> getLocations()        { return List.copyOf(locations); }
    @Override public List<LocalDateTime> getSchedule(){ return schedule; }

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
        assignedTruck = truck;
    }

    @Override public List<DeliveryPackage> getAssignedPackages() {
        return List.copyOf(assignedPackages);
    }

    public Optional<Truck> getAssignedTruck() {
        return Optional.ofNullable(assignedTruck);
    }

    @Override
    public void removePackage(int packageId) {
        assignedPackages.removeIf(p -> p.getId() == packageId);
    }

    @Override
    public void unassignTruck() { assignedTruck = null; }

    @Override
    public String print() {
        StringBuilder sb = new StringBuilder();

        appendHeader(sb);
        appendStops(sb);
        appendTruckLine(sb);
        appendPackageLine(sb);
        appendDeliveryWeight(sb);

        return sb.toString();
    }

    private void appendHeader(StringBuilder sb) {
        sb.append(String.format(PrintConstants.ROUTE_HEADER, id));
    }

    private void appendStops(StringBuilder sb) {
        var stops = getLocations();
        var etas  = getSchedule();
        for (int i = 0; i < stops.size(); i++) {
            sb.append(String.format(
                    PrintConstants.ROUTE_STOP_TEMPLATE,
                    stops.get(i),
                    DateTimeFormatter.ofPattern(PrintConstants.DATE_TIME_FORMAT)
                            .format(etas.get(i))
            ));
            // add arrow between stops, newline at end
            if (i < stops.size() - 1) sb.append(" →");
            else                      sb.append("\n");
        }
    }

    private void appendTruckLine(StringBuilder sb) {
        String truckPart = getAssignedTruck()
                .map(t -> String.valueOf(t.getId()))
                .orElse("No truck assigned");
        sb.append(String.format(PrintConstants.ROUTE_TRUCK_LINE, truckPart));
    }

    private void appendPackageLine(StringBuilder sb) {
        String pkgPart = getAssignedPackages().isEmpty()
                ? "No packages assigned"
                : getAssignedPackages().stream()
                .map(p -> String.valueOf(p.getId()))
                .collect(Collectors.joining(", "));
        sb.append(String.format(PrintConstants.ROUTE_PACKAGES_LINE, pkgPart));
    }

    private void appendDeliveryWeight(StringBuilder sb){
        double deliveryWeight = Calculations.calculateTotalLoad(getAssignedPackages());
        String weightPart = deliveryWeight == 0
                ? " No delivery weight.\n"
                : String.format(PrintConstants.ROUTE_DELIVERY_WEIGHT,deliveryWeight);
        sb.append(weightPart);

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
