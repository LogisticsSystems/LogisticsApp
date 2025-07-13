package com.company.logistics.commands.removing;

import com.company.logistics.commands.CommandsConstants;
import com.company.logistics.commands.contracts.Command;
import com.company.logistics.dto.PackageSnapshot;
import com.company.logistics.enums.UserRole;
import com.company.logistics.models.contracts.User;
import com.company.logistics.repositories.contracts.UserRepository;
import com.company.logistics.services.assignment.AssignmentService;
import com.company.logistics.utils.ParsingHelpers;
import com.company.logistics.utils.ValidationHelper;

import java.util.List;

public class RemovePackageFromRouteCommand implements Command {

    public static final int EXPECTED_NUMBER_OF_ARGUMENTS = 2;

    private final AssignmentService assignmentService;
    private final User loggedInUser;

    private int packageId;
    private int routeId;

    public RemovePackageFromRouteCommand(AssignmentService assignmentService, UserRepository userRepository) {
        this.assignmentService = assignmentService;
        this.loggedInUser = userRepository.getLoggedInUser();
    }

    @Override
    public String execute(List<String> parameters) {
        validateLoggedInUser();

        ValidationHelper.validateArgumentsCount(parameters , EXPECTED_NUMBER_OF_ARGUMENTS);

        parseParameters(parameters);

        PackageSnapshot updated = assignmentService.removePackageFromRoute(this.packageId , this.routeId);

        return buildResultLine("Package", packageId, routeId, updated.status());
    }

    private void validateLoggedInUser() {
        ValidationHelper.validateUserHasRole(loggedInUser, UserRole.EMPLOYEE);
    }

    private void parseParameters(List<String> parameters) {
        packageId= ParsingHelpers.tryParseInt(parameters.get(0),"Package ID");
        routeId= ParsingHelpers.tryParseInt(parameters.get(1),"Route ID");
    }

    private String buildResultLine(String type, int id, int routeId, Enum<?> status) {
        return String.format(CommandsConstants.REMOVED_FROM_ROUTE, type, id, routeId, status);
    }
}
