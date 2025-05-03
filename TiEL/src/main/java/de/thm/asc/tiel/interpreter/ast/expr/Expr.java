package de.thm.asc.tiel.interpreter.ast.expr;

public abstract class Expr {
    public abstract <R> R accept(ExprVisitor<R> visitor);
}
