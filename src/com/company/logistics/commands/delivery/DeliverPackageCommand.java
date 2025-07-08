package com.company.logistics.commands.delivery;

import com.company.logistics.commands.contracts.Command;
import com.company.logistics.services.delivery.PackageDeliveryService;
import com.company.logistics.models.contracts.DeliveryPackage;
import com.company.logistics.utils.ParsingHelpers;
import com.company.logistics.utils.ValidationHelper;

import java.util.List;

public class DeliverPackageCommand implements Command {
    public static final int EXPECTED_NUMBER_OF_ARGUMENTS = 1;
    public static final String PACKAGE_DELIVERED_MESSAGE = "Package %d has been delivered.";

    private final PackageDeliveryService deliveryService;

    private int packageId;

    public DeliverPackageCommand(PackageDeliveryService service) {
        this.deliveryService = service;
    }

    @Override
    public String execute(List<String> parameters) {
        ValidationHelper.validateArgumentsCount(parameters, EXPECTED_NUMBER_OF_ARGUMENTS);

        parseParameters(parameters);

        DeliveryPackage pkg = deliveryService.deliverPackage(this.packageId);


        return String.format(PACKAGE_DELIVERED_MESSAGE, this.packageId);
    }

    private void parseParameters(List<String> parameters) {
        this.packageId = ParsingHelpers.tryParseInt(parameters.get(0), "Package ID");
    }
}
