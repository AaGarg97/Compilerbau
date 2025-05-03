package de.thm.asc.tiel.interpreter.ast.expr;

import de.thm.asc.tiel.interpreter.scanning.Token;

public class VariableExpr extends Expr {

    public final Token name;

    public VariableExpr(Token name) {
        this.name = name;
    }

    @Override
    public <R> R accept(ExprVisitor<R> visitor) {
        return visitor.visitVariableExpr(this);
    }
}
