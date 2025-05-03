package de.thm.asc.tiel.interpreter.ast.stmt;

import java.util.List;

public class BlockStmt extends Stmt {

    public final List<Stmt> statements;

    public BlockStmt(List<Stmt> statements) {
        this.statements = statements;
    }

    @Override
    public <T> T accept(StmtVisitor<T> visitor) {
        return visitor.visitBlockStmt(this);
    }
}
