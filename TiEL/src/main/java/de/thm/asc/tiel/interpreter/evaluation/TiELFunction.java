package de.thm.asc.tiel.interpreter.evaluation;

import de.thm.asc.tiel.interpreter.ast.stmt.FunctionDeclStmt;

import java.util.List;

/**
 * Represents a callable function in the TiEL programming language.
 * This class implements {@link TiELCallable} and wraps a function declaration
 * along with its closure environment.
 */
class TiELFunction implements TiELCallable {

    private final FunctionDeclStmt declaration;
    private final Environment closure;

    /**
     * Constructs a new TiELFunction instance.
     *
     * @param declaration The function declaration.
     * @param closure     The closure environment in which the function was declared.
     */
    TiELFunction(FunctionDeclStmt declaration, Environment closure) {
        this.declaration = declaration;
        this.closure = closure;
    }

    /**
     * Returns the number of parameters the function accepts.
     *
     * @return The arity of the function.
     */
    @Override
    public int arity() {
        return declaration.params.size();
    }

    /**
     * Calls the function with the given evaluator and arguments.
     *
     * @param evaluator The evaluator executing the function.
     * @param arguments The arguments passed to the function.
     * @return The result of function execution, or {@code null} if no return value is specified.
     */
    @Override
    public Object call(Evaluator evaluator, List<Object> arguments) {
        var environment = new Environment(closure);

        for (var i = 0; i < declaration.params.size(); i++) {
            environment.define(declaration.params.get(i).lexeme(), arguments.get(i));
        }

        try {
            evaluator.executeBlock(declaration.body, environment);
        } catch (ReturnException returnValue) {
            return returnValue.value;
        }

        return null;
    }


    /**
     * Returns a string representation of the function.
     *
     * @return A string in the format "<fn functionName>".
     */
    @Override
    public String toString() {
        return String.format("<fn %s>", declaration.name.lexeme());
    }
}
