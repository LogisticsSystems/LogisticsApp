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

public class ListRoutesWithNoAssignedTrucksCommand implements Command {

    private final List<Route> routes;
    private final User loggedInUser;

    public ListRoutesWithNoAssignedTrucksCommand(RouteRepository routeRepository, UserRepository userRepository) {
        routes = routeRepository.getRoutes();
        this.loggedInUser = userRepository.getLoggedInUser();
    }


    @Override
    public String execute(List<String> parameters) {
        validateLoggedInUser();

        List<Route> routesWithoutTruck = getRoutesWithNoAssignedTrucks(routes);
        if(routesWithoutTruck.isEmpty()){
            return String.format(CommandsConstants.NO_ROUTES_WITHOUT_TRUCKS);
        }
        return PrintConstants.LINE_BREAK + "\n" + ListingHelpers.elementsToString(routesWithoutTruck);

    }

    private void validateLoggedInUser() {
        ValidationHelper.validateUserHasRole(loggedInUser, UserRole.EMPLOYEE, UserRole.DATA_ANALYST);
    }

    private List<Route> getRoutesWithNoAssignedTrucks(List<Route> routes) {
        return routes.stream()
                .filter(r -> r.getAssignedTruck() == null)
                .toList();
    }
}
