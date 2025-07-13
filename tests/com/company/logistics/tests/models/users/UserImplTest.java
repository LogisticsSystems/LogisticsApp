package com.company.logistics.tests.models.users;

import com.company.logistics.enums.City;
import com.company.logistics.enums.UserRole;
import com.company.logistics.exceptions.InvalidUserInputException;
import com.company.logistics.models.contracts.User;
import com.company.logistics.models.delivery.DeliveryPackageImpl;
import com.company.logistics.models.users.UserImpl;
import com.company.logistics.utils.ErrorMessages;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.print.attribute.standard.MediaSize;

public class UserImplTest {
    private static final String USERNAME = "sil";
    private static final String NAME = "myname";
    private static final String PASSWORD = "sil123";
    private static final UserRole ROLE = UserRole.EMPLOYEE;

    private static User user;

    @BeforeEach
    public void setUp() {
        user = new UserImpl(USERNAME, NAME, PASSWORD, ROLE);
    }

    // Test username
    @Test
    public void constructor_Should_ThrowException_When_UsernameIsTooShort() {
        Exception ex = Assertions.assertThrows(
                InvalidUserInputException.class,
                () -> new UserImpl("a", NAME, PASSWORD, ROLE));

        String expectedMessage = String.format(
                ErrorMessages.NUMBER_NOT_IN_RANGE, "Username", 2, 20);
        Assertions.assertEquals(expectedMessage, ex.getMessage());
    }

    @Test
    public void constructor_Should_ThrowException_When_UsernameIsTooLong() {
        Exception ex = Assertions.assertThrows(
                InvalidUserInputException.class,
                () -> new UserImpl("asdfghjklasdfghjklasdfghjk",
                        NAME, PASSWORD, ROLE));

        String expectedMessage = String.format(
                ErrorMessages.NUMBER_NOT_IN_RANGE, "Username", 2, 20);
        Assertions.assertEquals(expectedMessage, ex.getMessage());
    }

    @Test
    public void constructor_Should_ThrowException_When_UsernameIncludesSpecialCharacters() {
        Exception ex = Assertions.assertThrows(
                InvalidUserInputException.class,
                () -> new UserImpl("asd!@#", NAME, PASSWORD, ROLE));

        String expectedMessage = String.format(
                ErrorMessages.SYMBOLS_PATTERN_ERR, "Username");
        Assertions.assertEquals(expectedMessage, ex.getMessage());
    }

    // Test username
    @Test
    public void constructor_Should_ThrowException_When_NameIsTooShort() {
        Exception ex = Assertions.assertThrows(
                InvalidUserInputException.class,
                () -> new UserImpl(USERNAME, "a", PASSWORD, ROLE));

        String expectedMessage = String.format(
                ErrorMessages.NUMBER_NOT_IN_RANGE, "Name", 2, 20);
        Assertions.assertEquals(expectedMessage, ex.getMessage());
    }

    @Test
    public void constructor_Should_ThrowException_When_NameIsTooLong() {
        Exception ex = Assertions.assertThrows(
                InvalidUserInputException.class,
                () -> new UserImpl(USERNAME, "asdfghjklasdfghjklasdfghjk",
                        PASSWORD, ROLE));

        String expectedMessage = String.format(
                ErrorMessages.NUMBER_NOT_IN_RANGE, "Name", 2, 20);
        Assertions.assertEquals(expectedMessage, ex.getMessage());
    }

    // Test Password
    @Test
    public void constructor_Should_ThrowException_When_PasswordIsTooShort() {
        Exception ex = Assertions.assertThrows(
                InvalidUserInputException.class,
                () -> new UserImpl(USERNAME, NAME, "a", ROLE));

        String expectedMessage = String.format(
                ErrorMessages.NUMBER_NOT_IN_RANGE, "Password", 5, 30);
        Assertions.assertEquals(expectedMessage, ex.getMessage());
    }

    @Test
    public void constructor_Should_ThrowException_When_PasswordIsTooLong() {
        Exception ex = Assertions.assertThrows(
                InvalidUserInputException.class,
                () -> new UserImpl(USERNAME, NAME,
                        "asdfghjklasdfghjklasdfghjkasdfghjklasdfghjklasdfghjk",
                        ROLE));

        String expectedMessage = String.format(
                ErrorMessages.NUMBER_NOT_IN_RANGE, "Password", 5, 30);
        Assertions.assertEquals(expectedMessage, ex.getMessage());
    }

    @Test
    public void constructor_Should_ThrowException_When_PasswordIncludesSpecialCharacters() {
        Exception ex = Assertions.assertThrows(
                InvalidUserInputException.class,
                () -> new UserImpl(USERNAME, NAME, "!!!!!!!", ROLE));

        String expectedMessage = String.format(
                ErrorMessages.SYMBOLS_PATTERN_ERR, "Password");
        Assertions.assertEquals(expectedMessage, ex.getMessage());
    }

    //Test getters
    @Test
    public void getUsername_Should_ReturnUsername_When_Called() {
        Assertions.assertEquals(user.getUsername(), USERNAME);
    }

    @Test
    public void getName_Should_ReturnName_When_Called() {
        Assertions.assertEquals(user.getName(), NAME);
    }

    @Test
    public void getRole_Should_ReturnRole_When_Called() {
        Assertions.assertEquals(user.getRole(), ROLE);
    }

    @Test
    public void checkPassword_Should_ReturnTrue_When_GivenCorrectPassword() {
        Assertions.assertTrue(user.checkPassword(PASSWORD));
    }

    @Test
    public void checkPassword_Should_ReturnFalse_When_GivenWrongPassword() {
        Assertions.assertFalse(user.checkPassword("asfdsfsd"));
    }
}
