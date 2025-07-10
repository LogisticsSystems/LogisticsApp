package com.company.logistics.commands.authentications;

import com.company.logistics.commands.contracts.Command;
import com.company.logistics.exceptions.InvalidUserInputException;
import com.company.logistics.repositories.contracts.UserRepository;

import java.util.List;

public class LogoutCommand implements Command {
    private final static String USER_LOGGED_OUT = "User %s successfully logged out!";
    public final static String NO_LOGGED_IN_USER = "No user logged in.";

    private final UserRepository userRepository;
    private String username;

    public LogoutCommand(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.username = userRepository.getLoggedInUser().getUsername();
    }

    @Override
    public String execute(List<String> parameters) {
        validateLoggedInUser();

        userRepository.logout();

        return String.format(USER_LOGGED_OUT, username);
    }

    private void validateLoggedInUser() {
        if (!userRepository.hasLoggedInUser()) {
            throw new InvalidUserInputException(NO_LOGGED_IN_USER);
        }
    }
}
