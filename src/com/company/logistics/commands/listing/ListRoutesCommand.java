package com.company.logistics.commands.listing;

import com.company.logistics.commands.CommandsConstants;
import com.company.logistics.commands.contracts.Command;
import com.company.logistics.core.contracts.LogisticsRepository;
import com.company.logistics.models.contracts.Route;
import com.company.logistics.utils.ListingHelpers;
import com.company.logistics.utils.PrintConstants;

import java.util.List;

public class ListRoutesCommand implements Command {

    private final LogisticsRepository repository;

    public ListRoutesCommand(LogisticsRepository repository) {
        this.repository = repository;
    }

    public String execute(List<String> parameters) {
        List<Route> routes = repository.getRoutes();
        if (routes.isEmpty()) {
            return String.format(CommandsConstants.NONE_FOUND_MESSAGE, "routes");
        }

        return PrintConstants.LINE_BREAK + "\n" + ListingHelpers.elementsToString(routes);
    }
}
