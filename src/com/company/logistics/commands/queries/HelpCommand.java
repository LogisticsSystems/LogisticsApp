package com.company.logistics.commands.queries;

import com.company.logistics.commands.contracts.Command;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class HelpCommand implements Command {
    private static final String GREEN = "\u001B[32m";
    private static final String COLOR_RESET = "\u001B[0m";
    private static final String ITALICS = "\033[3m";
    private static final String ITALICS_RESET = "\033[0m";

    @Override
    public String execute(List<String> parameters) {

        printFromFile();

        return "";
    }

    private void printFromFile() {
        try (
            FileReader reader = new FileReader("CommandInfo.txt");
            BufferedReader bufferedReader = new BufferedReader(reader);
        ) {
            int lineNumber = 0;

            while (bufferedReader.ready()) {
                String line = bufferedReader.readLine();

                if (lineNumber % 2 == 1) {
                    System.out.println(GREEN + ITALICS + line + ITALICS_RESET + COLOR_RESET);
                }
                else {
                    System.out.println(line);
                }

                lineNumber++;
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
