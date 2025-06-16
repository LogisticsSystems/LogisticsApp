package com.company.logistics.commands.listing;

import com.company.logistics.commands.CommandsConstants;
import com.company.logistics.commands.contracts.Command;
import com.company.logistics.core.contracts.LogisticsRepository;
import com.company.logistics.models.contracts.Truck;
import com.company.logistics.utils.ListingHelpers;

import java.util.List;

public class ListTrucksCommand implements Command {

    private final List<Truck> trucks;

    public ListTrucksCommand(LogisticsRepository agencyRepository) {
        trucks = agencyRepository.getTrucks();
    }

    public String execute(List<String> parameters) {
        if (trucks.isEmpty()) {
            return String.format(CommandsConstants.NONE_FOUND_MESSAGE, "trucks");
        }

        return ListingHelpers.elementsToString(trucks);
    }
}
