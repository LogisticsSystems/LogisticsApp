package com.company.logistics.commands.assigning;

import com.company.logistics.commands.CommandsConstants;
import com.company.logistics.commands.contracts.Command;
import com.company.logistics.services.assignment.AssignmentService;
import com.company.logistics.utils.ParsingHelpers;
import com.company.logistics.utils.ValidationHelper;

import java.util.List;

public class AssignTruckToRouteCommand implements Command {
    public static final int EXPECTED_NUMBER_OF_ARGUMENTS = 2;

    private final AssignmentService assignmentService;

    private int truckId;
    private int routeId;

    public AssignTruckToRouteCommand(AssignmentService assignmentService) {

        this.assignmentService = assignmentService;
    }

    @Override
    public String execute(List<String> parameters) {
        ValidationHelper.validateArgumentsCount(parameters, EXPECTED_NUMBER_OF_ARGUMENTS);

        parseParameters(parameters);

        assignmentService.assignTruckToRoute(this.truckId, this.routeId);

        return String.format(CommandsConstants.ASSIGNED_TRUCK_ROUTE, "Truck", this.truckId, this.routeId);
    }

    private void parseParameters(List<String> parameters) {
        this.truckId = ParsingHelpers.tryParseInt(parameters.get(0), "Truck ID");
        this.routeId = ParsingHelpers.tryParseInt(parameters.get(1), "Route ID");
    }
}
