package com.company.logistics.commands.listing;

import com.company.logistics.commands.CommandsConstants;
import com.company.logistics.commands.contracts.Command;
import com.company.logistics.enums.UserRole;
import com.company.logistics.models.contracts.User;
import com.company.logistics.repositories.contracts.UserRepository;
import com.company.logistics.utils.ListingHelpers;
import com.company.logistics.utils.ParsingHelpers;
import com.company.logistics.utils.PrintConstants;
import com.company.logistics.utils.ValidationHelper;

import java.util.List;

public class ListUsersWithRoleCommand implements Command {
    public static final int EXPECTED_NUMBER_OF_ARGUMENTS = 1;

    private UserRole role;
    private final List<User> users;
    private final User loggedInUser;


    public ListUsersWithRoleCommand(UserRepository userRepository) {
        this.users = userRepository.getUsers();
        this.loggedInUser = userRepository.getLoggedInUser();
    }

    @Override
    public String execute(List<String> parameters) {
        validateLoggedInUser();

        ValidationHelper.validateArgumentsCount(parameters,EXPECTED_NUMBER_OF_ARGUMENTS);

        parseParameters(parameters);

        List<User> usersWithRole = getUsersWithRole(role);

        if(usersWithRole.isEmpty()){

            return String.format(CommandsConstants.NO_USERS_WITH_ROLE, this.role);
        }

        return PrintConstants.LINE_BREAK + "\n" + ListingHelpers.elementsToString(usersWithRole);
    }

    private void validateLoggedInUser() {
        ValidationHelper.validateUserHasRole(loggedInUser, UserRole.DATA_ANALYST, UserRole.MANAGER);
    }

    private void parseParameters(List<String> parameters) {
        role = ParsingHelpers.tryParseEnum(parameters.get(0),
                UserRole.class,
                String.format(CommandsConstants.INVALID_ROLE_MESSAGE,parameters.get(0)));
    }

    private List<User> getUsersWithRole(UserRole role) {
        return users.stream()
                .filter(u -> u.getRole() == role)
                .toList();
    }
}
