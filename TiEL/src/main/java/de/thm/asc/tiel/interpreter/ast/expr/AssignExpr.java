package de.thm.asc.tiel.interpreter.ast.expr;
public class AssignExpr extends Expr {

    public final Expr name;
    public final Expr value;

    public AssignExpr(Expr name, Expr value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public <R> R accept(ExprVisitor<R> visitor) {
        return visitor.visitAssignExpr(this);
    }
}
