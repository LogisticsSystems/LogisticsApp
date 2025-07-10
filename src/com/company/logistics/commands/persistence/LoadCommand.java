package com.company.logistics.commands.persistence;

import com.company.logistics.commands.contracts.Command;
import com.company.logistics.services.persistence.PersistenceService;
import com.company.logistics.utils.ValidationHelper;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class LoadCommand implements Command {
    private static final int EXPECTED_NUMBER_OF_ARGUMENTS = 0;
    private final PersistenceService persistenceService;

    public LoadCommand(PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    @Override
    public String execute(List<String> parameters) {
        ValidationHelper.validateArgumentsCount(parameters, EXPECTED_NUMBER_OF_ARGUMENTS);
        try {
            persistenceService.load(Path.of("appState.bin"));
            return "State loaded from appState.bin";
        } catch (IOException | ClassNotFoundException e) {
            return "Load failed: " + e.getMessage();
        }
    }
}
