package com.company.logistics.commands.creation;

import com.company.logistics.commands.CommandsConstants;
import com.company.logistics.commands.contracts.Command;
import com.company.logistics.enums.UserRole;
import com.company.logistics.models.contracts.User;
import com.company.logistics.repositories.contracts.UserRepository;
import com.company.logistics.utils.ParsingHelpers;
import com.company.logistics.utils.ValidationHelper;

import java.util.List;

public class CreateUserCommand implements Command {
    public static final int EXPECTED_NUMBER_OF_ARGUMENTS = 4;
    public static final String USER_LOGGED_IN = "Please log out first.";
    private final UserRepository userRepository;
    private final User loggedInUser;

    private String username;
    private String name;
    private String password;
    private UserRole role;

    public CreateUserCommand (UserRepository userRepository) {
        this.userRepository = userRepository;
        loggedInUser = userRepository.getLoggedInUser();
    }

    @Override
    public String execute(List<String> parameters) {
        validateLoggedInUser();

        ValidationHelper.validateArgumentsCount(parameters, EXPECTED_NUMBER_OF_ARGUMENTS);

        parseParameters(parameters);

        userRepository.createUser(this.username, this.name, this.password, this.role);

        return String.format(CommandsConstants.USER_CREATED_MESSAGE, username);
    }

    private void validateLoggedInUser() {
        ValidationHelper.validateUserHasRole(loggedInUser, UserRole.MANAGER);
    }

    private void parseParameters(List<String> parameters) {
        this.username = parameters.get(0);
        this.name = parameters.get(1);
        this.password = parameters.get(2);
        this.role = ParsingHelpers.tryParseEnum(parameters.get(3),
                UserRole.class,
                String.format(CommandsConstants.INVALID_ROLE_MESSAGE, parameters.get(3)));
    }
}
