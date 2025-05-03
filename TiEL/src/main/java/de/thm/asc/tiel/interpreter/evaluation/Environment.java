package de.thm.asc.tiel.interpreter.evaluation;

import de.thm.asc.tiel.interpreter.error.RuntimeError;
import de.thm.asc.tiel.interpreter.scanning.Token;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents an environment that stores variable bindings.
 * Environments can be nested, allowing for lexical scoping.
 */
class Environment {

    private final Environment enclosing;
    private final Map<String, Object> values = new HashMap<>();

    /**
     * Constructs a global environment with no enclosing scope.
     */
    Environment() {
        this.enclosing = null;
    }

    /**
     * Constructs a new environment with a given enclosing environment.
     *
     * @param enclosing The enclosing environment, providing outer scope access.
     */
    Environment(Environment enclosing) {
        this.enclosing = enclosing;
    }

    /**
     * Retrieves the value of a variable by name.
     *
     * @param name The name of the variable.
     * @return The value of the variable.
     * @throws RuntimeError if the variable is undefined.
     */
    Object get(String name) {
        if (values.containsKey(name)) {
            return values.get(name);
        }

        if (enclosing != null) {
            return enclosing.get(name);
        }

        throw new RuntimeError(String.format("Undefined variable '%s'.", name));
    }

    /**
     * Assigns a value to an existing variable.
     *
     * @param name  The name of the variable.
     * @param value The value to assign.
     * @throws RuntimeError if the variable is not defined in the current or any enclosing scope.
     */
    void assign(String name, Object value) {
        if (values.containsKey(name)) {
            values.put(name, value);
            return;
        }

        if (enclosing != null) {
            enclosing.assign(name, value);
            return;
        }

        throw new RuntimeError(String.format("Identifier not declared '%s'.", name));
    }


    /**
     * Defines a new variable in the current environment.
     *
     * @param name  The name of the variable.
     * @param value The value of the variable.
     */
    void define(String name, Object value) {
        if (values.containsKey(name)) {
            throw new RuntimeError(String.format("Identifier already declared '%s'.", name));
        }

        values.put(name, value);
    }

    /**
     * Returns a string representation of the environment, including enclosed scopes.
     *
     * @return A string representation of variable bindings in this environment.
     */
    @Override
    public String toString() {
        String result = values.toString();
        if (enclosing != null) {
            result += " -> " + enclosing;
        }

        return result;
    }
}
