package com.company.logistics.commands.removals;

import com.company.logistics.commands.CommandsConstants;
import com.company.logistics.commands.contracts.Command;
import com.company.logistics.enums.PackageStatus;
import com.company.logistics.enums.UserRole;
import com.company.logistics.models.contracts.User;
import com.company.logistics.repositories.contracts.UserRepository;
import com.company.logistics.services.assignment.AssignmentService;
import com.company.logistics.utils.ParsingHelpers;
import com.company.logistics.utils.ValidationHelper;

import java.util.List;
import java.util.stream.Collectors;

public class RemoveTruckFromRouteCommand implements Command {

    public static final int EXPECTED_NUMBER_OF_ARGUMENTS = 2;

    private final AssignmentService assignmentService;
    private final User loggedInUser;

    private int truckId;
    private int routeId;

    public RemoveTruckFromRouteCommand(AssignmentService assignmentService, UserRepository userRepository) {
        this.assignmentService = assignmentService;
        this.loggedInUser = userRepository.getLoggedInUser();
    }
    @Override
    public String execute(List<String> parameters) {
        validateLoggedInUser();

        ValidationHelper.validateArgumentsCount(parameters,EXPECTED_NUMBER_OF_ARGUMENTS);
        
        parseParameters(parameters);

        List<Integer> statusChangedIDs = assignmentService.removeTruckFromRoute(this.truckId , this.routeId);

        StringBuilder sb = new StringBuilder();
        sb.append(String.format(CommandsConstants.REMOVED_TRUCK_FROM_ROUTE, "Truck", truckId, routeId));

        appendPackageIDsLine(sb, statusChangedIDs);
        return sb.toString();
    }

    private void validateLoggedInUser() {
        ValidationHelper.validateUserHasRole(loggedInUser, UserRole.EMPLOYEE);
    }

    private void parseParameters(List<String> parameters) {
        this.truckId= ParsingHelpers.tryParseInt(parameters.get(0),"Truck ID");
        this.routeId= ParsingHelpers.tryParseInt(parameters.get(1),"Route ID");
    }

    private void appendPackageIDsLine(StringBuilder sb, List<Integer> ids) {
        if (ids.isEmpty()) { return; }

        sb.append(String.format("\nPackage's {IDs} now %s: ", PackageStatus.PENDING))
                .append(ids.stream()
                        .map(Object::toString)
                        .collect(Collectors.joining(", "))
                );
    }
}
