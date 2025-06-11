package de.thm.asc.tiel.interpreter.ast.expr;

import de.thm.asc.tiel.interpreter.scanning.Token;

import java.util.List;

public class ArrayExpr extends Expr {

    public final Token leftBracket;
    public final Token rightBracket;
    public final List<Expr> arguments;

    public ArrayExpr(Token leftBracket, Token rightBracket, List<Expr> arguments) {
        this.leftBracket = leftBracket;
        this.arguments = arguments;
        this.rightBracket = rightBracket;
    }

    @Override
    public <R> R accept(ExprVisitor<R> visitor) {
        return visitor.visitArrayExpr(this);
    }
}
