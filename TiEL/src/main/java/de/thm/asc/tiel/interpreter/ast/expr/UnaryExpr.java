package de.thm.asc.tiel.interpreter.ast.expr;

import de.thm.asc.tiel.interpreter.scanning.Token;

public class UnaryExpr extends Expr {

    public final Token operator;
    public final Expr right;

    public UnaryExpr(Token operator, Expr right) {
        this.operator = operator;
        this.right = right;
    }

    @Override
    public <R> R accept(ExprVisitor<R> visitor) {
        return visitor.visitUnaryExpr(this);
    }
}
