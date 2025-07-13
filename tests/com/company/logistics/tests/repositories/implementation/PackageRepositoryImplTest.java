package com.company.logistics.tests.repositories.implementation;

import com.company.logistics.enums.City;
import com.company.logistics.exceptions.InvalidUserInputException;
import com.company.logistics.models.contracts.DeliveryPackage;
import com.company.logistics.repositories.implementation.PackageRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PackageRepositoryImplTest {

    private PackageRepositoryImpl repo;

    @BeforeEach
    public void setup() {
        repo = new PackageRepositoryImpl();
    }

    @Test
    public void createPackage_Should_AssignUniqueIncrementingIds() {
        DeliveryPackage pkg1 = repo.createPackage("John Doe", 5.0, City.SYD, City.MEL);
        DeliveryPackage pkg2 = repo.createPackage("Jane Doe", 7.0, City.MEL, City.PER);

        assertEquals(1, pkg1.getId());
        assertEquals(2, pkg2.getId());
    }

    @Test
    public void createPackage_Should_StorePackageCorrectly() {
        DeliveryPackage pkg = repo.createPackage("John Smith", 10.0, City.SYD, City.MEL);
        List<DeliveryPackage> all = repo.getPackages();

        assertEquals(1, all.size());
        assertEquals(pkg, all.get(0));
    }

    @Test
    public void getPackages_Should_ReturnSortedById() {
        repo.createPackage("ABCDED", 1.0, City.SYD, City.MEL);
        repo.createPackage("ABCDED", 2.0, City.SYD, City.MEL);
        repo.createPackage("ABCDED", 3.0, City.SYD, City.MEL);

        List<DeliveryPackage> sorted = repo.getPackages();
        assertEquals(3, sorted.size());
        assertTrue(sorted.get(0).getId() < sorted.get(1).getId());
    }

    @Test
    public void findPackageById_Should_ReturnCorrectPackage() {
        DeliveryPackage created = repo.createPackage("ABCDED", 2.5, City.SYD, City.MEL);
        DeliveryPackage found = repo.findPackageById(created.getId());

        assertEquals(created, found);
    }

    @Test
    public void findPackageById_Should_Throw_WhenNotFound() {
        Exception ex = assertThrows(InvalidUserInputException.class, () -> repo.findPackageById(99));
        assertTrue(ex.getMessage().contains("No package with ID:"));
    }

    @Test
    public void clearAll_Should_RemoveAllPackages() {
        repo.createPackage("ABCDED", 1.0, City.SYD, City.MEL);
        repo.createPackage("ABCDED", 2.0, City.SYD, City.MEL);

        repo.clearAll();

        assertTrue(repo.getPackages().isEmpty());
    }

    @Test
    public void addPackage_Should_ManuallyInsertPackage() {
        DeliveryPackage manual = repo.createPackage("ABCDED", 1.0, City.SYD, City.MEL);
        repo.clearAll();

        repo.addPackage(manual);
        assertEquals(1, repo.getPackages().size());
        assertEquals(manual, repo.findPackageById(manual.getId()));
    }

    @Test
    public void setNextId_Should_ControlAutoIncrementManually() {
        repo.setNextId(42);
        DeliveryPackage pkg = repo.createPackage("ABCDED", 1.0, City.SYD, City.MEL);

        assertEquals(42, pkg.getId());
    }
}
