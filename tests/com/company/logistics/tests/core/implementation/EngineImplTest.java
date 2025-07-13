package com.company.logistics.tests.core.implementation;

import com.company.logistics.services.engine.CommandProcessor;
import com.company.logistics.core.implementation.EngineImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EngineImplTest {
    private CommandProcessor mockProcessor;
    private ByteArrayOutputStream outContent;
    private PrintStream originalOut;
    private PrintStream originalErr;

    @BeforeEach
    public void setup() {
        mockProcessor = mock(CommandProcessor.class);
        outContent = new ByteArrayOutputStream();
        originalOut = System.out;
        originalErr = System.err;
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(outContent));
    }

    @Test
    public void start_Should_SaveOnExit_AndPrintConfirmation() {
        String expectedMessage = "State saved to appState.bin and appState.txt";
        when(mockProcessor.processCommand("SAVE")).thenReturn(expectedMessage);
        System.setIn(new ByteArrayInputStream("Exit\n".getBytes()));
        EngineImpl engine = new EngineImpl(mockProcessor);

        engine.start();

        assertTrue(outContent.toString().contains(expectedMessage));
    }

    @Test
    public void start_Should_HandleSaveException_OnExit() {
        when(mockProcessor.processCommand("SAVE"))
                .thenThrow(new RuntimeException("disk full"));
        System.setIn(new ByteArrayInputStream("Exit\n".getBytes()));
        EngineImpl engine = new EngineImpl(mockProcessor);

        engine.start();

        assertTrue(outContent.toString().contains("Error saving state on exit: disk full"));
    }
}
