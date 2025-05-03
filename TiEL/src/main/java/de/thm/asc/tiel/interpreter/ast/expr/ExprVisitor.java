package de.thm.asc.tiel.interpreter.ast.expr;

/**
 *
 * @param <R> return value of visit method
 */
public interface ExprVisitor<R> {

    R visitAssignExpr(AssignExpr expr);
    R visitBinaryExpr(BinaryExpr expr);
    R visitCallExpr(CallExpr expr);
    R visitLiteralExpr(LiteralExpr expr);
    R visitLogicalExpr(LogicalExpr expr);
    R visitUnaryExpr(UnaryExpr expr);
    R visitVariableExpr(VariableExpr expr);
}
