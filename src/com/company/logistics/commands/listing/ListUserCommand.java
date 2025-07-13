package com.company.logistics.commands.listing;

import com.company.logistics.commands.CommandsConstants;
import com.company.logistics.commands.contracts.Command;
import com.company.logistics.enums.UserRole;
import com.company.logistics.models.contracts.Route;
import com.company.logistics.models.contracts.User;
import com.company.logistics.repositories.contracts.UserRepository;
import com.company.logistics.utils.ListingHelpers;
import com.company.logistics.utils.PrintConstants;
import com.company.logistics.utils.ValidationHelper;

import java.util.List;

public class ListUserCommand implements Command {
    private final UserRepository userRepository;
    private final User loggedInUser;

    public ListUserCommand(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.loggedInUser = userRepository.getLoggedInUser();
    }

    public String execute(List<String> parameters) {
        validateLoggedInUser();

        List<User> users = userRepository.getUsers();
        if (users.isEmpty()) {
            return String.format(CommandsConstants.NONE_FOUND_MESSAGE, "users");
        }

        return PrintConstants.LINE_BREAK + "\n" + ListingHelpers.elementsToString(users);
    }

    private void validateLoggedInUser() {
        ValidationHelper.validateUserHasRole(loggedInUser, UserRole.DATA_ANALYST, UserRole.MANAGER);
    }
}
