package de.thm.asc.tiel.interpreter.ast.expr;

import de.thm.asc.tiel.interpreter.scanning.Token;

import java.util.List;

public class IndexExpr extends Expr {

    public final Token leftBracket;
    public final Token rightBracket;
    public final Expr  array;
    public final Expr  index;


    public IndexExpr(Expr array, Token leftBracket, Expr index, Token rightBracket) {
        this.leftBracket = leftBracket;
        this.array = array;
        this.rightBracket = rightBracket;
        this.index = index;
    }

    @Override
    public <R> R accept(ExprVisitor<R> visitor) {
        return visitor.visitIndexExpr(this);
    }
}
