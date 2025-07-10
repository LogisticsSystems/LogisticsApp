package com.company.logistics.commands.persistence;

import com.company.logistics.commands.contracts.Command;
import com.company.logistics.exceptions.InvalidUserInputException;
import com.company.logistics.services.persistence.PersistenceService;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class SaveCommand implements Command {
    private static final String SAVE_TO            = "State saved to %s and %s";
    private static final String SAVE_FAILED        = "Save failed: ";
    private static final String SAVE_COMMAND_USAGE = "Usage: SAVE [<binaryFile>]";

    private final PersistenceService persistenceService;

    public SaveCommand(PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    @Override
    public String execute(List<String> parameters) {
        if (parameters.size() > 1) {
            throw new InvalidUserInputException(SAVE_COMMAND_USAGE);
        }

        Path binPath = !parameters.isEmpty() ? Path.of(parameters.get(0)) : Path.of("appState.bin");
        Path txtPath = Path.of("appState.txt");

        try {
            persistenceService.save(binPath, txtPath);
            return String.format(SAVE_TO, binPath, txtPath);
        } catch (IOException e) {
            throw new InvalidUserInputException(SAVE_FAILED + e.getMessage());
        }
    }
}
