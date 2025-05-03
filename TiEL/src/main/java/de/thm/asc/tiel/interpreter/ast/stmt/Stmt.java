package de.thm.asc.tiel.interpreter.ast.stmt;

public abstract class Stmt {
    public abstract <T> T accept(StmtVisitor<T> visitor);
}