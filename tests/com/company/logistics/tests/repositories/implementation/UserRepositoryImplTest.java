package com.company.logistics.tests.repositories.implementation;

import com.company.logistics.enums.UserRole;
import com.company.logistics.exceptions.InvalidUserInputException;
import com.company.logistics.models.contracts.User;
import com.company.logistics.repositories.implementation.UserRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.company.logistics.utils.ErrorMessages.*;
import static org.junit.jupiter.api.Assertions.*;

public class UserRepositoryImplTest {
    private UserRepositoryImpl repo;

    @BeforeEach
    public void setup() {
        repo = new UserRepositoryImpl();
    }

    @Test
    public void constructor_Should_AddDefaultAdminManagerUser() {
        List<User> users = repo.getUsers();
        assertEquals(1, users.size());

        User admin = users.get(0);
        assertEquals("admin", admin.getUsername());
        assertEquals(UserRole.MANAGER, admin.getRole());
    }

    @Test
    public void createUser_Should_AddUser_WhenValid() {
        User user = repo.createUser("emily", "Emily Smith", "pass123", UserRole.EMPLOYEE);

        assertEquals("emily", user.getUsername());
        assertEquals(UserRole.EMPLOYEE, user.getRole());
        assertEquals(2, repo.getUsers().size());
    }

    @Test
    public void createUser_Should_Throw_WhenUsernameAlreadyExists() {
        repo.createUser("emily", "Emily Smith", "pass123", UserRole.EMPLOYEE);

        InvalidUserInputException ex = assertThrows(InvalidUserInputException.class, () ->
                repo.createUser("emily", "Different", "diffpass", UserRole.DATA_ANALYST));

        assertEquals(String.format(USER_ALREADY_EXIST, "emily"), ex.getMessage());
    }

    @Test
    public void login_Should_SetLoggedUser_WhenCredentialsAreValid() {
        repo.createUser("analyst", "Data Ana", "secure", UserRole.DATA_ANALYST);

        repo.login("analyst", "secure");

        assertTrue(repo.hasLoggedInUser());
        assertEquals("analyst", repo.getLoggedInUser().getUsername());
    }

    @Test
    public void login_Should_Throw_WhenPasswordIncorrect() {
        repo.createUser("worker", "Worker Bee", "correctpass", UserRole.EMPLOYEE);

        InvalidUserInputException ex = assertThrows(InvalidUserInputException.class, () ->
                repo.login("worker", "wrongpass"));

        assertEquals(INCORRECT_PASSWORD, ex.getMessage());
    }

    @Test
    public void login_Should_Throw_WhenUserDoesNotExist() {
        InvalidUserInputException ex = assertThrows(InvalidUserInputException.class, () ->
                repo.login("ghost", "nope"));

        assertEquals(String.format(NO_SUCH_USER, "ghost"), ex.getMessage());
    }

    @Test
    public void logout_Should_ClearLoggedUser() {
        repo.createUser("tester", "Test Man", "testpass", UserRole.EMPLOYEE);
        repo.login("tester", "testpass");

        repo.logout();

        assertFalse(repo.hasLoggedInUser());
    }

    @Test
    public void getLoggedInUser_Should_Throw_WhenNoUserLoggedIn() {
        InvalidUserInputException ex = assertThrows(InvalidUserInputException.class, () ->
                repo.getLoggedInUser());

        assertEquals(NO_LOGGED_IN_USER, ex.getMessage());
    }

    @Test
    public void removeUser_Should_Remove_WhenExists() {
        repo.createUser("alex", "Alex G", "password", UserRole.DATA_ANALYST);

        User removed = repo.removeUser("alex");

        assertEquals("alex", removed.getUsername());
        assertEquals(1, repo.getUsers().size());
    }

    @Test
    public void removeUser_Should_Throw_WhenUserNotFound() {
        InvalidUserInputException ex = assertThrows(InvalidUserInputException.class, () ->
                repo.removeUser("nobody"));

        assertEquals(String.format(NO_SUCH_USER, "nobody"), ex.getMessage());
    }

    @Test
    public void findUserByUsername_Should_ReturnUser_WhenExists() {
        repo.createUser("jane", "Jane Doe", "password", UserRole.MANAGER);

        User user = repo.findUserByUsername("jane");

        assertEquals("jane", user.getUsername());
    }

    @Test
    public void findUserByUsername_Should_Throw_WhenNotExists() {
        InvalidUserInputException ex = assertThrows(InvalidUserInputException.class, () ->
                repo.findUserByUsername("nope"));

        assertEquals(String.format(NO_SUCH_USER, "nope"), ex.getMessage());
    }

    @Test
    public void getUsers_Should_ReturnSortedByRole() {
        repo.createUser("alpha", "Alpha", "password", UserRole.EMPLOYEE);
        repo.createUser("bravo", "Bravo", "password", UserRole.DATA_ANALYST);

        List<User> users = repo.getUsers();

        assertEquals(3, users.size());
        assertEquals(UserRole.EMPLOYEE, users.get(0).getRole());
        assertEquals(UserRole.MANAGER, users.get(1).getRole());
        assertEquals(UserRole.DATA_ANALYST, users.get(2).getRole());
    }
}
