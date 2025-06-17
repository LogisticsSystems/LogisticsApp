package com.company.logistics.models.delivery;

import com.company.logistics.enums.City;
import com.company.logistics.models.contracts.DeliveryPackage;
import com.company.logistics.models.contracts.Route;
import com.company.logistics.utils.ValidationHelper;

import java.util.Objects;

public class DeliveryPackageImpl implements DeliveryPackage {

    private static final int CONTACT_INFO_MIN_LENGTH=5;//(name-1char surname-1char phoneNumber-1char)+2spaces
    private static final int CONTACT_INFO_MAX_LENGHT=57;//(name-20char surname-20char phoneNumber-15char)+2spaces
    private int id;
    private City startLocation;
    private City endLocation;
    private double weightKg;
    private String contactInfo;
    private boolean isAssignedToRoute;
    private boolean isAssignedToTruck;

    public DeliveryPackageImpl(int id,
                               City startLocation,
                               City endLocation,
                               double weightKg,
                               String contactInfo) {
        setId(id);
        setStartLocation(startLocation);
        setEndLocation(endLocation);
        setWeightKg(weightKg);
        setContactInfo(contactInfo);
        isAssignedToRoute=false;
        isAssignedToTruck=false;


    }

    @Override
    public int getId() {
        return id;
    }

    private void setId(int id) {
        this.id = id;
    }

    public City getStartLocation() {
        return startLocation;
    }

    private void setStartLocation(City startLocation) {
        this.startLocation = startLocation;
    }

    public City getEndLocation() {
        return endLocation;
    }

    private void setEndLocation(City endLocation) {
        this.endLocation = endLocation;
    }

    public double getWeightKg() {
        return weightKg;
    }

    private void setWeightKg(double weightKg) {
        ValidationHelper.validateDoubleNonNegative(weightKg,"Weight(kg)");
        this.weightKg=weightKg;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    private void setContactInfo(String contactInfo) {
        ValidationHelper.valideStringLenght(contactInfo
                ,CONTACT_INFO_MIN_LENGTH
                ,CONTACT_INFO_MAX_LENGHT
                ,"Contact information");
        this.contactInfo=contactInfo;
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
    public boolean isAssignedToTruck() {
        return isAssignedToTruck;
    }
    @Override
    public void assignToTruck() {
        isAssignedToTruck = true;
    }

    @Override
    public void assignToRoute(Route route) {

    }



    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DeliveryPackageImpl that = (DeliveryPackageImpl) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String print() {
        return "DeliveryPackage {" +
                "id=" + id +
                ", startLocation= " + Objects.toString(startLocation, "N/A") +
                ", endLocation= " + Objects.toString(endLocation, "N/A") +
                ", weightKg= " + String.format("%.2f", weightKg) + "kg" + // Форматиране до 2 десетични знака
                ", contactInfo= '" + (contactInfo != null ? contactInfo : "N/A") + '\'' +
                '}';
    }
}
