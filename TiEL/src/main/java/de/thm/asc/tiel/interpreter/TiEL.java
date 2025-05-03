package de.thm.asc.tiel.interpreter;

import de.thm.asc.tiel.interpreter.ast.AstPrinter;
import de.thm.asc.tiel.interpreter.cli.CommandLineParser;
import de.thm.asc.tiel.interpreter.error.Error;
import de.thm.asc.tiel.interpreter.error.ScanningError;
import de.thm.asc.tiel.interpreter.evaluation.Evaluator;
import de.thm.asc.tiel.interpreter.error.RuntimeError;
import de.thm.asc.tiel.interpreter.parsing.Parser;
import de.thm.asc.tiel.interpreter.error.ParsingError;
import de.thm.asc.tiel.interpreter.scanning.Scanner;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Main class to start the interpreter for Tiel
 */
public class TiEL {

    public static void main(String[] args) throws IOException {
        var cli = CommandLineParser.parse(args);

        var path = Path.of(cli.input);
        var source = Files.readString(path, Charset.defaultCharset());

        try {
            if (cli.showTokens) {
                printTokens(source);
            }
            if (cli.showAst) {
                printAst(source);
            }

            process(source, System.out);
        } catch (ScanningError e) {
            Error.error(e.line, e.getMessage());
        } catch (ParsingError e) {
            Error.error(e.line, e.getMessage());
        } catch (RuntimeError e) {
            Error.error(e.getMessage());
        }
    }

    private static void printTokens(String source) {
        var tokens = new Scanner(source).scanTokens();
        System.out.println("Tokens:");
        for (var t : tokens) {
            System.out.println(t);
        }
        System.out.println();
    }

    private static void printAst(String source) {
        var tokens = new Scanner(source).scanTokens();
        var statements = new Parser(tokens).parse();
        System.out.println("AST:");
        System.out.println(new AstPrinter().print(statements));
        System.out.println();
    }

    public static void process(String source, PrintStream output) {
        //<< 01-implement, interpreter-phases, Methode process()
        var tokens = new Scanner(source).scanTokens();
        var statements = new Parser(tokens).parse();
        new Evaluator(output).interpret(statements);
        //>>

    }

}
