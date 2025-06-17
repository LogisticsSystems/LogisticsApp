package com.company.logistics.models.vehicles;

import com.company.logistics.models.contracts.Route;
import com.company.logistics.models.contracts.Truck;
import com.company.logistics.utils.ValidationHelper;

public class TruckImpl implements Truck {

    private static final int NAME_MIN_LENGTH =2;
    private static final int NAME_MAX_LENGTH =10;

    private int id;
    private String name;
    private double capacityKg;
    private double maxRangeKm;
    private boolean isAssignedToRoute;

    public TruckImpl(int id, String name, double capacityKg, double maxRangeKm) {
        setId(id);
        setName(name);
        setCapacityKg(capacityKg);
        setMaxRangeKm(maxRangeKm);
        isAssignedToRoute=false;

    }

    @Override
    public int getId() {
        return id;
    }

    private void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        ValidationHelper.valideStringLenght(name, NAME_MIN_LENGTH, NAME_MAX_LENGTH,"Truck name");
        this.name=name;
    }

    public double getCapacityKg() {
        return capacityKg;
    }

    private void setCapacityKg(double capacityKg) {
        ValidationHelper.validateDoubleNonNegative(capacityKg,"Truck capacity");
        this.capacityKg=capacityKg;
    }

    public double getMaxRangeKm() {
        return maxRangeKm;
    }

    public void setMaxRangeKm(double maxRangeKm) {
        ValidationHelper.validateDoubleNonNegative(maxRangeKm,"Truck max range(km)");
        this.maxRangeKm=maxRangeKm;
    }
    @Override
    public boolean isAssignedToRoute() {
        return isAssignedToRoute;
    }
    @Override
    public void assignToRoute() {
        isAssignedToRoute = true;
    }


    @Override
    public String print() {
        return "Vehicle{" +
                "id=" + id +
                ", name='" + (name != null ? name : "N/A") + '\'' + // Добавена проверка за null
                ", capacityKg=" + String.format("%.2f", capacityKg) + "kg" + // Форматиране до 2 десетични знака
                ", maxRangeKm=" + String.format("%.2f", maxRangeKm) + "km" + // Форматиране до 2 десетични знака
                '}';
    }
}
