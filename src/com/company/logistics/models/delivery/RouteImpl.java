package com.company.logistics.models.delivery;

import com.company.logistics.enums.City;
import com.company.logistics.models.contracts.DeliveryPackage;
import com.company.logistics.models.contracts.Route;
import com.company.logistics.models.contracts.Truck;
import com.company.logistics.utils.ListingHelpers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RouteImpl implements Route {
    private int id;
    private List<City> locations;
    private LocalDateTime departureTime;
    private final List<DeliveryPackage> assignedPackages;

    private final List<Truck> assignedTrucks;

    public RouteImpl(int id, List<City> locations, LocalDateTime departureTime) {
        setId(id);
        setLocations(locations);
        setDepartureTime(departureTime);
        this.assignedPackages=new ArrayList<>();
        this.assignedTrucks=new ArrayList<>();
    }

    @Override
    public int getId() { return id; }

    private void setId(int id) {
        this.id = id;
    }

    private void setLocations(List<City> locations) {
        this.locations = locations;
    }

    private void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    @Override
    public LocalDateTime getDepartureTime() {return LocalDateTime.now(); }

    @Override
    public void assignPackage(DeliveryPackage pkg) {
        //TODO
        //validation
        assignedPackages.add(pkg);
    }

    @Override
    public void assignTruck(Truck truck) {
        //TODO
        //validation
        assignedTrucks.add(truck);
    }

    @Override
    public List<City> getLocations() {
        return new ArrayList<>(locations);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RouteImpl route = (RouteImpl) o;
        return id == route.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String print() {
        return "Route {" +
                "id=" + id +
                ", locations=" + Objects.toString(locations, "N/A") + // List.toString() ще работи тук
                ", departureTime=" + Objects.toString(departureTime, "N/A") + // LocalDateTime.toString()
                ", assignedPackages=" + ListingHelpers.elementsToString(assignedPackages) + // List.toString() ще работи тук за List<DeliveryPackage>
                ", assignedTrucks=" + ListingHelpers.elementsToString(assignedTrucks) + // List.toString() ще работи тук за List<DeliveryPackage>
                '}';
    }
}
