package de.thm.asc.tiel.interpreter.ast.expr;

import de.thm.asc.tiel.interpreter.scanning.Token;

import java.util.List;

public class CallExpr extends Expr {

    public final Expr callee;
    public final Token paren;
    public final List<Expr> arguments;

    public CallExpr(Expr callee, Token paren, List<Expr> arguments) {
        this.callee = callee;
        this.paren = paren;
        this.arguments = arguments;
    }

    @Override
    public <R> R accept(ExprVisitor<R> visitor) {
        return visitor.visitCallExpr(this);
    }
}
