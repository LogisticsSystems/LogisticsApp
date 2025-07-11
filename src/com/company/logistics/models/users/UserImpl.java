package com.company.logistics.models.users;

import com.company.logistics.enums.UserRole;
import com.company.logistics.models.contracts.User;
import com.company.logistics.utils.ValidationHelper;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public class UserImpl implements User, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public static final int USERNAME_LEN_MIN = 2;
    public static final int USERNAME_LEN_MAX = 20;
    private static final String USERNAME_REGEX_PATTERN = "^[A-Za-z0-9]+$";
    public static final int PASSWORD_LEN_MIN = 5;
    public static final int PASSWORD_LEN_MAX = 30;
    private static final String PASSWORD_REGEX_PATTERN = "^[A-Za-z0-9@*_-]+$";
    public static final int NAME_LEN_MIN = 2;
    public static final int NAME_LEN_MAX = 20;

    private final static String USER_TO_STRING = "Username: %s, Name: %s, Role: %s%n";

    private final String username;
    private final String name;
    private final String password;
    private final UserRole userRole;

    public UserImpl(String username,
                    String name,
                    String password,
                    UserRole userRole) {
        ValidationHelper.validatePattern(username, USERNAME_REGEX_PATTERN, "Username");
        ValidationHelper.validateStringLength(username, USERNAME_LEN_MIN, USERNAME_LEN_MAX, "Username");
        ValidationHelper.validatePattern(password, PASSWORD_REGEX_PATTERN, "Password");
        ValidationHelper.validateStringLength(password, PASSWORD_LEN_MIN, PASSWORD_LEN_MAX, "Password");
        ValidationHelper.validateStringLength(name, NAME_LEN_MIN, NAME_LEN_MAX, "Name");

        this.username   = username;
        this.password   = password;
        this.name       = name;
        this.userRole   = userRole;
    }

    @Override public String getUsername()   { return this.username;}
    @Override public String getName()       { return this.name;}
    @Override public UserRole getRole()     { return this.userRole;}

    @Override
    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    @Override
    public String print() {
        return String.format(USER_TO_STRING,
                getUsername(),
                getName(),
                getRole());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserImpl user = (UserImpl) o;
        return this.username.equals(user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.username);
    }
}
