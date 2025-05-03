package de.thm.asc.tiel.interpreter.error;

/**
 * Utility class for handling errors in the TiEL interpreter.
 * Provides methods for reporting errors and terminating execution.
 */
public class Error {

    /**
     * Reports an error at a specific line and terminates the program.
     *
     * @param line    The line number where the error occurred.
     * @param message The error message to display.
     */
    public static void error(int line, String message) {
        System.out.printf("Error on line %s: %s%n", line, message);
        System.exit(1);
    }

    /**
     * Reports a general error and terminates the program.
     *
     * @param message The error message to display.
     */
    public static void error(String message) {
        System.out.printf("Error: %s%n", message);
        System.exit(1);
    }
}
