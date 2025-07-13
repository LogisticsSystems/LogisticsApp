package com.company.logistics.tests.services.engine;

import com.company.logistics.commands.contracts.Command;
import com.company.logistics.core.contracts.CommandFactory;
import com.company.logistics.services.engine.CommandProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CommandProcessorTest {
    private CommandFactory mockCommandFactory;
    private Command mockCommand;
    private CommandProcessor processor;

    @BeforeEach
    public void setup() {
        mockCommandFactory = mock(CommandFactory.class);
        mockCommand = mock(Command.class);
        processor = new CommandProcessor(mockCommandFactory);
    }

    @Test
    public void extractCommandName_Should_ReturnFirstWord() {
        String input = "CREATEPACKAGE Sofia Varna 5000 Ivan";
        String result = processor.extractCommandName(input);
        assertEquals("CREATEPACKAGE", result);
    }

    @Test
    public void extractCommandParameters_Should_ReturnRemainingWords() {
        String input = "CREATEPACKAGE Sofia Varna 5000 Ivan";
        List<String> params = processor.extractCommandParameters(input);
        assertEquals(List.of("Sofia", "Varna", "5000", "Ivan"), params);
    }

    @Test
    public void processCommand_Should_DelegateToFactory_AndExecuteCommand() {
        // Arrange
        String input = "LISTROUTES Active Only";
        when(mockCommandFactory.createCommandFromCommandName("LISTROUTES")).thenReturn(mockCommand);
        when(mockCommand.execute(List.of("Active", "Only"))).thenReturn("Routes listed.");

        // Act
        String result = processor.processCommand(input);

        // Assert
        assertEquals("Routes listed.", result);
        verify(mockCommandFactory).createCommandFromCommandName("LISTROUTES");
        verify(mockCommand).execute(List.of("Active", "Only"));
    }
}
