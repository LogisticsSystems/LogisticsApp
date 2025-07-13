package com.company.logistics.commands.querying;

import com.company.logistics.commands.CommandsConstants;
import com.company.logistics.commands.contracts.Command;
import com.company.logistics.enums.City;
import com.company.logistics.enums.UserRole;
import com.company.logistics.models.contracts.Route;
import com.company.logistics.models.contracts.User;
import com.company.logistics.repositories.contracts.RouteRepository;
import com.company.logistics.repositories.contracts.UserRepository;
import com.company.logistics.utils.ListingHelpers;
import com.company.logistics.utils.ParsingHelpers;
import com.company.logistics.utils.ValidationHelper;

import java.util.List;

public class FindRouteCommand implements Command {
    public static final int EXPECTED_NUMBER_OF_ARGUMENTS = 2;

    private final RouteRepository routeRepository;
    private final User loggedInUser;

    private City startLocation;
    private City endLocation;

    public FindRouteCommand(RouteRepository routeRepository, UserRepository userRepository) {
        this.routeRepository = routeRepository;
        this.loggedInUser = userRepository.getLoggedInUser();
    }


    @Override
    public String execute(List<String> parameters) {
        validateLoggedInUser();

        ValidationHelper.validateArgumentsCount(parameters, EXPECTED_NUMBER_OF_ARGUMENTS);

        parseParameters(parameters);

        List<Route> routes = routeRepository.findRoutes(this.startLocation, this.endLocation);

        if (routes.isEmpty()) {
            return String.format(CommandsConstants.NO_MATCHING_ROUTES_MESSAGE, this.startLocation, this.endLocation);
        }

        return ListingHelpers.elementsToString(routes);
    }

    private void validateLoggedInUser() {
        ValidationHelper.validateUserHasRole(loggedInUser, UserRole.EMPLOYEE);
    }

    private void parseParameters(List<String> parameters) {
        this.startLocation = ParsingHelpers.tryParseEnum(parameters.get(0),
                City.class);
        this.endLocation = ParsingHelpers.tryParseEnum(parameters.get(1),
                City.class);
    }



}
