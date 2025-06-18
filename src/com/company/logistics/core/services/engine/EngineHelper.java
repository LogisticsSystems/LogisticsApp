package com.company.logistics.core.services.engine;

import com.company.logistics.commands.contracts.Command;
import com.company.logistics.core.contracts.CommandFactory;
import com.company.logistics.core.contracts.LogisticsRepository;

import java.util.ArrayList;
import java.util.List;

public class EngineHelper {
    private final CommandFactory commandFactory;
    private final LogisticsRepository repository;

    public EngineHelper(CommandFactory commandFactory, LogisticsRepository repository) {
        this.commandFactory = commandFactory;
        this.repository = repository;
    }

    public String processCommand(String inputLine) {
        String commandName = extractCommandName(inputLine);
        Command command = commandFactory.createCommandFromCommandName(commandName, repository);
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
