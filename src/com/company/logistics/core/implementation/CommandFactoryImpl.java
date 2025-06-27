package com.company.logistics.core.implementation;

import com.company.logistics.commands.assigning.AssignPackageToRouteCommand;
import com.company.logistics.commands.assigning.AssignTruckToRouteCommand;
import com.company.logistics.commands.contracts.Command;
import com.company.logistics.commands.creation.CreatePackageCommand;
import com.company.logistics.commands.creation.CreateRouteCommand;
import com.company.logistics.commands.delivery.DeliverPackageCommand;
import com.company.logistics.commands.listing.ListPackagesCommand;
import com.company.logistics.commands.listing.ListRoutesCommand;
import com.company.logistics.commands.listing.ListTrucksCommand;
import com.company.logistics.commands.queries.FindRoute;
import com.company.logistics.commands.speed.ChangeSpeedModelCommand;
import com.company.logistics.core.context.EngineContext;
import com.company.logistics.core.contracts.CommandFactory;
import com.company.logistics.core.contracts.LogisticsRepository;
import com.company.logistics.core.services.delivery.PackageDeliveryService;
import com.company.logistics.enums.CommandType;
import com.company.logistics.utils.ValidationHelper;

public class CommandFactoryImpl implements CommandFactory {
    private static final String INVALID_COMMAND = "Invalid command name: %s";
    private final EngineContext engineContext;

    public CommandFactoryImpl(EngineContext engineContext) {
        this.engineContext = engineContext;
    }

    @Override
    public Command createCommandFromCommandName(String commandTypeAsString) {
        ValidationHelper.validateNotNull(commandTypeAsString, "commandName");

        LogisticsRepository repository = engineContext.getRepository();
        PackageDeliveryService deliveryService = engineContext.getDeliveryService();

        ValidationHelper.validateNotNull(repository, "repository");
        ValidationHelper.validateNotNull(deliveryService, "delivery service");

        CommandType type;
        try {
            type = CommandType.valueOf(commandTypeAsString.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException(String.format(INVALID_COMMAND, commandTypeAsString));
        }

        return switch (type) {
            case CREATEPACKAGE       -> new CreatePackageCommand(repository);
            case CREATEROUTE         -> new CreateRouteCommand(repository);
            case ASSIGNPACKAGETOROUTE-> new AssignPackageToRouteCommand(repository);
            case ASSIGNTRUCKTOROUTE  -> new AssignTruckToRouteCommand(repository);
            case FINDROUTE           -> new FindRoute(repository);
            case LISTPACKAGEINFO     -> new ListPackagesCommand(repository);
            case LISTROUTEINFO       -> new ListRoutesCommand(repository);
            case LISTTRUCKINFO       -> new ListTrucksCommand(repository);

            case DELIVERPACKAGE      -> new DeliverPackageCommand(deliveryService);

            case CHANGESPEEDMODEL    -> new ChangeSpeedModelCommand(engineContext);
        };
    }
}
