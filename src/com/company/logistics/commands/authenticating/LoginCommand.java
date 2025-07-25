package com.company.logistics.commands.authenticating;

import com.company.logistics.commands.CommandsConstants;
import com.company.logistics.commands.contracts.Command;
import com.company.logistics.exceptions.InvalidUserInputException;
import com.company.logistics.repositories.contracts.UserRepository;
import com.company.logistics.utils.ValidationHelper;

import java.util.List;

public class LoginCommand implements Command {

    public static final int EXPECTED_NUMBER_OF_ARGUMENTS = 2;

    private final UserRepository userRepository;
    private String username;
    private String password;

    public LoginCommand(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public String execute(List<String> parameters) {
        validateLoggedInUser();

        ValidationHelper.validateArgumentsCount(parameters, EXPECTED_NUMBER_OF_ARGUMENTS);

        parseParameters(parameters);

        userRepository.login(this.username, this.password);

        return String.format(CommandsConstants.USER_LOGGED_IN, username);
    }

    private void parseParameters(List<String> parameters) {
        this.username = parameters.get(0);
        this.password = parameters.get(1);
    }

    private void validateLoggedInUser() {
        if (userRepository.hasLoggedInUser()) {
            throw new InvalidUserInputException(String.format(CommandsConstants.USER_LOGGED_IN_ALREADY,
                    userRepository.getLoggedInUser().getUsername())
            );
        }
    }
}
