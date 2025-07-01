package com.company.logistics.services.engine;

import com.company.logistics.commands.contracts.Command;
import com.company.logistics.core.contracts.CommandFactory;

import java.util.ArrayList;
import java.util.List;

public class CommandProcessor {
    private final CommandFactory commandFactory;

    public CommandProcessor(CommandFactory commandFactory) {
        this.commandFactory = commandFactory;
    }

    public String processCommand(String inputLine) {
        String commandName = extractCommandName(inputLine);
        Command command = commandFactory.createCommandFromCommandName(commandName);
        List<String> parameters = extractCommandParameters(inputLine);
        return command.execute(parameters);
    }

    public String extractCommandName(String inputLine) {
        return inputLine.split(" ")[0];
    }

    public List<String> extractCommandParameters(String inputLine) {
        String[] parts = inputLine.split(" ");
        List<String> parameters = new ArrayList<>();
        for (int i = 1; i < parts.length; i++) {
            parameters.add(parts[i]);
        }
        return parameters;
    }
}
