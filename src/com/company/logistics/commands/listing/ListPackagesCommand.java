package com.company.logistics.commands.listing;

import com.company.logistics.commands.CommandsConstants;
import com.company.logistics.commands.contracts.Command;
import com.company.logistics.enums.UserRole;
import com.company.logistics.models.contracts.DeliveryPackage;
import com.company.logistics.models.contracts.User;
import com.company.logistics.repositories.contracts.PackageRepository;
import com.company.logistics.repositories.contracts.UserRepository;
import com.company.logistics.utils.ListingHelpers;
import com.company.logistics.utils.PrintConstants;
import com.company.logistics.utils.ValidationHelper;

import java.util.List;

public class ListPackagesCommand implements Command {

    private final List<DeliveryPackage> packages;
    private final User loggedInUser;


    public ListPackagesCommand(PackageRepository packageRepository, UserRepository userRepository) {
        packages = packageRepository.getPackages();
        this.loggedInUser = userRepository.getLoggedInUser();
    }

    public String execute(List<String> parameters) {
        validateLoggedInUser();

        if (packages.isEmpty()) {
            return String.format(CommandsConstants.NONE_FOUND_MESSAGE, "packages");
        }

        return PrintConstants.LINE_BREAK + "\n" + ListingHelpers.elementsToString(packages);
    }

    private void validateLoggedInUser() {
        ValidationHelper.validateUserHasRole(loggedInUser, UserRole.EMPLOYEE, UserRole.DATA_ANALYST);
    }
}
