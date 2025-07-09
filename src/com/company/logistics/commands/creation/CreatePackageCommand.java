package com.company.logistics.commands.creation;

import com.company.logistics.commands.CommandsConstants;
import com.company.logistics.commands.contracts.*;
import com.company.logistics.enums.City;
import com.company.logistics.models.contracts.DeliveryPackage;
import com.company.logistics.repositories.contracts.PackageRepository;
import com.company.logistics.utils.ParsingHelpers;
import com.company.logistics.utils.ValidationHelper;

import java.util.List;


public class CreatePackageCommand implements Command {
    public static final int EXPECTED_NUMBER_OF_ARGUMENTS = 4;

    private final PackageRepository packageRepository;

    private String contactInfo;
    private double weight;
    private City startLocation;
    private City endLocation;

    public CreatePackageCommand (PackageRepository packageRepository) {
        this.packageRepository = packageRepository;
    }

    @Override
    public String execute(List<String> parameters) {
        ValidationHelper.validateArgumentsCount(parameters, EXPECTED_NUMBER_OF_ARGUMENTS);

        parseParameters(parameters);

        DeliveryPackage createdPackage = packageRepository.createPackage(this.contactInfo, this.weight, this.startLocation, this.endLocation);

        return String.format(CommandsConstants.PACKAGE_CREATED_MESSAGE, createdPackage.getId());
    }

    private void parseParameters(List<String> parameters) {
        this.contactInfo = parameters.get(0);
        this.weight = ParsingHelpers.tryParseDouble(parameters.get(1), "Weight");
        this.startLocation = ParsingHelpers.tryParseEnum(parameters.get(2),
                City.class,
                String.format(CommandsConstants.INVALID_CITY_MESSAGE, parameters.get(2)));
        this.endLocation = ParsingHelpers.tryParseEnum(parameters.get(3),
                City.class,
                String.format(CommandsConstants.INVALID_CITY_MESSAGE, parameters.get(3)));
    }
}
