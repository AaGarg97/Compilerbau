package de.thm.asc.tiel.interpreter.ast.stmt;

/**
 * @param <R> return value of visit method
 */
public interface StmtVisitor<R> {

    R visitBlockStmt(BlockStmt stmt);
    R visitExpressionStmt(ExpressionStmt stmt);
    R visitFunctionDeclStmt(FunctionDeclStmt stmt);
    R visitIfStmt(IfStmt stmt);
    R visitReturnStmt(ReturnStmt stmt);
    R visitVarDeclStmt(VarDeclStmt stmt);
    R visitWhileStmt(WhileStmt stmt);
}
