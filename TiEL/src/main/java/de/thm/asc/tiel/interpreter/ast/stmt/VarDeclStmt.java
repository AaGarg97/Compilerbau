package de.thm.asc.tiel.interpreter.ast.stmt;

import de.thm.asc.tiel.interpreter.ast.expr.Expr;
import de.thm.asc.tiel.interpreter.scanning.Token;

public class VarDeclStmt extends Stmt {

    public final Token name;
    public final Expr initializer;

    public VarDeclStmt(Token name, Expr initializer) {
        this.name = name;
        this.initializer = initializer;
    }

    @Override
    public <T> T accept(StmtVisitor<T> visitor) {
        return visitor.visitVarDeclStmt(this);
    }
}
