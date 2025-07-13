package com.company.logistics.commands.querying;

import com.company.logistics.commands.contracts.Command;
import com.company.logistics.enums.UserRole;
import com.company.logistics.models.contracts.User;
import com.company.logistics.repositories.contracts.PackageRepository;
import com.company.logistics.repositories.contracts.UserRepository;
import com.company.logistics.utils.ParsingHelpers;
import com.company.logistics.utils.PrintConstants;
import com.company.logistics.utils.ValidationHelper;

import java.util.List;

public class ViewPackageWithIDCommand implements Command {

    public static final int EXPECTED_NUMBER_OF_ARGUMENTS = 1;

    private final PackageRepository packageRepository;
    private final User loggedInUser;

    private int packageId;

    public ViewPackageWithIDCommand(PackageRepository packageRepository, UserRepository userRepository) {
        this.packageRepository = packageRepository;
        this.loggedInUser = userRepository.getLoggedInUser();
    }

    @Override
    public String execute(List<String> parameters) {
        validateLoggedInUser();

        ValidationHelper.validateArgumentsCount(parameters,EXPECTED_NUMBER_OF_ARGUMENTS);

        parseParameters(parameters);

        return PrintConstants.LINE_BREAK + "\n" + packageRepository.findPackageById(packageId).print();

    }

    private void validateLoggedInUser() {
        ValidationHelper.validateUserHasRole(loggedInUser, UserRole.EMPLOYEE, UserRole.DATA_ANALYST);
    }

    private void parseParameters(List<String> parameters) {
        packageId= ParsingHelpers.tryParseInt(parameters.get(0),"Package ID");
    }
}
