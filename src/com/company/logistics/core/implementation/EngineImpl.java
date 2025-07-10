package com.company.logistics.core.implementation;

import com.company.logistics.core.contracts.Engine;
import com.company.logistics.exceptions.InvalidUserInputException;
import com.company.logistics.services.engine.CommandProcessor;

import java.util.Scanner;

public class EngineImpl implements Engine {
    private static final String TERMINATION_COMMAND = "Exit";
    private static final String EMPTY_COMMAND_ERROR = "Command cannot be empty.";

    private static final String BLUE = "\u001B[34m";
    private static final String RED = "\u001B[31m";
    private static final String COLOR_RESET = "\u001B[0m";
    private static final String ITALICS = "\033[3m";
    private static final String BOLD = "\u001B[1m";
    private static final String ITALICS_BOLD_RESET = "\033[0m";

    private static final String SAVE_ERROR_ON_EXIT = "Error saving state on exit: ";

    private static final String COMMAND_HEADER = "*** For a list of commands with examples, please type HELP. ***";
    private static final String ENTER_COMMAND = "  Enter command: ";


    private final CommandProcessor commandProcessor;

    public EngineImpl(CommandProcessor commandProcessor) {
        this.commandProcessor = commandProcessor;
    }

    @Override
    public void start() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            try {
                System.out.println(ITALICS +
                        BLUE +
                        COMMAND_HEADER +
                        COLOR_RESET +
                        ITALICS_BOLD_RESET);
                System.out.print(BOLD +
                        ENTER_COMMAND +
                        ITALICS_BOLD_RESET);

                String inputLine = scanner.nextLine();

                if (inputLine.isBlank()) {
                    System.out.println(EMPTY_COMMAND_ERROR);
                    continue;
                }

                if (inputLine.equalsIgnoreCase(TERMINATION_COMMAND)) {
                    try {
                        String result = commandProcessor.processCommand("SAVE");
                        System.out.println(result);
                    } catch (Exception e) {
                        System.err.println(SAVE_ERROR_ON_EXIT + e.getMessage());
                    }
                    break;
                }

                String executionResult = commandProcessor.processCommand(inputLine);
                System.out.println(executionResult);

            } catch (InvalidUserInputException ex) {
                System.out.println(RED +
                        ex.getMessage() +
                        COLOR_RESET);
            }
        }
    }
}
