package com.company.logistics.commands.listing;

import com.company.logistics.commands.CommandsConstants;
import com.company.logistics.commands.contracts.Command;
import com.company.logistics.core.contracts.LogisticsRepository;
import com.company.logistics.models.contracts.DeliveryPackage;
import com.company.logistics.utils.ListingHelpers;

import java.util.List;

public class ListPackagesCommand implements Command {

    private final List<DeliveryPackage> packages;

    public ListPackagesCommand(LogisticsRepository repository) {
        packages = repository.getPackages();
    }

    public String execute(List<String> parameters) {
        if (packages.isEmpty()) {
            return String.format(CommandsConstants.NONE_FOUND_MESSAGE, "packages");
        }

        return ListingHelpers.elementsToString(packages);
    }
}
