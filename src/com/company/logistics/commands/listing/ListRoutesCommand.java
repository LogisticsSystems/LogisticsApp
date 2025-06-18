package com.company.logistics.commands.listing;

import com.company.logistics.commands.CommandsConstants;
import com.company.logistics.commands.contracts.Command;
import com.company.logistics.core.contracts.LogisticsRepository;
import com.company.logistics.models.contracts.Route;
import com.company.logistics.utils.ListingHelpers;

import java.util.List;

public class ListRoutesCommand implements Command {

    private final List<Route> routes;

    public ListRoutesCommand(LogisticsRepository repository) {
        routes = repository.getRoutes();
    }

    public String execute(List<String> parameters) {
        if (routes.isEmpty()) {
            return String.format(CommandsConstants.NONE_FOUND_MESSAGE, "routes");
        }

        return ListingHelpers.elementsToString(routes);
    }
}
