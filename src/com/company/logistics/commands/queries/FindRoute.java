package com.company.logistics.commands.queries;

import com.company.logistics.commands.CommandsConstants;
import com.company.logistics.commands.contracts.Command;
import com.company.logistics.core.contracts.LogisticsRepository;
import com.company.logistics.enums.City;
import com.company.logistics.models.contracts.Route;
import com.company.logistics.utils.ListingHelpers;
import com.company.logistics.utils.ParsingHelpers;
import com.company.logistics.utils.ValidationHelper;

import java.util.List;

public class FindRoute implements Command {
    public static final int EXPECTED_NUMBER_OF_ARGUMENTS = 2;

    private final LogisticsRepository repository;

    City startLocation;
    City endLocation;

    public FindRoute(LogisticsRepository repository) {
        this.repository = repository;
    }


    @Override
    public String execute(List<String> parameters) {
        ValidationHelper.validateArgumentsCount(parameters, EXPECTED_NUMBER_OF_ARGUMENTS);

        parseParameters(parameters);

        List<Route> routes = repository.findRoutes(this.startLocation, this.endLocation);

        if (routes.isEmpty()) {
            return String.format(CommandsConstants.NO_MATCHING_ROUTES_MESSAGE,
                    this.startLocation, this.endLocation);
        }
        else {
            return ListingHelpers.elementsToString(routes);
        }
    }

    private void parseParameters(List<String> parameters) {
        this.startLocation = ParsingHelpers.tryParseEnum(parameters.get(0),
                City.class,
                String.format(CommandsConstants.INVALID_CITY_MESSAGE, parameters.get(2)));
        this.endLocation = ParsingHelpers.tryParseEnum(parameters.get(1),
                City.class,
                String.format(CommandsConstants.INVALID_CITY_MESSAGE, parameters.get(3)));
    }



}
