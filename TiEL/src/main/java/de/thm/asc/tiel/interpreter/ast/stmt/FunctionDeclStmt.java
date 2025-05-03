package de.thm.asc.tiel.interpreter.ast.stmt;

import de.thm.asc.tiel.interpreter.scanning.Token;

import java.util.List;

public class FunctionDeclStmt extends Stmt {

    public final Token name;
    public final List<Token> params;
    public final List<Stmt> body;

    public FunctionDeclStmt(Token name, List<Token> params, List<Stmt> body) {
        this.name = name;
        this.params = params;
        this.body = body;
    }

    @Override
    public <T> T accept(StmtVisitor<T> visitor) {
        return visitor.visitFunctionDeclStmt(this);
    }
}
