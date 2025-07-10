package com.company.logistics.commands.persistence;

import com.company.logistics.commands.contracts.Command;
import com.company.logistics.exceptions.InvalidUserInputException;
import com.company.logistics.services.persistence.PersistenceService;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.List;

public class LoadCommand implements Command {
    private static final String LOAD_FROM             = "State loaded from %s";
    private static final String LOAD_FAILED           = "Load failed: ";
    private static final String NO_FILE               = "Nothing to load: no bin file at %s";
    private static final String LOAD_COMMAND_USAGE    = "Usage: LOAD [<binaryFile>]";

    private final PersistenceService persistenceService;

    public LoadCommand(PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    @Override
    public String execute(List<String> parameters) {
        if (parameters.size() > 1) {
            throw new InvalidUserInputException(LOAD_COMMAND_USAGE);
        }

        Path binPath = parameters.isEmpty() ? Path.of("appState.bin") : Path.of(parameters.get(0));

        try {
            persistenceService.load(binPath);
            return String.format(LOAD_FROM, binPath);
        } catch (NoSuchFileException e) {
            throw new InvalidUserInputException(String.format(NO_FILE, binPath));
        } catch (IOException | ClassNotFoundException e) {
            throw new InvalidUserInputException(LOAD_FAILED + e.getMessage());
        }
    }
}
