package com.company.logistics.commands.listing;

import com.company.logistics.commands.CommandsConstants;
import com.company.logistics.commands.contracts.Command;
import com.company.logistics.enums.PackageStatus;
import com.company.logistics.enums.UserRole;
import com.company.logistics.models.contracts.DeliveryPackage;
import com.company.logistics.models.contracts.User;
import com.company.logistics.repositories.contracts.PackageRepository;
import com.company.logistics.repositories.contracts.UserRepository;
import com.company.logistics.utils.ListingHelpers;
import com.company.logistics.utils.ParsingHelpers;
import com.company.logistics.utils.PrintConstants;
import com.company.logistics.utils.ValidationHelper;

import java.util.List;

public class ListPackagesWithStatusCommand implements Command {

    public static final int EXPECTED_NUMBER_OF_ARGUMENTS = 1;

    private PackageStatus status;
    private final List<DeliveryPackage> packages;
    private final User loggedInUser;

    public ListPackagesWithStatusCommand(PackageRepository packageRepository, UserRepository userRepository) {
        this.packages = packageRepository.getPackages();
        this.loggedInUser = userRepository.getLoggedInUser();
    }

    @Override
    public String execute(List<String> parameters) {
        validateLoggedInUser();

        ValidationHelper.validateArgumentsCount(parameters,EXPECTED_NUMBER_OF_ARGUMENTS);

        parseParameters(parameters);

        List<DeliveryPackage> packagesWithDesiredStatus = getPackagesWithStatus(status);

        if(packagesWithDesiredStatus.isEmpty()){

            return String.format(CommandsConstants.NO_PACKAGES_WITH_STATUS, this.status);
        }

        return PrintConstants.LINE_BREAK + "\n" + ListingHelpers.elementsToString(packagesWithDesiredStatus);

    }

    private void validateLoggedInUser() {
        ValidationHelper.validateUserHasRole(loggedInUser, UserRole.EMPLOYEE, UserRole.DATA_ANALYST);
    }

    private void parseParameters(List<String> parameters) {
        status = ParsingHelpers.tryParseEnum(parameters.get(0),
                PackageStatus.class,
                String.format(CommandsConstants.INVALID_STATUS_MESSEGE,parameters.get(0)));
    }

    private List<DeliveryPackage> getPackagesWithStatus(PackageStatus status) {
        return packages.stream()
                .filter(pkg -> pkg.getStatus() == status)
                .toList();
    }

}
