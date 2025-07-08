package com.company.logistics.commands.listing;

import com.company.logistics.commands.CommandsConstants;
import com.company.logistics.commands.contracts.Command;
import com.company.logistics.core.contracts.LogisticsRepository;
import com.company.logistics.enums.PackageStatus;
import com.company.logistics.models.contracts.DeliveryPackage;
import com.company.logistics.utils.ListingHelpers;
import com.company.logistics.utils.ParsingHelpers;
import com.company.logistics.utils.PrintConstants;
import com.company.logistics.utils.ValidationHelper;

import java.util.List;

public class ListPackagesWithStatusCommand implements Command {

    private static final String NONE_FOUND_SENTENCE_TYPIFICATION ="packages with status %s";

    public static final int EXPECTED_NUMBER_OF_ARGUMENTS = 1;

    private PackageStatus status;
    private final List<DeliveryPackage> packages;

    public ListPackagesWithStatusCommand(LogisticsRepository repository) { this.packages = repository.getPackages(); }


    @Override
    public String execute(List<String> parameters) {
        ValidationHelper.validateArgumentsCount(parameters,EXPECTED_NUMBER_OF_ARGUMENTS);

        parseParameters(parameters);

        List<DeliveryPackage> packagesWithDesiredStatus = getPackagesWithStatus(status);

        if(packagesWithDesiredStatus.isEmpty()){

            return String.format(CommandsConstants.NONE_FOUND_MESSAGE, stringTypificationBuilder());
        }


        return PrintConstants.LINE_BREAK + "\n" + ListingHelpers.elementsToString(packagesWithDesiredStatus);

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

    private String stringTypificationBuilder() {
        return String.format(NONE_FOUND_SENTENCE_TYPIFICATION,this.status);
    }
}
