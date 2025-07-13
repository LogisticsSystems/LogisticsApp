package com.company.logistics.tests.models.delivery;

import com.company.logistics.enums.City;
import com.company.logistics.enums.PackageStatus;
import com.company.logistics.exceptions.InvalidUserInputException;
import com.company.logistics.models.contracts.DeliveryPackage;
import com.company.logistics.models.delivery.DeliveryPackageImpl;
import com.company.logistics.utils.ErrorMessages;
import com.company.logistics.utils.PrintConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class DeliveryPackageImplTest {
    private static final int ID = 1;
    private static final String CONTACT = "package";
    private static final double WEIGHT = 123.0;
    private static final City CITY_ONE = City.MEL;
    private static final City CITY_TWO = City.ADL;

    private static DeliveryPackage pack;

    @BeforeEach
    public void setUp() {
        pack = new DeliveryPackageImpl(ID, CITY_ONE, CITY_TWO, WEIGHT, CONTACT);
    }

    // Test Id
    @Test
    public void constructor_Should_ThrowException_When_IdIsNotPositive() {
        Exception ex = Assertions.assertThrows(
                InvalidUserInputException.class,
                () -> new DeliveryPackageImpl(-2, CITY_ONE, CITY_TWO, WEIGHT, CONTACT));

        String expectedMessage = String.format(ErrorMessages.NON_POSITIVE_INT, "Package ID");
        Assertions.assertEquals(expectedMessage, ex.getMessage());
    }

    //Test cities
    @Test
    public void constructor_Should_ThrowException_When_FirstCityIsNull() {
        Exception ex = Assertions.assertThrows(
                InvalidUserInputException.class,
                () -> new DeliveryPackageImpl(ID, null, CITY_TWO, WEIGHT, CONTACT));

        String expectedMessage = String.format(ErrorMessages.NOT_NULL, "StartLocation");
        Assertions.assertEquals(expectedMessage, ex.getMessage());
    }

    @Test
    public void constructor_Should_ThrowException_When_SecondCityIsNull() {
        Exception ex = Assertions.assertThrows(
                InvalidUserInputException.class,
                () -> new DeliveryPackageImpl(ID, CITY_ONE, null, WEIGHT, CONTACT));

        String expectedMessage = String.format(ErrorMessages.NOT_NULL, "EndLocation");
        Assertions.assertEquals(expectedMessage, ex.getMessage());
    }

    @Test
    public void constructor_Should_ThrowException_When_CitiesAreTheSame() {
        Exception ex = Assertions.assertThrows(
                InvalidUserInputException.class,
                () -> new DeliveryPackageImpl(ID, CITY_ONE, CITY_ONE, WEIGHT, CONTACT));

        String expectedMessage = ErrorMessages.START_END_NOT_SAME_ERROR;
        Assertions.assertEquals(expectedMessage, ex.getMessage());
    }

    // Test weight
    @Test
    public void constructor_Should_ThrowException_When_WeightIsNegative() {
        Exception ex = Assertions.assertThrows(
                InvalidUserInputException.class,
                () -> new DeliveryPackageImpl(ID, CITY_ONE, CITY_TWO, -12, CONTACT));

        String expectedMessage = String.format(
                ErrorMessages.NUMBER_NOT_IN_RANGE, "Weight(kg)", 0, 42000);
        Assertions.assertEquals(expectedMessage, ex.getMessage());
    }

    @Test
    public void constructor_Should_ThrowException_When_WeightIsAboveMax() {
        Exception ex = Assertions.assertThrows(
                InvalidUserInputException.class,
                () -> new DeliveryPackageImpl(ID, CITY_ONE, CITY_TWO, 45000, CONTACT));

        String expectedMessage = String.format(
                ErrorMessages.NUMBER_NOT_IN_RANGE, "Weight(kg)", 0, 42000);
        Assertions.assertEquals(expectedMessage, ex.getMessage());
    }

    // Test contact info
    @Test
    public void constructor_Should_ThrowException_When_ContactInfoIsTooShort() {
        Exception ex = Assertions.assertThrows(
                InvalidUserInputException.class,
                () -> new DeliveryPackageImpl(ID, CITY_ONE, CITY_TWO, WEIGHT, "asd"));

        String expectedMessage = String.format(
                ErrorMessages.NUMBER_NOT_IN_RANGE, "ContactInfo", 5, 57);
        Assertions.assertEquals(expectedMessage, ex.getMessage());
    }

    @Test
    public void constructor_Should_ThrowException_When_ContactInfoIsTooLong() {
        Exception ex = Assertions.assertThrows(
                InvalidUserInputException.class,
                () -> new DeliveryPackageImpl(ID, CITY_ONE, CITY_TWO, WEIGHT,
                        "asdfghjkl;asdfghjkl;asdfghjkl;asdfghjkl;asdfghjkl;asdfghjkl;"));

        String expectedMessage = String.format(
                ErrorMessages.NUMBER_NOT_IN_RANGE, "ContactInfo", 5, 57);
        Assertions.assertEquals(expectedMessage, ex.getMessage());
    }

    //Test getters
    @Test
    public void getId_Should_ReturnId_When_Called() {
        Assertions.assertEquals(pack.getId(), ID);
    }

    @Test
    public void getStartLocation_Should_ReturntartLocation_When_Called() {
        Assertions.assertEquals(pack.getStartLocation(), CITY_ONE);
    }

    @Test
    public void getEndLocation_Should_ReturnEndLocation_When_Called() {
        Assertions.assertEquals(pack.getEndLocation(), CITY_TWO);
    }

    @Test
    public void getWeightKg_Should_ReturnWeightKg_When_Called() {
        Assertions.assertEquals(pack.getWeightKg(), WEIGHT);
    }

    @Test
    public void getContactInfo_Should_ReturnContactInfo_When_Called() {
        Assertions.assertEquals(pack.getContactInfo(), CONTACT);
    }

    // Test Expected Arrival
    @Test
    public void setExpectedArrival_Should_SetExpectedArrival_When_Called() {
        LocalDateTime time = LocalDateTime.now();

        pack.setExpectedArrival(time);

        Assertions.assertEquals(pack.getExpectedArrival(), time);
    }

    // Test status
    @Test
    public void getStatus_Should_ReturnUnassigned_When_PackIsCreated() {
        Assertions.assertEquals(pack.getStatus(), PackageStatus.UNASSIGNED);
    }

    @Test
    public void advancePackageStatus_Should_ChangeStatusForward_When_Called() {
        pack.advancePackageStatus();

        Assertions.assertEquals(pack.getStatus(), PackageStatus.PENDING);
    }

    @Test
    public void advancePackageStatus_Should_ThrowException_When_PackageIsAlreadyDelivered() {
        pack.advancePackageStatus();
        pack.advancePackageStatus();
        pack.advancePackageStatus();

        Exception ex = Assertions.assertThrows(
                InvalidUserInputException.class,
                () -> pack.advancePackageStatus());

        String expectedMessage = String.format(
                PrintConstants.PACKAGE_ALREADY_AT, ID, "Delivered");
        Assertions.assertEquals(expectedMessage, ex.getMessage());
    }

    @Test
    public void advancePackageStatus_Should_RemoveExpectedArrival_When_PackageIsDelivered() {
        pack.advancePackageStatus();
        pack.advancePackageStatus();
        pack.advancePackageStatus();

        Assertions.assertNull(pack.getExpectedArrival());
    }

    @Test
    public void revertPackageStatus_Should_ChangeStatusBack_When_Called() {
        pack.advancePackageStatus();
        pack.revertPackageStatus();

        Assertions.assertEquals(pack.getStatus(), PackageStatus.UNASSIGNED);
    }

    @Test
    public void revertPackageStatus_Should_ThrowException_When_PackageIsAlreadyUnassigned() {
        Exception ex = Assertions.assertThrows(
                InvalidUserInputException.class,
                () -> pack.revertPackageStatus());

        String expectedMessage = String.format(
                PrintConstants.PACKAGE_ALREADY_AT, ID, "Awaiting route assignment");
        Assertions.assertEquals(expectedMessage, ex.getMessage());
    }

    @Test
    public void revertPackageStatus_Should_RemoveExpectedArrival_When_PackageIsUnassigned() {
        pack.advancePackageStatus();
        pack.setExpectedArrival(LocalDateTime.now());
        pack.revertPackageStatus();

        Assertions.assertNull(pack.getExpectedArrival());
    }


}
