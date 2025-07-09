package com.company.logistics.commands.listing;

import com.company.logistics.commands.CommandsConstants;
import com.company.logistics.commands.contracts.Command;
import com.company.logistics.models.contracts.DeliveryPackage;
import com.company.logistics.repositories.contracts.PackageRepository;
import com.company.logistics.utils.ListingHelpers;
import com.company.logistics.utils.PrintConstants;

import java.util.List;

public class ListPackagesCommand implements Command {

    private final List<DeliveryPackage> packages;

    public ListPackagesCommand(PackageRepository packageRepository) {
        packages = packageRepository.getPackages();
    }

    public String execute(List<String> parameters) {
        if (packages.isEmpty()) {
            return String.format(CommandsConstants.NONE_FOUND_MESSAGE, "packages");
        }

        return PrintConstants.LINE_BREAK + "\n" + ListingHelpers.elementsToString(packages);
    }
}
