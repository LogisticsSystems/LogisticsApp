package com.company.logistics.commands.creation;

import com.company.logistics.commands.CommandsConstants;
import com.company.logistics.commands.contracts.Command;
import com.company.logistics.core.services.routing.management.RouteCreationService;
import com.company.logistics.enums.City;
import com.company.logistics.models.contracts.Route;
import com.company.logistics.utils.ParsingHelpers;
import com.company.logistics.utils.ValidationHelper;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class CreateRouteCommand implements Command {
    public static final int EXPECTED_NUMBER_OF_ARGUMENTS = 2;
    public static final String ROUTE_CREATED_MESSAGE = "Route was created with ID %d.";

    private final RouteCreationService creationService;

    private List<City> locations;
    private LocalDateTime departureTime;

    public CreateRouteCommand(RouteCreationService creationService) {
        this.creationService = creationService;
    }

    @Override
    public String execute(List<String> parameters) {
        ValidationHelper.validateArgumentsCount(parameters, EXPECTED_NUMBER_OF_ARGUMENTS);

        parseParameters(parameters);

        Route createdRoute = creationService.createRoute(locations, departureTime);

        return String.format(ROUTE_CREATED_MESSAGE, createdRoute.getId());
    }

    private void parseParameters(List<String> parameters) {
        this.locations = Arrays.stream(parameters.get(0)
                        .split(","))
                .map(c -> ParsingHelpers.tryParseEnum(c, City.class, CommandsConstants.INVALID_CITY_MESSAGE))
                .toList();
        this.departureTime = ParsingHelpers.tryParseDateTime(parameters.get(1));
    }
}
