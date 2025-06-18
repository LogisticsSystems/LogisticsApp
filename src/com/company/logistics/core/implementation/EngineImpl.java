package com.company.logistics.core.implementation;

import com.company.logistics.core.contracts.Engine;
import com.company.logistics.core.services.engine.EngineHelper;

import java.util.Scanner;

public class EngineImpl implements Engine {
    private static final String TERMINATION_COMMAND = "Exit";
    private static final String EMPTY_COMMAND_ERROR = "Command cannot be empty.";

    private final EngineHelper engineHelper;

    public EngineImpl(EngineHelper engineHelper) {
        this.engineHelper = engineHelper;
    }

    @Override
    public void start() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            try {
                String inputLine = scanner.nextLine();

                if (inputLine.isBlank()) {
                    System.out.println(EMPTY_COMMAND_ERROR);
                    continue;
                }

                if (inputLine.equalsIgnoreCase(TERMINATION_COMMAND)) {
                    //TODO Save command logic
                    break;
                }

                String executionResult = engineHelper.processCommand(inputLine);
                System.out.println(executionResult);

            } catch (Exception ex) {
                System.out.println(ex.getMessage() != null ? ex.getMessage() : ex.toString());
            }
        }
    }
}
