package de.thm.asc.tiel.interpreter.ast.stmt;

import de.thm.asc.tiel.interpreter.ast.expr.Expr;
import de.thm.asc.tiel.interpreter.scanning.Token;

public class ReturnStmt extends Stmt {

    public final Token keyword;
    public final Expr value;

    public ReturnStmt(Token keyword, Expr value) {
        this.keyword = keyword;
        this.value = value;
    }

    @Override
    public <T> T accept(StmtVisitor<T> visitor) {
        return visitor.visitReturnStmt(this);
    }
}
