package com.company.logistics.commands.queries;

import com.company.logistics.commands.contracts.Command;
import com.company.logistics.exceptions.InvalidUserInputException;
import com.company.logistics.models.contracts.User;
import com.company.logistics.repositories.contracts.UserRepository;
import com.company.logistics.utils.ErrorMessages;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class HelpCommand implements Command {
    private static final String GREEN = "\u001B[32m";
    private static final String COLOR_RESET = "\u001B[0m";
    private static final String BOLD = "\u001B[1m";
    private static final String ITALICS = "\033[3m";
    private static final String STYLE_RESET = "\033[0m";

    private static final String INFO_FOR_ROLE_HEADER = "%n===== Commands Available when %s user is logged in =====%n";

    private final User loggedInUser;

    public HelpCommand(UserRepository userRepository) {
        User loggedIn;
        try {
            loggedIn = userRepository.getLoggedInUser();
        } catch (InvalidUserInputException e) {
            loggedIn = null;
        }

        this.loggedInUser = loggedIn;
    }

    @Override
    public String execute(List<String> parameters) {
        printFromFile();

        return "";
    }

    private void printFromFile() {
        if (loggedInUser != null) {
            switch (loggedInUser.getRole()) {
                case EMPLOYEE -> printInfoFromFile("EmployeeCommandsInfo.txt", "Employee");
                case MANAGER -> printInfoFromFile("ManagerCommandsInfo.txt", "Manager");
                case DATA_ANALYST -> printInfoFromFile("DataAnalystCommandsInfo.txt", "Data Analyst");
            }
        } else {
            printInfoFromFile("NoLoginCommandsInfo.txt", "no");
            printInfoFromFile("EmployeeCommandsInfo.txt", "Employee");
            printInfoFromFile("ManagerCommandsInfo.txt", "Manager");
            printInfoFromFile("DataAnalystCommandsInfo.txt", "Data Analyst");
        }
    }

    private void printInfoFromFile(String filename, String userType) {
        try (
                FileReader reader = new FileReader(filename);
                BufferedReader bufferedReader = new BufferedReader(reader);
        ) {
            int lineNumber = 0;
            System.out.printf(BOLD + INFO_FOR_ROLE_HEADER + STYLE_RESET, userType);

            while (bufferedReader.ready()) {
                String line = bufferedReader.readLine();

                if (lineNumber % 2 == 1) {
                    System.out.println(GREEN + ITALICS + line + STYLE_RESET + COLOR_RESET);
                }
                else {
                    System.out.println(line);
                }

                lineNumber++;
            }

        } catch (IOException e) {
            throw new InvalidUserInputException(ErrorMessages.INFORMATION_CURRENTLY_UNAVAILABLE);
        }
    }
}
