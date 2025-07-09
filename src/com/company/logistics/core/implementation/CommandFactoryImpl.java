package com.company.logistics.core.implementation;

import com.company.logistics.commands.assigning.AssignPackageToRouteCommand;
import com.company.logistics.commands.assigning.AssignTruckToRouteCommand;
import com.company.logistics.commands.contracts.Command;
import com.company.logistics.commands.creation.CreatePackageCommand;
import com.company.logistics.commands.creation.CreateRouteCommand;
import com.company.logistics.commands.delivery.DeliverPackageCommand;
import com.company.logistics.commands.listing.*;
import com.company.logistics.commands.queries.FindRoute;
import com.company.logistics.commands.queries.ViewPackageWithIDCommand;
import com.company.logistics.commands.removals.RemovePackageFromRouteCommand;
import com.company.logistics.commands.removals.RemoveTruckFromRouteCommand;
import com.company.logistics.commands.queries.HelpCommand;
import com.company.logistics.commands.speed.ChangeSpeedModelCommand;
import com.company.logistics.commands.speed.ViewSpeedModelCommand;
import com.company.logistics.core.context.EngineContext;
import com.company.logistics.core.contracts.CommandFactory;
import com.company.logistics.exceptions.InvalidUserInputException;
import com.company.logistics.repositories.contracts.PackageRepository;
import com.company.logistics.repositories.contracts.RouteRepository;
import com.company.logistics.repositories.contracts.TruckRepository;
import com.company.logistics.services.assignment.AssignmentService;
import com.company.logistics.services.delivery.PackageDeliveryService;
import com.company.logistics.services.routing.computing.RouteRecalculatorService;
import com.company.logistics.services.routing.management.RouteCreationService;
import com.company.logistics.services.speeds.SpeedModelService;
import com.company.logistics.enums.CommandType;
import com.company.logistics.utils.ValidationHelper;

public class CommandFactoryImpl implements CommandFactory {
    private static final String INVALID_COMMAND = "Invalid command name: %s";

    private final PackageRepository       packageRepository;
    private final RouteRepository         routeRepository;
    private final TruckRepository         truckRepository;
    private final AssignmentService       assignmentService;
    private final PackageDeliveryService  deliveryService;
    private final RouteCreationService    routeCreationService;
    private final SpeedModelService       speedModelService;
    private final RouteRecalculatorService routeRecalculatorService;

    public CommandFactoryImpl(EngineContext engineContext) {
        this.packageRepository        = engineContext.getPackageRepository();
        this.routeRepository          = engineContext.getRouteRepository();
        this.truckRepository          = engineContext.getTruckRepository();
        this.assignmentService        = engineContext.getAssignmentService();
        this.deliveryService          = engineContext.getDeliveryService();
        this.routeCreationService     = engineContext.getRouteCreationService();
        this.speedModelService        = engineContext.getSpeedModelService();
        this.routeRecalculatorService = engineContext.getRouteRecalculatorService();
    }

    @Override
    public Command createCommandFromCommandName(String commandTypeAsString) {
        ValidationHelper.validateNotNull(commandTypeAsString, "commandName");

        CommandType type;
        try {
            type = CommandType.valueOf(commandTypeAsString.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new InvalidUserInputException(String.format(INVALID_COMMAND, commandTypeAsString));
        }

        return switch (type) {
            // ——— CRUD / creation ———
            case CREATEPACKAGE    -> new CreatePackageCommand(packageRepository);
            case CREATEROUTE      -> new CreateRouteCommand(routeCreationService);

            // ——— Queries & listings ———
            case FINDROUTE                      -> new FindRoute(routeRepository);
            case VIEWPACKAGEWITHID              -> new ViewPackageWithIDCommand(packageRepository);
            case LISTPACKAGEINFO                -> new ListPackagesCommand(packageRepository);
            case LISTROUTEINFO                  -> new ListRoutesCommand(routeRepository);
            case LISTTRUCKINFO                  -> new ListTrucksCommand(truckRepository);
            case LISTROUTESWITHNOTRUCKASSIGNED  -> new ListRoutesWithNoAssignedTrucksCommand(routeRepository);
            case LISTPACKAGESWITHSTATUS         -> new ListPackagesWithStatusCommand(packageRepository);
            case HELP                           -> new HelpCommand();

            // ——— Assignment ———
            case ASSIGNPACKAGETOROUTE -> new AssignPackageToRouteCommand(assignmentService);
            case ASSIGNTRUCKTOROUTE   -> new AssignTruckToRouteCommand(assignmentService);

            // ——— Removals ———
            case REMOVETRUCKFROMROUTE -> new RemoveTruckFromRouteCommand(assignmentService);
            case REMOVEPACKAGEFROMROUTE -> new RemovePackageFromRouteCommand(assignmentService);

            // ——— Delivery ———
            case DELIVERPACKAGE   -> new DeliverPackageCommand(deliveryService);

            // ——— Speed model swap ———
            case CHANGESPEEDMODEL -> new ChangeSpeedModelCommand(routeRepository, speedModelService, routeRecalculatorService);
            case VIEWSPEEDMODEL   -> new ViewSpeedModelCommand(speedModelService);
        };
    }
}
