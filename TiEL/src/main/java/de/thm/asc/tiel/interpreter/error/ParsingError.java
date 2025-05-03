package de.thm.asc.tiel.interpreter.error;

/**
 * Represents a parsing error in the TiEL interpreter.
 * This exception is thrown when the parser encounters an invalid syntax or structure.
 */
public class ParsingError extends RuntimeException {

    /**
     * The line number where the error occurred.
     */
    public final int line;

    /**
     * Constructs a new parsing error with the specified message and line number.
     *
     * @param message The error message describing the issue.
     * @param line    The line number where the error occurred.
     */
    public ParsingError(String message, int line) {
        super(message);
        this.line = line;
    }
}

