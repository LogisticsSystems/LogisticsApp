package com.company.logistics.tests.repositories.implementation;

import com.company.logistics.enums.City;
import com.company.logistics.exceptions.InvalidUserInputException;
import com.company.logistics.models.contracts.DeliveryPackage;
import com.company.logistics.models.contracts.Route;
import com.company.logistics.models.delivery.DeliveryPackageImpl;
import com.company.logistics.repositories.implementation.RouteRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RouteRepositoryImplTest {

    private RouteRepositoryImpl repo;
    private LocalDateTime departure;

    @BeforeEach
    public void setup() {
        repo = new RouteRepositoryImpl();
        departure = LocalDateTime.of(2024, 1, 1, 9, 0);
    }

    @Test
    public void createRoute_Should_AssignUniqueIncrementingIds() {
        Route r1 = repo.createRoute(List.of(City.SYD, City.MEL), departure);
        Route r2 = repo.createRoute(List.of(City.MEL, City.PER), departure);

        assertEquals(1, r1.getId());
        assertEquals(2, r2.getId());
    }

    @Test
    public void getRoutes_Should_ReturnSortedByDeparture() {
        Route r1 = repo.createRoute(List.of(City.SYD, City.MEL), departure.plusHours(5));
        Route r2 = repo.createRoute(List.of(City.MEL, City.PER), departure.plusHours(2));
        Route r3 = repo.createRoute(List.of(City.MEL, City.BRI), departure.plusHours(8));

        List<Route> sorted = repo.getRoutes();

        assertEquals(3, sorted.size());
        assertEquals(r2, sorted.get(0));
        assertEquals(r1, sorted.get(1));
        assertEquals(r3, sorted.get(2));
    }

    @Test
    public void findRoutes_Should_FilterCorrectly() {
        repo.createRoute(List.of(City.SYD, City.MEL, City.PER), departure);
        repo.createRoute(List.of(City.MEL, City.BRI, City.PER), departure);
        repo.createRoute(List.of(City.SYD, City.BRI), departure);

        List<Route> result = repo.findRoutes(City.SYD, City.PER);
        assertEquals(1, result.size());
    }

    @Test
    public void findRouteById_Should_ReturnCorrectRoute() {
        Route route = repo.createRoute(List.of(City.SYD, City.MEL), departure);
        Route found = repo.findRouteById(route.getId());

        assertEquals(route, found);
    }

    @Test
    public void findRouteById_Should_Throw_WhenNotFound() {
        assertThrows(InvalidUserInputException.class, () -> repo.findRouteById(999));
    }

    @Test
    public void findRouteByPackageId_Should_ReturnCorrectRoute() {
        Route route = repo.createRoute(List.of(City.SYD, City.MEL), departure);
        DeliveryPackage pkg = new DeliveryPackageImpl(1, City.SYD, City.MEL, 20.0, "Contact");
        route.assignPackage(pkg);

        Route result = repo.findRouteByPackageId(1);
        assertEquals(route, result);
    }

    @Test
    public void findRouteByPackageId_Should_Throw_WhenNotFound() {
        repo.createRoute(List.of(City.SYD, City.MEL), departure);
        assertThrows(InvalidUserInputException.class, () -> repo.findRouteByPackageId(123));
    }

    @Test
    public void clearAll_Should_RemoveAllRoutes() {
        repo.createRoute(List.of(City.SYD, City.MEL), departure);
        repo.clearAll();
        assertTrue(repo.getRoutes().isEmpty());
    }

    @Test
    public void addRoute_Should_AddExternallyCreatedRoute() {
        Route route = repo.createRoute(List.of(City.SYD, City.MEL), departure);
        repo.clearAll();

        repo.addRoute(route);
        assertEquals(1, repo.getRoutes().size());
        assertEquals(route, repo.findRouteById(route.getId()));
    }

    @Test
    public void setNextId_Should_ControlAutoIncrement() {
        repo.setNextId(77);
        Route route = repo.createRoute(List.of(City.SYD, City.MEL), departure);

        assertEquals(77, route.getId());
    }
}
