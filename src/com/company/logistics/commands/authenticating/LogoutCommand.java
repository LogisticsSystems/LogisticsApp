package com.company.logistics.commands.authenticating;

import com.company.logistics.commands.CommandsConstants;
import com.company.logistics.commands.contracts.Command;
import com.company.logistics.exceptions.InvalidUserInputException;
import com.company.logistics.repositories.contracts.UserRepository;

import java.util.List;

public class LogoutCommand implements Command {
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

        return String.format(CommandsConstants.USER_LOGGED_OUT, username);
    }

    private void validateLoggedInUser() {
        if (!userRepository.hasLoggedInUser()) {
            throw new InvalidUserInputException(CommandsConstants.NO_LOGGED_IN_USER);
        }
    }
}
