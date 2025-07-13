package com.company.logistics.tests.models.vehicles;

import com.company.logistics.enums.City;
import com.company.logistics.exceptions.InvalidUserInputException;
import com.company.logistics.models.contracts.Truck;
import com.company.logistics.models.delivery.DeliveryPackageImpl;
import com.company.logistics.models.vehicles.TruckImpl;
import com.company.logistics.utils.ErrorMessages;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TruckImplTest {
    private static final int ID = 1;
    private static final String NAME = "truck";
    private static final double CAPACITY = 123.0;
    private static final double MAX_RANGE = 123.0;

    private static Truck truck;

    @BeforeEach
    public void setUp() {
        truck = new TruckImpl(ID, NAME, CAPACITY, MAX_RANGE);
    }

    // Test name
    @Test
    public void constructor_Should_ThrowException_When_NameIsTooShort() {
        Exception ex = Assertions.assertThrows(
                InvalidUserInputException.class,
                () -> new TruckImpl(ID, "s", CAPACITY, MAX_RANGE));

        String expectedMessage = String.format(
                ErrorMessages.NUMBER_NOT_IN_RANGE, "Truck name", 2, 10);
        Assertions.assertEquals(expectedMessage, ex.getMessage());
    }

    @Test
    public void constructor_Should_ThrowException_When_NameIsTooLong() {
        Exception ex = Assertions.assertThrows(
                InvalidUserInputException.class,
                () -> new TruckImpl(ID, "asdfghjkl;asdfghjkl;asdfghjkl", CAPACITY, MAX_RANGE));

        String expectedMessage = String.format(
                ErrorMessages.NUMBER_NOT_IN_RANGE, "Truck name", 2, 10);
        Assertions.assertEquals(expectedMessage, ex.getMessage());
    }

    // Test capacity
    @Test
    public void constructor_Should_ThrowException_When_CapacityIsNegative() {
        Exception ex = Assertions.assertThrows(
                InvalidUserInputException.class,
                () -> new TruckImpl(ID, NAME, -123, MAX_RANGE));

        String expectedMessage = String.format(
                ErrorMessages.NON_POSITIVE_INT, "Truck capacity");
        Assertions.assertEquals(expectedMessage, ex.getMessage());
    }

    // Test Rrange
    @Test
    public void constructor_Should_ThrowException_When_RangeIsNegative() {
        Exception ex = Assertions.assertThrows(
                InvalidUserInputException.class,
                () -> new TruckImpl(ID, NAME, CAPACITY, -123));

        String expectedMessage = String.format(
                ErrorMessages.NON_POSITIVE_INT, "Truck max range");
        Assertions.assertEquals(expectedMessage, ex.getMessage());
    }

    //Test getters
    @Test
    public void getId_Should_ReturnId_When_Called() {
        Assertions.assertEquals(truck.getId(), ID);
    }

    @Test
    public void getName_Should_ReturnName_When_Called() {
        Assertions.assertEquals(truck.getName(), NAME);
    }

    @Test
    public void getCapacityKg_Should_ReturnCapacity_When_Called() {
        Assertions.assertEquals(truck.getCapacityKg(), CAPACITY);
    }

    @Test
    public void getMaxRangeKm_Should_ReturnRange_When_Called() {
        Assertions.assertEquals(truck.getMaxRangeKm(), MAX_RANGE);
    }

    @Test
    public void isAssignedToRoute_Should_False_When_Initialized() {
        Assertions.assertFalse(truck.isAssignedToRoute());
    }

    // Test assignment
    @Test
    public void assignToRoute_Should_AssignToRoute_When_Called() {
        truck.assignToRoute();

        Assertions.assertTrue(truck.isAssignedToRoute());
    }

    @Test
    public void unassignFromRoute_Should_UnassignToRoute_When_Called() {
        truck.assignToRoute();
        truck.unassignFromRoute();

        Assertions.assertFalse(truck.isAssignedToRoute());
    }



}
