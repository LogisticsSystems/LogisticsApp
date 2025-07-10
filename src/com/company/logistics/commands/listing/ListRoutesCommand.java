package com.company.logistics.commands.listing;

import com.company.logistics.commands.CommandsConstants;
import com.company.logistics.commands.contracts.Command;
import com.company.logistics.enums.UserRole;
import com.company.logistics.models.contracts.Route;
import com.company.logistics.models.contracts.User;
import com.company.logistics.repositories.contracts.RouteRepository;
import com.company.logistics.repositories.contracts.UserRepository;
import com.company.logistics.utils.ListingHelpers;
import com.company.logistics.utils.PrintConstants;
import com.company.logistics.utils.ValidationHelper;

import java.util.List;

public class ListRoutesCommand implements Command {

    private final RouteRepository routeRepository;
    private final User loggedInUser;

    public ListRoutesCommand(RouteRepository routeRepository, UserRepository userRepository) {
        this.routeRepository = routeRepository;
        this.loggedInUser = userRepository.getLoggedInUser();
    }

    public String execute(List<String> parameters) {
        validateLoggedInUser();

        List<Route> routes = routeRepository.getRoutes();
        if (routes.isEmpty()) {
            return String.format(CommandsConstants.NONE_FOUND_MESSAGE, "routes");
        }

        return PrintConstants.LINE_BREAK + "\n" + ListingHelpers.elementsToString(routes);
    }

    private void validateLoggedInUser() {
        ValidationHelper.validateUserHasRole(loggedInUser, UserRole.EMPLOYEE, UserRole.DATA_ANALYST);
    }
}
