package de.thm.asc.tiel.interpreter.ast.expr;

import de.thm.asc.tiel.interpreter.scanning.Token;

public class LogicalExpr extends Expr {

    public final Expr left;
    public final Token operator;
    public final Expr right;

    public LogicalExpr(Expr left, Token operator, Expr right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    @Override
    public <R> R accept(ExprVisitor<R> visitor) {
        return visitor.visitLogicalExpr(this);
    }
}
