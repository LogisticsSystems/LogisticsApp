package com.company.logistics.commands.listing;

import com.company.logistics.commands.CommandsConstants;
import com.company.logistics.commands.contracts.Command;
import com.company.logistics.models.contracts.Route;
import com.company.logistics.repositories.contracts.RouteRepository;
import com.company.logistics.utils.ListingHelpers;
import com.company.logistics.utils.PrintConstants;

import java.util.List;

public class ListRoutesWithNoAssignedTrucksCommand implements Command {

    private final List<Route> routes;

    public ListRoutesWithNoAssignedTrucksCommand(RouteRepository routeRepository) {
        routes = routeRepository.getRoutes();
    }


    @Override
    public String execute(List<String> parameters) {

        List<Route> routesWithoutTruck = getRoutesWithNoAssignedTrucks(routes);
        if(routesWithoutTruck.isEmpty()){
            return String.format(CommandsConstants.NO_ROUTES_WITHOUT_TRUCKS);
        }
        return PrintConstants.LINE_BREAK + "\n" + ListingHelpers.elementsToString(routesWithoutTruck);

    }


    private List<Route> getRoutesWithNoAssignedTrucks(List<Route> routes) {
        return routes.stream()
                .filter(r -> r.getAssignedTruck().isEmpty())
                .toList();
    }
}
