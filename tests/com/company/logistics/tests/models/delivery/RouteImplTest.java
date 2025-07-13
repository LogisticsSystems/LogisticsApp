package com.company.logistics.tests.models.delivery;

import com.company.logistics.enums.City;
import com.company.logistics.exceptions.InvalidUserInputException;
import com.company.logistics.models.contracts.DeliveryPackage;
import com.company.logistics.models.contracts.Route;
import com.company.logistics.models.contracts.Truck;
import com.company.logistics.models.delivery.DeliveryPackageImpl;
import com.company.logistics.models.delivery.RouteImpl;
import com.company.logistics.models.vehicles.TruckImpl;
import com.company.logistics.utils.ErrorMessages;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

public class RouteImplTest {
    private static final int ID = 1;
    private static final List<City> LOCATIONS = List.of(City.MEL, City.ADL);
    private static final LocalDateTime DEPARTURE_TIME = LocalDateTime.now();

    private static DeliveryPackage pack = new DeliveryPackageImpl(1, City.MEL, City.ADL, 123.0, "package");
    private static DeliveryPackage packTwo = new DeliveryPackageImpl(2, City.MEL, City.ADL, 123.0, "package");
    private static Truck truck = new TruckImpl(2, "package", 123, 1234);

    private static Route route;

    @BeforeEach
    public void setUp() {
        route = new RouteImpl(ID, LOCATIONS, DEPARTURE_TIME);
    }

    // Test Id
    @Test
    public void constructor_Should_ThrowException_When_IdIsNotPositive() {
        Exception ex = Assertions.assertThrows(
                InvalidUserInputException.class,
                () -> new RouteImpl(-2, LOCATIONS, DEPARTURE_TIME));

        String expectedMessage = String.format(ErrorMessages.NON_POSITIVE_INT, "Route ID");
        Assertions.assertEquals(expectedMessage, ex.getMessage());
    }

    // Test locations
    @Test
    public void constructor_Should_ThrowException_When_TooFewLocations() {
        Exception ex = Assertions.assertThrows(
                InvalidUserInputException.class,
                () -> new RouteImpl(ID, List.of(City.MEL), DEPARTURE_TIME));

        String expectedMessage = String.format(ErrorMessages.MIN_LIST_SIZE, "Locations", 2);
        Assertions.assertEquals(expectedMessage, ex.getMessage());
    }

    @Test
    public void constructor_Should_ThrowException_When_AStopRepeats() {
        Exception ex = Assertions.assertThrows(
                InvalidUserInputException.class,
                () -> new RouteImpl(ID, List.of(City.MEL, City.MEL, City.ADL), DEPARTURE_TIME));

        String expectedMessage = String.format(ErrorMessages.NON_UNIQUE_INTERMEDIATE_STOPS, City.MEL);
        Assertions.assertEquals(expectedMessage, ex.getMessage());
    }

    // Test time
    @Test
    public void constructor_Should_ThrowException_When_TimeIsNull() {
        Exception ex = Assertions.assertThrows(
                InvalidUserInputException.class,
                () -> new RouteImpl(ID, LOCATIONS, null));

        String expectedMessage = String.format(ErrorMessages.NOT_NULL, "Departure time");
        Assertions.assertEquals(expectedMessage, ex.getMessage());
    }

    // Test getters
    @Test
    public void getId_Should_ReturnId_When_Called() {
        Assertions.assertEquals(route.getId(), ID);
    }

    @Test
    public void getDepartureTime_Should_ReturnDepartureTime_When_Called() {
        Assertions.assertEquals(route.getDepartureTime(), DEPARTURE_TIME);
    }

    @Test
    public void getLocations_Should_ReturnLocations_When_Called() {
        Assertions.assertEquals(route.getLocations(), LOCATIONS);
    }

    //Test schedule
    @Test
    public void setSchedule_Should_ThrowException_When_ListIsNotTheSameSizeAsLocations() {
        List dateTimes = List.of(DEPARTURE_TIME, DEPARTURE_TIME, DEPARTURE_TIME);

        Exception ex = Assertions.assertThrows(
                InvalidUserInputException.class,
                () -> route.setSchedule(dateTimes));

        String expectedMessage = String.format(ErrorMessages.EXACT_LIST_SIZE, "Schedule", 2);
        Assertions.assertEquals(expectedMessage, ex.getMessage());
    }

    @Test
    public void setSchedule_Should_setSchedule_When_Called() {
        List dateTimes = List.of(DEPARTURE_TIME, DEPARTURE_TIME);

        route.setSchedule(dateTimes);

        Assertions.assertEquals(route.getSchedule(), dateTimes);
    }

    // Test packages
    @Test
    public void assignPackage_Should_ThrowException_When_PackageIsNull() {
        Exception ex = Assertions.assertThrows(
                InvalidUserInputException.class,
                () -> route.assignPackage(null));

        String expectedMessage = String.format(ErrorMessages.NOT_NULL, "Package");
        Assertions.assertEquals(expectedMessage, ex.getMessage());
    }

    @Test
    public void assignPackage_Should_assignPackage_When_GivenValidPackage() {
        route.assignPackage(pack);

        Assertions.assertEquals(1, route.getAssignedPackages().size());
    }

    @Test
    public void removePackage_Should_removePackage_When_GivenValidiD() {
        route.assignPackage(pack);
        route.assignPackage(packTwo);
        route.removePackage(1);

        Assertions.assertEquals(1, route.getAssignedPackages().size());
    }

    // Test truck
    @Test
    public void assignTruck_Should_ThrowException_When_TruckIsNull() {
        Exception ex = Assertions.assertThrows(
                InvalidUserInputException.class,
                () -> route.assignTruck(null));

        String expectedMessage = String.format(ErrorMessages.NOT_NULL, "Truck");
        Assertions.assertEquals(expectedMessage, ex.getMessage());
    }

    @Test
    public void assignTruck_Should_assignTruck_When_GivenValidTruck() {
        route.assignTruck(truck);

        Assertions.assertEquals(truck, route.getAssignedTruck().get());
    }

    @Test
    public void unassignTruck_Should_unassignTruck_When_Called() {
        route.assignTruck(truck);
        route.unassignTruck();

        Assertions.assertTrue( route.getAssignedTruck().isEmpty());
    }



}
