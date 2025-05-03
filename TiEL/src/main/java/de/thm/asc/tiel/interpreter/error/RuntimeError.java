package de.thm.asc.tiel.interpreter.error;

/**
 * Represents a runtime error in the TiEL interpreter.
 * This exception is thrown when an operation encounters an invalid state,
 * such as accessing an undefined variable or performing an illegal operation.
 */
public class RuntimeError extends RuntimeException {

    /**
     * Constructs a new runtime error with the specified message.
     *
     * @param message The error message describing the issue.
     */
    public RuntimeError(String message) {
        super(message);
    }
}
