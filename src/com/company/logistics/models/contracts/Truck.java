package com.company.logistics.models.contracts;

public interface Truck extends Identifyable, Printable{
    //TODO

    void assignToRoute();
    boolean isAssignedToRoute();
}
