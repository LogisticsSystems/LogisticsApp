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
import com.company.logistics.services.assignment.AssignmentService;
import com.company.logistics.services.delivery.PackageDeliveryService;
import com.company.logistics.services.routing.computing.RouteRecalculatorService;
import com.company.logistics.services.routing.management.RouteCreationService;
import com.company.logistics.services.speeds.SpeedModelService;
import com.company.logistics.enums.CommandType;
import com.company.logistics.utils.ValidationHelper;

public class CommandFactoryImpl implements CommandFactory {
    private static final String INVALID_COMMAND = "Invalid command name: %s";

    private final LogisticsRepository     repository;
    private final AssignmentService       assignmentService;
    private final PackageDeliveryService  deliveryService;
    private final RouteCreationService    routeCreationService;
    private final SpeedModelService       speedModelService;
    private final RouteRecalculatorService routeRecalculatorService;

    public CommandFactoryImpl(EngineContext engineContext) {
        this.repository               = engineContext.getRepository();
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
            throw new IllegalArgumentException(String.format(INVALID_COMMAND, commandTypeAsString));
        }

        return switch (type) {
            // ——— CRUD / creation ———
            case CREATEPACKAGE    -> new CreatePackageCommand(repository);
            case CREATEROUTE      -> new CreateRouteCommand(routeCreationService);

            // ——— Queries & listings ———
            case FINDROUTE        -> new FindRoute(repository);
            case LISTPACKAGEINFO  -> new ListPackagesCommand(repository);
            case LISTROUTEINFO    -> new ListRoutesCommand(repository);
            case LISTTRUCKINFO    -> new ListTrucksCommand(repository);

            // ——— Assignment ———
            case ASSIGNPACKAGETOROUTE -> new AssignPackageToRouteCommand(assignmentService);
            case ASSIGNTRUCKTOROUTE   -> new AssignTruckToRouteCommand(assignmentService);

            // ——— Delivery ———
            case DELIVERPACKAGE   -> new DeliverPackageCommand(deliveryService);

            // ——— Speed model swap ———
            case CHANGESPEEDMODEL -> new ChangeSpeedModelCommand(repository, speedModelService, routeRecalculatorService);

        };
    }
}
