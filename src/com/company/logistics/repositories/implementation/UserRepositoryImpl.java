package com.company.logistics.repositories.implementation;

import com.company.logistics.enums.UserRole;
import com.company.logistics.exceptions.InvalidUserInputException;
import com.company.logistics.models.contracts.User;
import com.company.logistics.models.users.UserImpl;
import com.company.logistics.repositories.contracts.UserRepository;
import com.company.logistics.utils.ErrorMessages;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class UserRepositoryImpl implements UserRepository, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final Map<String, User> users = new HashMap<>();
    private User loggedUser;

    public UserRepositoryImpl() {
        createUser("admin", "admin", "admin123", UserRole.MANAGER);
    }

    @Override
    public List<User> getUsers() {
        return users.values().stream()
                .sorted(Comparator.comparing(User::getRole))
                .collect(Collectors.toList());
    }

    @Override
    public User getLoggedInUser() {
        if (loggedUser == null) {
            throw new InvalidUserInputException(ErrorMessages.NO_LOGGED_IN_USER);
        }
        return loggedUser;
    }

    @Override
    public boolean hasLoggedInUser() {
        return loggedUser != null;
    }

    @Override
    public void login(String username, String password) {
        User user = findUserByUsername(username);

        if (!user.checkPassword(password)) {
            throw new InvalidUserInputException(ErrorMessages.INCORRECT_PASSWORD);
        }

        loggedUser = user;
    }

    @Override
    public void logout() {
        loggedUser = null;
    }

    @Override
    public User createUser(String username, String name, String password, UserRole userRole) {
        validateUniqueUser(username);
        User user = new UserImpl(username, name, password, userRole);
        users.put(user.getUsername(), user);
        return user;
    }

    private void validateUniqueUser(String username) {
        if (users.containsKey(username)) {
            throw new InvalidUserInputException(String.format(ErrorMessages.USER_ALREADY_EXIST, username));
        }
    }

    @Override
    public User removeUser(String username) {
        User removed = users.remove(username);

        if (removed == null) {
            throw new InvalidUserInputException(String.format(ErrorMessages.NO_SUCH_USER, username));
        }

        return removed;
    }

    @Override
    public User findUserByUsername(String username) {
        User user = users.get(username);

        if (user == null) {
            throw new InvalidUserInputException(String.format(ErrorMessages.NO_SUCH_USER, username));
        }

        return user;
    }

    public void addUser(User user) { users.put(user.getUsername(), user); }
    public void clearAll() { users.clear(); }
}
