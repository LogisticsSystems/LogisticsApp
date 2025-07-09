package com.company.logistics.commands.listing;

import com.company.logistics.commands.CommandsConstants;
import com.company.logistics.commands.contracts.Command;
import com.company.logistics.models.contracts.Truck;
import com.company.logistics.repositories.contracts.TruckRepository;
import com.company.logistics.utils.ListingHelpers;
import com.company.logistics.utils.PrintConstants;

import java.util.List;

public class ListTrucksCommand implements Command {

    private final List<Truck> trucks;

    public ListTrucksCommand(TruckRepository truckRepository) {
        trucks = truckRepository.getTrucks();
    }

    public String execute(List<String> parameters) {
        if (trucks.isEmpty()) {
            return String.format(CommandsConstants.NONE_FOUND_MESSAGE, "trucks");
        }

        return PrintConstants.LINE_BREAK + "\n" + ListingHelpers.elementsToString(trucks);
    }
}
