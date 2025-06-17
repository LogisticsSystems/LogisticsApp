package com.company.logistics.core.contracts;

import com.company.logistics.commands.contracts.Command;

public interface CommandFactory {

    Command createCommandFromCommandName(String commandTypeAsString, LogisticsRepository logisticsRepository);

}
