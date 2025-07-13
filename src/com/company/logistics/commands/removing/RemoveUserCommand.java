package com.company.logistics.commands.removing;

import com.company.logistics.commands.CommandsConstants;
import com.company.logistics.commands.contracts.Command;
import com.company.logistics.enums.UserRole;
import com.company.logistics.exceptions.InvalidUserInputException;
import com.company.logistics.models.contracts.User;
import com.company.logistics.repositories.contracts.UserRepository;
import com.company.logistics.utils.ValidationHelper;

import java.util.List;

public class RemoveUserCommand implements Command {
    public static final int EXPECTED_NUMBER_OF_ARGUMENTS = 1;

    private final UserRepository userRepository;
    private final User loggedInUser;

    private String username;

    public RemoveUserCommand (UserRepository userRepository) {
        this.userRepository = userRepository;
        loggedInUser = userRepository.getLoggedInUser();
    }

    @Override
    public String execute(List<String> parameters) {
        validateLoggedInUser();

        ValidationHelper.validateArgumentsCount(parameters, EXPECTED_NUMBER_OF_ARGUMENTS);

        parseParameters(parameters);

        User user = userRepository.removeUser(this.username);

        return String.format(CommandsConstants.USER_REMOVED_MESSAGE, username);
    }

    private void validateLoggedInUser() {
        ValidationHelper.validateUserHasRole(loggedInUser, UserRole.MANAGER);

        if (loggedInUser.getUsername().equals(this.username)) {
            throw new InvalidUserInputException(String.format(CommandsConstants.CURRENTLY_LOGGED_IN, username));
        }
    }

    private void parseParameters(List<String> parameters) {
        this.username = parameters.get(0);
    }
}
