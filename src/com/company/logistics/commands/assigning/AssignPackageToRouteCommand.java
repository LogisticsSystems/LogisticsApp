package com.company.logistics.commands.assigning;

import com.company.logistics.commands.CommandsConstants;
import com.company.logistics.commands.contracts.Command;
import com.company.logistics.services.assignment.AssignmentService;
import com.company.logistics.utils.ParsingHelpers;
import com.company.logistics.utils.ValidationHelper;

import java.util.List;

public class AssignPackageToRouteCommand implements Command {
    public static final int EXPECTED_NUMBER_OF_ARGUMENTS = 2;

    private final AssignmentService assignmentService;

    private int packageId;
    private int routeId;

    public AssignPackageToRouteCommand(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @Override
    public String execute(List<String> parameters) {
        ValidationHelper.validateArgumentsCount(parameters, EXPECTED_NUMBER_OF_ARGUMENTS);

        parseParameters(parameters);

        assignmentService.assignPackageToRoute(this.packageId, this.routeId);

        return String.format(CommandsConstants.ASSIGNED_TO_ROUTE_MESSAGE, "Package", this.packageId, this.routeId);
    }

    private void parseParameters(List<String> parameters) {
        this.packageId = ParsingHelpers.tryParseInt(parameters.get(0), "package id");
        this.routeId = ParsingHelpers.tryParseInt(parameters.get(1), "route id");
    }
}
