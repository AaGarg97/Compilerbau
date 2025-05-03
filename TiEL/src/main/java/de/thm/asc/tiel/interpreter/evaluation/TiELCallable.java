package de.thm.asc.tiel.interpreter.evaluation;

import java.util.List;

/**
 * Represents a callable entity in the TiEL programming language.
 * Classes implementing this interface can be invoked as functions.
 */
interface TiELCallable {

    /**
     * Returns the number of parameters required by the callable entity.
     *
     * @return The number of parameters (arity) expected by the callable.
     */
    int arity();

    /**
     * Executes the callable entity with the given evaluator and arguments.
     *
     * @param evaluator The evaluator responsible for executing the function.
     * @param arguments The list of arguments provided to the callable entity.
     * @return The result of the function execution.
     */
    Object call(Evaluator evaluator, List<Object> arguments);
}
