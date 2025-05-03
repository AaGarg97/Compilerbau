package de.thm.asc.tiel.interpreter.error;

/**
 * Represents a scanning error in the TiEL interpreter.
 * This exception is thrown when the scanner encounters an invalid token.
 */
public class ScanningError extends RuntimeException {

    /**
     * The line number where the error occurred.
     */
    public final int line;

    /**
     * Constructs a new scanning error with the specified message and line number.
     *
     * @param message The error message describing the issue.
     * @param line    The line number where the error occurred.
     */
    public ScanningError(String message, int line) {
        super(message);
        this.line = line;
    }
}
