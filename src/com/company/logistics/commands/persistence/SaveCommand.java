package com.company.logistics.commands.persistence;

import com.company.logistics.commands.contracts.Command;
import com.company.logistics.services.persistence.PersistenceService;
import com.company.logistics.utils.ValidationHelper;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class SaveCommand implements Command {
    private static final int EXPECTED_NUMBER_OF_ARGUMENTS = 0;
    private final PersistenceService persistenceService;

    public SaveCommand(PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    @Override
    public String execute(List<String> parameters) {
        ValidationHelper.validateArgumentsCount(parameters, EXPECTED_NUMBER_OF_ARGUMENTS);
        try {
            persistenceService.save(
                    Path.of("appState.bin"),
                    Path.of("appState.txt")
            );
            return "State saved to appState.bin and appState.txt";
        } catch (IOException e) {
            return "Save failed: " + e.getMessage();
        }
    }
}
