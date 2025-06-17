package com.company.logistics.models.contracts;

public interface DeliveryPackage extends Identifyable, Printable {
    //TODO
    void assignToRoute(Route route);
}
