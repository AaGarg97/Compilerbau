package de.thm.asc.tiel.interpreter.cli;

import java.io.PrintStream;

public class CommandLineParser {

    public String input;
    public boolean showTokens = false;
    public boolean showAst = false;

    private CommandLineParser() {
    }

    private static void showUsage(PrintStream out) {
        out.println("Usage: tiel [OPTIONS] INPUT");
        out.println();
        out.println("Options:");
        out.println("  --tokens     Scans the source and prints the tokens.");
        out.println("  --ast        Scans and parses the source and prints the AST.");
        out.println("  --help       Shows this dialog.");
    }

    private static void usageError(String format, Object... args) {
        System.err.printf("Usage error: " + format, args);
        System.err.println();
        showUsage(System.err);
        System.exit(1);
    }

    public static CommandLineParser parse(String[] args) {
        var parser = new CommandLineParser();

        for (var a : args) {
            switch (a) {
                case "--tokens" -> parser.showTokens = true;
                case "--ast" -> parser.showAst = true;
                case "--help" -> showUsage(System.out);
                default -> {
                    if (!a.startsWith("--")) {
                        if (parser.input == null) {
                            parser.input = a;
                        } else {
                            usageError("Too many positional arguments!");
                        }
                    } else {
                        usageError("Unknown option '%s'!", a);
                    }
                }
            }
        }

        if (parser.input == null) {
            usageError("No input file!");
        }

        return parser;
    }
}
