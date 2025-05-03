package de.thm.asc.tiel.interpreter.ast.stmt;

import de.thm.asc.tiel.interpreter.ast.expr.Expr;

public class ExpressionStmt extends Stmt {

    public final Expr expression;

    public ExpressionStmt(Expr expression) {
        this.expression = expression;
    }

    @Override
    public <T> T accept(StmtVisitor<T> visitor) {
        return visitor.visitExpressionStmt(this);
    }
}
