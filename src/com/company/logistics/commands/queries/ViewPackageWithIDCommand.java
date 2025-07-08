package com.company.logistics.commands.queries;

import com.company.logistics.commands.contracts.Command;
import com.company.logistics.core.contracts.LogisticsRepository;
import com.company.logistics.utils.ParsingHelpers;
import com.company.logistics.utils.PrintConstants;
import com.company.logistics.utils.ValidationHelper;

import java.util.List;

public class ViewPackageWithIDCommand implements Command {

    public static final int EXPECTED_NUMBER_OF_ARGUMENTS = 1;

    private final LogisticsRepository repository;

    private int packageId;

    public ViewPackageWithIDCommand(LogisticsRepository repository) {
        this.repository=repository;
    }

    @Override
    public String execute(List<String> parameters) {
        ValidationHelper.validateArgumentsCount(parameters,EXPECTED_NUMBER_OF_ARGUMENTS);

        parseParameters(parameters);

        return PrintConstants.LINE_BREAK + "\n" + repository.findPackageById(packageId).print();

    }

    private void parseParameters(List<String> parameters) {
        packageId= ParsingHelpers.tryParseInt(parameters.get(0),"Package ID");
    }


}
