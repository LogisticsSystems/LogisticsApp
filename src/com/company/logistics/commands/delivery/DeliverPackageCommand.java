package com.company.logistics.commands.delivery;

import com.company.logistics.commands.CommandsConstants;
import com.company.logistics.commands.contracts.Command;
import com.company.logistics.core.contracts.LogisticsRepository;
import com.company.logistics.enums.City;
import com.company.logistics.models.contracts.DeliveryPackage;
import com.company.logistics.utils.ParsingHelpers;
import com.company.logistics.utils.ValidationHelper;

import java.util.Arrays;
import java.util.List;

public class DeliverPackageCommand implements Command {
    public static final int EXPECTED_NUMBER_OF_ARGUMENTS = 1;
    public static final String PACKAGE_DELIVERED_MESSAGE = "Package %d has been delivered.";

    private int packageId;

    private final LogisticsRepository repository;

    public DeliverPackageCommand(LogisticsRepository repository) {
        this.repository = repository;
    }

    @Override
    public String execute(List<String> parameters) {
        ValidationHelper.validateArgumentsCount(parameters, EXPECTED_NUMBER_OF_ARGUMENTS);

        parseParameters(parameters);

        DeliveryPackage pkg = repository.deliverPackage(this.packageId);


        return String.format(PACKAGE_DELIVERED_MESSAGE, this.packageId);
    }

    private void parseParameters(List<String> parameters) {
        this.packageId = ParsingHelpers.tryParseInt(parameters.get(0), "package ID");
    }
}
