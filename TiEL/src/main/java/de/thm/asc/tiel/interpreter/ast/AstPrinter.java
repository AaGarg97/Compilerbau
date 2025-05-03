package de.thm.asc.tiel.interpreter.ast;

import de.thm.asc.tiel.interpreter.ast.expr.*;
import de.thm.asc.tiel.interpreter.ast.stmt.*;
import de.thm.asc.tiel.interpreter.scanning.Token;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The AstPrinter class is responsible for converting an abstract syntax tree to a printable string representation.
 */
public class AstPrinter implements ExprVisitor<String>, StmtVisitor<String> {

    /**
     * Prints the string representation of an expression.
     *
     * @param expr The expression to print.
     * @return The formatted string representation.
     */
    public String print(Expr expr) {
        return expr.accept(this);
    }

    /**
     * Prints the string representation of a statement.
     *
     * @param stmt The statement to print.
     * @return The formatted string representation.
     */
    public String print(Stmt stmt) {
        return stmt.accept(this);
    }

    /**
     * Prints a list of statements, each on a new line.
     *
     * @param stmts The list of statements to print.
     * @return The formatted string representation.
     */
    public String print(List<Stmt> stmts) {
        var sb = new StringBuilder();
        for (var s : stmts) {
            sb.append(print(s)).append("\r\n");
        }
        return sb.toString();
    }

    @Override
    public String visitAssignExpr(AssignExpr expr) {
        return sExpr(AssignExpr.class.getSimpleName(), expr.name.lexeme(), expr.value);
    }

    @Override
    public String visitBinaryExpr(BinaryExpr expr) {
        return sExpr(BinaryExpr.class.getSimpleName(), expr.operator.lexeme(), expr.left, expr.right);
    }

    @Override
    public String visitCallExpr(CallExpr expr) {
        return sExpr(CallExpr.class.getSimpleName(), expr.callee, expr.arguments);
    }

    @Override
    public String visitLiteralExpr(LiteralExpr expr) {
        if (expr.value == null) return sExpr(LiteralExpr.class.getSimpleName(), "nil");
        return sExpr(LiteralExpr.class.getSimpleName(), expr.value.toString());
    }

    @Override
    public String visitLogicalExpr(LogicalExpr expr) {
        return sExpr(LogicalExpr.class.getSimpleName(), expr.operator.lexeme(), expr.left, expr.right);
    }

    @Override
    public String visitUnaryExpr(UnaryExpr expr) {
        return sExpr(UnaryExpr.class.getSimpleName(), expr.operator.lexeme(), expr.right);
    }

    @Override
    public String visitVariableExpr(VariableExpr expr) {
        return sExpr(VariableExpr.class.getSimpleName(), expr.name.lexeme());
    }

    @Override
    public String visitBlockStmt(BlockStmt stmt) {
        return sExpr(BlockStmt.class.getSimpleName(), stmt.statements.toArray());
    }

    @Override
    public String visitExpressionStmt(ExpressionStmt stmt) {
        return sExpr(ExpressionStmt.class.getSimpleName(), stmt.expression);
    }

    @Override
    public String visitFunctionDeclStmt(FunctionDeclStmt stmt) {
        var params = stmt.params.stream()
                .map(Token::lexeme)
                .toArray();
        var body = stmt.body.toArray();

        return sExpr(FunctionDeclStmt.class.getSimpleName(), stmt.name.lexeme(), sExpr("Params", params), sExpr("Body", body));
    }

    @Override
    public String visitIfStmt(IfStmt stmt) {
        if (stmt.elseBranch == null)
            return sExpr(IfStmt.class.getSimpleName(), stmt.condition, stmt.thenBranch);
        return sExpr(IfStmt.class.getSimpleName(), stmt.condition, stmt.thenBranch, stmt.elseBranch);
    }

    @Override
    public String visitReturnStmt(ReturnStmt stmt) {
        if (stmt.value == null) return sExpr(ReturnStmt.class.getSimpleName());
        return sExpr(ReturnStmt.class.getSimpleName(), stmt.value);
    }

    @Override
    public String visitVarDeclStmt(VarDeclStmt stmt) {
        return sExpr(VarDeclStmt.class.getSimpleName(), stmt.name.lexeme(), stmt.initializer);
    }

    @Override
    public String visitWhileStmt(WhileStmt stmt) {
        return sExpr(WhileStmt.class.getSimpleName(), stmt.condition, stmt.body);
    }

    private String sExpr(String name, Object... parts) {
        var joinedParts = Arrays.stream(parts)
                .map(this::stringify)
                .collect(Collectors.joining(" "));
        return String.format("(%s%s)", name, (joinedParts.isEmpty() ? "" : " " + joinedParts));
    }

    private String stringify(Object part) {
        return switch (part) {
            case Expr expr -> expr.accept(this);
            case Stmt stmt -> stmt.accept(this);
            case Token token -> token.lexeme();
            case List<?> list -> list.stream().map(this::stringify).collect(Collectors.joining(" "));
            case null -> "nil";
            default -> part.toString();
        };
    }
}
