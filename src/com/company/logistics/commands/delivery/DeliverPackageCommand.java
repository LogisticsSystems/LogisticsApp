package com.company.logistics.commands.delivery;

import com.company.logistics.commands.contracts.Command;
import com.company.logistics.dto.PackageSnapshot;
import com.company.logistics.enums.UserRole;
import com.company.logistics.models.contracts.User;
import com.company.logistics.repositories.contracts.UserRepository;
import com.company.logistics.services.delivery.PackageDeliveryService;
import com.company.logistics.utils.ParsingHelpers;
import com.company.logistics.utils.ValidationHelper;

import java.util.List;

public class DeliverPackageCommand implements Command {
    public static final int EXPECTED_NUMBER_OF_ARGUMENTS = 1;
    public static final String PACKAGE_DELIVERED_MESSAGE = "Package %d has been delivered. Status: %s";

    private final PackageDeliveryService deliveryService;
    private final User loggedInUser;

    private int packageId;

    public DeliverPackageCommand(PackageDeliveryService service, UserRepository userRepository) {
        this.deliveryService = service;
        this.loggedInUser = userRepository.getLoggedInUser();
    }

    @Override
    public String execute(List<String> parameters) {
        validateLoggedInUser();

        ValidationHelper.validateArgumentsCount(parameters, EXPECTED_NUMBER_OF_ARGUMENTS);

        parseParameters(parameters);

        PackageSnapshot pkg = deliveryService.deliverPackage(this.packageId);

        return String.format(PACKAGE_DELIVERED_MESSAGE, this.packageId, pkg.status());
    }

    private void validateLoggedInUser() {
        ValidationHelper.validateUserHasRole(loggedInUser, UserRole.EMPLOYEE);
    }

    private void parseParameters(List<String> parameters) {
        this.packageId = ParsingHelpers.tryParseInt(parameters.get(0), "Package ID");
    }
}
