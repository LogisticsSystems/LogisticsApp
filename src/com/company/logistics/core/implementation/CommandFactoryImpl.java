package com.company.logistics.core.implementation;

import com.company.logistics.commands.assigning.AssignPackageToRouteCommand;
import com.company.logistics.commands.assigning.AssignTruckToRouteCommand;
import com.company.logistics.commands.authentications.LoginCommand;
import com.company.logistics.commands.authentications.LogoutCommand;
import com.company.logistics.commands.contracts.Command;
import com.company.logistics.commands.creation.CreatePackageCommand;
import com.company.logistics.commands.creation.CreateRouteCommand;
import com.company.logistics.commands.creation.CreateUserCommand;
import com.company.logistics.commands.delivery.DeliverPackageCommand;
import com.company.logistics.commands.listing.*;
import com.company.logistics.commands.persistence.LoadCommand;
import com.company.logistics.commands.persistence.SaveCommand;
import com.company.logistics.commands.queries.FindRoute;
import com.company.logistics.commands.queries.ViewPackageWithIDCommand;
import com.company.logistics.commands.removals.RemovePackageFromRouteCommand;
import com.company.logistics.commands.removals.RemoveTruckFromRouteCommand;
import com.company.logistics.commands.queries.HelpCommand;
import com.company.logistics.commands.removals.RemoveUserCommand;
import com.company.logistics.commands.speed.ChangeSpeedModelCommand;
import com.company.logistics.commands.speed.ViewSpeedModelCommand;
import com.company.logistics.core.context.EngineContext;
import com.company.logistics.core.contracts.CommandFactory;
import com.company.logistics.exceptions.InvalidUserInputException;
import com.company.logistics.repositories.contracts.PackageRepository;
import com.company.logistics.repositories.contracts.RouteRepository;
import com.company.logistics.repositories.contracts.TruckRepository;
import com.company.logistics.repositories.contracts.UserRepository;
import com.company.logistics.services.assignment.AssignmentService;
import com.company.logistics.services.delivery.PackageDeliveryService;
import com.company.logistics.services.persistence.PersistenceService;
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
    private final UserRepository          userRepository;
    private final AssignmentService       assignmentService;
    private final PackageDeliveryService  deliveryService;
    private final RouteCreationService    routeCreationService;
    private final SpeedModelService       speedModelService;
    private final RouteRecalculatorService routeRecalculatorService;
    private final PersistenceService       persistenceService;

    public CommandFactoryImpl(EngineContext engineContext) {
        this.packageRepository        = engineContext.getPackageRepository();
        this.routeRepository          = engineContext.getRouteRepository();
        this.truckRepository          = engineContext.getTruckRepository();
        this.userRepository           = engineContext.getUserRepository();
        this.assignmentService        = engineContext.getAssignmentService();
        this.deliveryService          = engineContext.getDeliveryService();
        this.routeCreationService     = engineContext.getRouteCreationService();
        this.speedModelService        = engineContext.getSpeedModelService();
        this.routeRecalculatorService = engineContext.getRouteRecalculatorService();
        this.persistenceService       = engineContext.getPersistenceService();
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
            case CREATEPACKAGE    -> new CreatePackageCommand(packageRepository, userRepository);
            case CREATEROUTE      -> new CreateRouteCommand(routeCreationService, userRepository);
            case CREATEUSER       -> new CreateUserCommand(userRepository);
            case REMOVEUSER       -> new RemoveUserCommand(userRepository);

            // ——— Queries & listings ———
            case FINDROUTE                      -> new FindRoute(routeRepository, userRepository);
            case VIEWPACKAGEWITHID              -> new ViewPackageWithIDCommand(packageRepository, userRepository);
            case LISTPACKAGEINFO                -> new ListPackagesCommand(packageRepository, userRepository);
            case LISTROUTEINFO                  -> new ListRoutesCommand(routeRepository, userRepository);
            case LISTTRUCKINFO                  -> new ListTrucksCommand(truckRepository, userRepository);
            case LISTUSERINFO                   -> new ListUserCommand(userRepository);
            case LISTROUTESWITHNOTRUCKASSIGNED  -> new ListRoutesWithNoAssignedTrucksCommand(routeRepository, userRepository);
            case LISTPACKAGESWITHSTATUS         -> new ListPackagesWithStatusCommand(packageRepository, userRepository);
            case LISTUSERWITHROLE               -> new ListUsersWithRoleCommand(userRepository);
            case HELP                           -> new HelpCommand(userRepository);

            // ——— Authentication ———
            case LOGIN      -> new LoginCommand(userRepository);
            case LOGOUT     -> new LogoutCommand(userRepository);

            // ——— Assignment ———
            case ASSIGNPACKAGETOROUTE -> new AssignPackageToRouteCommand(assignmentService, userRepository);
            case ASSIGNTRUCKTOROUTE   -> new AssignTruckToRouteCommand(assignmentService, userRepository);

            // ——— Removals ———
            case REMOVETRUCKFROMROUTE   -> new RemoveTruckFromRouteCommand(assignmentService, userRepository);
            case REMOVEPACKAGEFROMROUTE -> new RemovePackageFromRouteCommand(assignmentService, userRepository);

            // ——— Delivery ———
            case DELIVERPACKAGE   -> new DeliverPackageCommand(deliveryService, userRepository);

            // ——— Speed model swap ———
            case CHANGESPEEDMODEL -> new ChangeSpeedModelCommand(routeRepository, speedModelService, routeRecalculatorService, userRepository);
            case VIEWSPEEDMODEL   -> new ViewSpeedModelCommand(speedModelService, userRepository);

            // ——— Persistence ———
            case SAVE -> new SaveCommand(persistenceService);
            case LOAD -> new LoadCommand(persistenceService);
        };
    }
}
