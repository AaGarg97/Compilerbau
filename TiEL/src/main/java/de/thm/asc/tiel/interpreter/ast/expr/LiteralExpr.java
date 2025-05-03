package de.thm.asc.tiel.interpreter.ast.expr;

public class LiteralExpr extends Expr {

    public final Object value;

    public LiteralExpr(Object value) {
        this.value = value;
    }

    @Override
    public <R> R accept(ExprVisitor<R> visitor) {
        return visitor.visitLiteralExpr(this);
    }
}
