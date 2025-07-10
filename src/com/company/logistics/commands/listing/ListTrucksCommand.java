package com.company.logistics.commands.listing;

import com.company.logistics.commands.CommandsConstants;
import com.company.logistics.commands.contracts.Command;
import com.company.logistics.enums.UserRole;
import com.company.logistics.models.contracts.Truck;
import com.company.logistics.models.contracts.User;
import com.company.logistics.repositories.contracts.TruckRepository;
import com.company.logistics.repositories.contracts.UserRepository;
import com.company.logistics.utils.ListingHelpers;
import com.company.logistics.utils.PrintConstants;
import com.company.logistics.utils.ValidationHelper;

import java.util.List;

public class ListTrucksCommand implements Command {

    private final List<Truck> trucks;
    private final User loggedInUser;

    public ListTrucksCommand(TruckRepository truckRepository, UserRepository userRepository) {
        trucks = truckRepository.getTrucks();
        this.loggedInUser = userRepository.getLoggedInUser();
    }

    public String execute(List<String> parameters) {
        validateLoggedInUser();

        if (trucks.isEmpty()) {
            return String.format(CommandsConstants.NONE_FOUND_MESSAGE, "trucks");
        }

        return PrintConstants.LINE_BREAK + "\n" + ListingHelpers.elementsToString(trucks);
    }

    private void validateLoggedInUser() {
        ValidationHelper.validateUserHasRole(loggedInUser, UserRole.EMPLOYEE, UserRole.DATA_ANALYST);
    }
}
