package de.thm.asc.tiel.interpreter.evaluation;

import de.thm.asc.tiel.interpreter.ast.expr.*;
import de.thm.asc.tiel.interpreter.ast.stmt.*;
import de.thm.asc.tiel.interpreter.ast.stmt.ReturnStmt;
import de.thm.asc.tiel.interpreter.error.RuntimeError;
import de.thm.asc.tiel.interpreter.scanning.Token;
import de.thm.asc.tiel.interpreter.scanning.TokenType;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * The Evaluator class interprets and executes TiEL language expressions and statements.
 * It maintains an execution environment and supports variable resolution, function calls,
 * and basic control flow operations.
 */
public class Evaluator implements ExprVisitor<Object>, StmtVisitor<Void> {

    /**
     * The global environment for storing global variables and functions.
     */
    private final Environment globals = new Environment();
    /**
     * The current execution environment, initially set to the global environment.
     */
    private Environment environment = globals;

    /**
     * Constructs an Evaluator and defines built-in functions.
     */
    public Evaluator(PrintStream out) {
        globals.define("print", new TiELCallable() {
            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Object call(Evaluator evaluator, List<Object> arguments) {
                out.println(stringify(arguments.getFirst()));
                return null;
            }

            @Override
            public String toString() {
                return "<native fn>";
            }
        });
    }

    /**
     * Interprets a list of statements by executing them sequentially.
     *
     * @param statements The statements to interpret.
     */
    public void interpret(List<Stmt> statements) {
        for (var s : statements) {
            execute(s);
        }
    }

    /**
     * Evaluates an expression.
     *
     * @param expr The expression to evaluate.
     * @return The evaluated value-
     */
    private Object evaluate(Expr expr) {
        return expr.accept(this);
    }

    /**
     * Executes a statement.
     *
     * @param stmt The statement to execute.
     */
    private void execute(Stmt stmt) {
        stmt.accept(this);
    }

    /**
     * Executes a block of statements in a new environment scope.
     *
     * @param statements  The block statements.
     * @param environment The new environment scope.
     */
    void executeBlock(List<Stmt> statements, Environment environment) {
        var previous = this.environment;
        try {
            this.environment = environment;

            for (var s : statements) {
                execute(s);
            }
        } finally {
            this.environment = previous;
        }
    }

    /**
     * Checks if an object is truthy according to TiEL's rules.
     *
     * @param object The object to check.
     * @return True if the object is truthy, false otherwise.
     */
    private boolean isTruthy(Object object) {
        if (object == null) return false;
        if (object instanceof Boolean b) return b;
        return true;
    }

    /**
     * Compares two objects for equality.
     *
     * @param a The first object.
     * @param b The second object.
     * @return True if the objects are equal, false otherwise.
     */
    private boolean isEqual(Object a, Object b) {
        if (a == null && b == null) return true;
        if (a == null) return false;

        return a.equals(b);
    }

    /**
     * Converts an object into its string representation.
     *
     * @param object The object to stringify.
     * @return The string representation of the object.
     */
    private String stringify(Object object) {
        if (object == null) return "nil";

        if (object instanceof Double) {
            var text = object.toString();
            if (text.endsWith(".0")) {
                text = text.substring(0, text.length() - 2);
            }
            return text;
        }

        return object.toString();
    }

    /**
     * Ensures that two operands are numbers, throwing an error if not.
     *
     * @param operator The operator token.
     * @param left     The left operand.
     * @param right    The right operand.
     */
    private void checkNumberOperands(Token operator, Object left, Object right) {
        if (left instanceof Double && right instanceof Double) return;
        throw new RuntimeError(String.format("Operands to '%s' must be numbers.", operator.lexeme()));
    }


    /**
     * Ensures that an operand is a number, throwing an error if not.
     *
     * @param operator The operator token.
     * @param operand  The operand.
     */
    private void checkNumberOperand(Token operator, Object operand) {
        if (operand instanceof Double) return;
        throw new RuntimeError(String.format("Operand to '%s' must be a number.", operator.lexeme()));
    }

    /**
     * Looks up a variable's value in the current environment.
     *
     * @param name The token representing the variable name.
     * @return The variable's value.
     */
    private Object lookUpVariable(Token name) {
        return environment.get(name.lexeme());
    }

    @Override
    public Object visitAssignExpr(AssignExpr expr) {
        var value = evaluate(expr.value);

        environment.assign(expr.name.lexeme(), value);

        return value;
    }

    @Override
    public Object visitBinaryExpr(BinaryExpr expr) {
        var left = evaluate(expr.left);
        var right = evaluate(expr.right);

        return switch (expr.operator.type()) {
            case EQUAL_EQUAL -> isEqual(left, right);
            case LESS -> {
                checkNumberOperands(expr.operator, left, right);
                yield (double) left < (double) right;
            }
            case MINUS -> {
                checkNumberOperands(expr.operator, left, right);
                yield (double) left - (double) right;
            }
            case PLUS -> {
                checkNumberOperands(expr.operator, left, right);
                yield (double) left + (double) right;
            }
            case SLASH -> {
                checkNumberOperands(expr.operator, left, right);
                yield (double) left / (double) right;
            }
            case STAR -> {
                checkNumberOperands(expr.operator, left, right);
                yield (double) left * (double) right;
            }
            default -> null; // Unreachable
        };
    }

    @Override
    public Object visitCallExpr(CallExpr expr) {
        var callee = evaluate(expr.callee);

        var arguments = new ArrayList<>();
        for (var argument : expr.arguments) {
            arguments.add(evaluate(argument));
        }

        if (!(callee instanceof TiELCallable function)) {
            throw new RuntimeError("Can only call functions.");
        }

        if (arguments.size() != function.arity()) {
            throw new RuntimeError(
                    String.format("Expected %s arguments but got %s.", function.arity(), arguments.size())
            );
        }

        return function.call(this, arguments);
    }

    @Override
    public Object visitLiteralExpr(LiteralExpr expr) {
        return expr.value;
    }

    @Override
    public Object visitLogicalExpr(LogicalExpr expr) {
        var left = evaluate(expr.left);

        if (expr.operator.type() == TokenType.OR) {
            if (isTruthy(left)) return true;
        } else {
            if (!isTruthy(left)) return false;
        }

        return isTruthy(evaluate(expr.right));
    }

    @Override
    public Object visitUnaryExpr(UnaryExpr expr) {
        var right = evaluate(expr.right);

        return switch (expr.operator.type()) {
            case NOT -> !isTruthy(right);
            case MINUS -> {
                checkNumberOperand(expr.operator, right);
                yield -(double) right;
            }
            default -> null; // Unreachable
        };
    }

    @Override
    public Object visitVariableExpr(VariableExpr expr) {
        return lookUpVariable(expr.name);
    }

    @Override
    public Void visitBlockStmt(BlockStmt stmt) {
        executeBlock(stmt.statements, new Environment(environment));
        return null;
    }

    @Override
    public Void visitExpressionStmt(ExpressionStmt stmt) {
        evaluate(stmt.expression);
        return null;
    }

    @Override
    public Void visitFunctionDeclStmt(FunctionDeclStmt stmt) {
        var function = new TiELFunction(stmt, environment);

        environment.define(stmt.name.lexeme(), function);

        return null;
    }

    @Override
    public Void visitIfStmt(IfStmt stmt) {
        if (isTruthy(evaluate(stmt.condition))) {
            execute(stmt.thenBranch);
        } else if (stmt.elseBranch != null) {
            execute(stmt.elseBranch);
        }
        return null;
    }

    @Override
    public Void visitReturnStmt(ReturnStmt stmt) {
        Object value = null;
        if (stmt.value != null) value = evaluate(stmt.value);

        throw new ReturnException(value);
    }

    @Override
    public Void visitVarDeclStmt(VarDeclStmt stmt) {
        var value = evaluate(stmt.initializer);

        environment.define(stmt.name.lexeme(), value);
        return null;
    }

    @Override
    public Void visitWhileStmt(WhileStmt stmt) {
        while (isTruthy(evaluate(stmt.condition))) {
            execute(stmt.body);
        }
        return null;
    }
}
