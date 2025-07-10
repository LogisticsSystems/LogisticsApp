package com.company.logistics.repositories.contracts;

import com.company.logistics.enums.UserRole;
import com.company.logistics.models.contracts.User;
import com.company.logistics.models.users.UserImpl;

import java.util.List;

public interface UserRepository {
    List<User> getUsers();

    User getLoggedInUser();

    User removeUser(String username);

    User findUserByUsername(String username);

    User createUser(String username, String name, String password, UserRole userRole);

    boolean hasLoggedInUser();

    void login(String username, String password);

    void logout();
}
