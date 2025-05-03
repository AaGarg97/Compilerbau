package de.thm.asc.tiel.interpreter;

import de.thm.asc.tiel.interpreter.error.ParsingError;
import de.thm.asc.tiel.interpreter.error.RuntimeError;
import de.thm.asc.tiel.interpreter.error.ScanningError;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class Assertions {

    private Assertions() {
    }

    static void assertPrintOutput(String sourceCode, String expectedOutput, Object... args) {
        var outputStream = new ByteArrayOutputStream();
        var printStream = new PrintStream(outputStream);

        TiEL.process(sourceCode, printStream);

        var actualOutput = outputStream.toString(StandardCharsets.UTF_8);
        assertEquals(String.format(expectedOutput, args), actualOutput);
    }

    static void assertScanningError(String sourceCode, String errorMessage, Object... args) {
        try {
            TiEL.process(sourceCode, new PrintStream(OutputStream.nullOutputStream()));
        } catch (ScanningError e) {
            assertEquals(String.format(errorMessage, args), e.getMessage());
            return;
        }

        fail();
    }

    static void assertParsingError(String sourceCode, String errorMessage, Object... args) {
        try {
            TiEL.process(sourceCode, new PrintStream(OutputStream.nullOutputStream()));
        } catch (ParsingError e) {
            assertEquals(String.format(errorMessage, args), e.getMessage());
            return;
        }

        fail();
    }

    static void assertRuntimeError(String sourceCode, String errorMessage, Object... args) {
        try {
            TiEL.process(sourceCode, new PrintStream(OutputStream.nullOutputStream()));
        } catch (RuntimeError e) {
            assertEquals(String.format(errorMessage, args), e.getMessage());
            return;
        }

        fail();
    }

}
