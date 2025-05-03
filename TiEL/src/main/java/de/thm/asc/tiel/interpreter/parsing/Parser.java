package de.thm.asc.tiel.interpreter.parsing;

import de.thm.asc.tiel.interpreter.ast.expr.*;
import de.thm.asc.tiel.interpreter.ast.stmt.*;
import de.thm.asc.tiel.interpreter.error.ParsingError;
import de.thm.asc.tiel.interpreter.scanning.Token;
import de.thm.asc.tiel.interpreter.scanning.TokenType;

import static de.thm.asc.tiel.interpreter.scanning.TokenType.*;

import java.util.ArrayList;
import java.util.List;

/**
 * The Parser class is responsible for parsing a list of tokens into an Abstract Syntax Tree (AST).
 * It processes statements and expressions according to the language's grammar rules.
 */
public class Parser {

    private final List<Token> tokens;
    private int current = 0;

    /**
     * Constructs a new Parser with the provided list of tokens.
     *
     * @param tokens The list of tokens to parse.
     */
    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    /**
     * Parses the token list into a list of statements.
     *
     * @return A list of parsed statements.
     */
    public List<Stmt> parse() {
        var statements = new ArrayList<Stmt>();

        while (!isAtEnd()) {
            statements.add(global());
        }

        return statements;
    }

    /**
     * Parses a global declaration statement.
     * NOTE: This method differs from `declaration` in that function declarations are allowed.
     *
     * @return The parsed statement.
     */
    private Stmt global() {
        if (match(FUN)) return function();
        if (match(VAR)) return varDeclaration();

        return statement();
    }

    /**
     * Parses a declaration statement.
     *
     * @return The parsed statement.
     */
    private Stmt declaration() {
        if (match(VAR)) return varDeclaration();

        return statement();
    }


    /**
     * Parses an expression.
     *
     * @return The parsed expression.
     */
    private Expr expression() {
        return assignment();
    }

    /**
     * Parses a statement.
     *
     * @return The parsed statement.
     */
    private Stmt statement() {
        if (match(IF)) return ifStatement();
        if (match(RETURN)) return returnStatement();
        if (match(WHILE)) return whileStatement();
        if (match(LEFT_BRACE)) return new BlockStmt(block());

        return expressionStatement();
    }

    /**
     * Parses an if statement.
     *
     * @return The parsed if statement.
     */
    private Stmt ifStatement() {
        var condition = expression();
        consume(THEN, "Expect 'then' after if condition."); // [parens]

        var thenBranch = statement();

        Stmt elseBranch = null;
        if (match(ELSE)) {
            elseBranch = statement();
        }

        return new IfStmt(condition, thenBranch, elseBranch);
    }

    /**
     * Parses a return statement.
     *
     * @return The parsed return statement.
     */
    private Stmt returnStatement() {
        var keyword = previous();
        Expr value = null;

        if (!check(SEMICOLON)) {
            value = expression();
        }

        consume(SEMICOLON, "Expect ';' after return value.");

        return new ReturnStmt(keyword, value);
    }

    /**
     * Parses a variable declaration statement.
     *
     * @return The parsed variable declaration.
     */
    private Stmt varDeclaration() {
        var name = consume(IDENTIFIER, "Expect variable name.");

        consume(EQUAL, "Expect '=' after variable name.");

        var initializer = expression();

        consume(SEMICOLON, "Expect ';' after variable declaration.");

        return new VarDeclStmt(name, initializer);
    }

    /**
     * Parses a while statement.
     *
     * @return The parsed while statement.
     */
    private Stmt whileStatement() {
        var condition = expression();
        consume(DO, "Expect 'do' after condition.");
        var body = statement();

        return new WhileStmt(condition, body);
    }

    /**
     * Parses an expression statement.
     *
     * @return The parsed expression statement.
     */
    private Stmt expressionStatement() {
        var expr = expression();
        consume(SEMICOLON, "Expect ';' after expression.");
        return new ExpressionStmt(expr);
    }

    /**
     * Parses a function declaration.
     *
     * @return The parsed function declaration.
     */
    private Stmt function() {
        var name = consume(IDENTIFIER, "Expect function name.");
        consume(LEFT_PAREN, "Expect '(' after function name.");
        var parameters = new ArrayList<Token>();
        if (!check(RIGHT_PAREN)) {

            do {
                parameters.add(consume(IDENTIFIER, "Expect parameter name."));
            } while (match(COMMA));
        }
        consume(RIGHT_PAREN, "Expect ')' after parameters.");

        consume(LEFT_BRACE, "Expect '{' before function body.");

        var body = block();

        return new FunctionDeclStmt(name, parameters, body);
    }

    /**
     * Parses a block statement.
     *
     * @return The list of parsed statements inside the block.
     */
    private List<Stmt> block() {
        var statements = new ArrayList<Stmt>();

        while (!check(RIGHT_BRACE) && !isAtEnd()) {
            statements.add(declaration());
        }

        consume(RIGHT_BRACE, "Expect '}' after block.");
        return statements;
    }

    /**
     * Parses an assignment statement.
     *
     * @return The parsed assignment statement.
     */
    private Expr assignment() {
        var expr = or();

        if (match(EQUAL)) {
            var equals = previous();
            var value = assignment();

            if (expr instanceof VariableExpr v) {
                Token name = v.name;
                return new AssignExpr(name, value);
            }

            throw new ParsingError("Invalid assignment target.", equals.line());
        }

        return expr;
    }

    /**
     * Parses logical OR expressions.
     *
     * @return The parsed OR expression.
     */
    private Expr or() {
        var expr = and();

        while (match(OR)) {
            var operator = previous();
            var right = and();
            expr = new LogicalExpr(expr, operator, right);
        }

        return expr;
    }

    /**
     * Parses logical AND expressions.
     *
     * @return The parsed AND expression.
     */
    private Expr and() {
        var expr = equality();

        while (match(AND)) {
            var operator = previous();
            var right = equality();
            expr = new LogicalExpr(expr, operator, right);
        }

        return expr;
    }

    /**
     * Parses equality expressions (e.g., ==).
     *
     * @return The parsed equality expression.
     */
    private Expr equality() {
        var expr = comparison();

        while (match(EQUAL_EQUAL)) {
            var operator = previous();
            var right = comparison();
            expr = new BinaryExpr(expr, operator, right);
        }

        return expr;
    }

    /**
     * Parses comparison expressions (e.g., <).
     *
     * @return The parsed comparison expression.
     */
    private Expr comparison() {
        var expr = term();

        while (match(LESS)) {
            var operator = previous();
            var right = term();
            expr = new BinaryExpr(expr, operator, right);
        }

        return expr;
    }

    /**
     * Parses term expressions (e.g., +, -).
     *
     * @return The parsed term expression.
     */
    private Expr term() {
        var expr = factor();

        while (match(MINUS, PLUS)) {
            var operator = previous();
            var right = factor();
            expr = new BinaryExpr(expr, operator, right);
        }

        return expr;
    }

    /**
     * Parses factor expressions (e.g., *, /).
     *
     * @return The parsed factor expression.
     */
    private Expr factor() {
        var expr = unary();

        while (match(SLASH, STAR)) {
            var operator = previous();
            var right = unary();
            expr = new BinaryExpr(expr, operator, right);
        }

        return expr;
    }

    /**
     * Parses unary expressions (e.g., not, -).
     *
     * @return The parsed unary expression.
     */
    private Expr unary() {
        if (match(NOT, MINUS)) {
            var operator = previous();
            var right = unary();
            return new UnaryExpr(operator, right);
        }

        return call();
    }

    /**
     * Completes parsing of a function call by parsing its arguments.
     *
     * @param callee The expression representing the function being called.
     * @return The completed function call expression.
     */
    private Expr finishCall(Expr callee) {
        var arguments = new ArrayList<Expr>();
        if (!check(RIGHT_PAREN)) {
            do {
                arguments.add(expression());
            } while (match(COMMA));
        }

        Token paren = consume(RIGHT_PAREN, "Expect ')' after arguments.");

        return new CallExpr(callee, paren, arguments);
    }

    /**
     * Parses function call expressions.
     *
     * @return The parsed call expression.
     */
    private Expr call() {
        var expr = primary();

        while (true) {
            if (match(LEFT_PAREN)) {
                expr = finishCall(expr);
            } else {
                break;
            }
        }

        return expr;
    }

    /**
     * Parses primary expressions (literals, identifiers, and grouped expressions).
     *
     * @return The parsed primary expression.
     */
    private Expr primary() {
        if (match(FALSE)) return new LiteralExpr(false);
        if (match(TRUE)) return new LiteralExpr(true);
        if (match(NIL)) return new LiteralExpr(null);

        if (match(NUMBER, STRING)) {
            return new LiteralExpr(previous().value());
        }

        if (match(IDENTIFIER)) {
            return new VariableExpr(previous());
        }

        if (match(LEFT_PAREN)) {
            var expr = expression();
            consume(RIGHT_PAREN, "Expect ')' after expression.");
            return expr;
        }

        throw new ParsingError("Expect expression.", peek().line());
    }

    /**
     * Consumes a token of the given type.
     *
     * @param type    The type to consume.
     * @param message The error message to display when the current type does not match the expected type.
     * @return Null (unreachable)
     */
    private Token consume(TokenType type, String message) {
        if (check(type)) return advance();

        throw new ParsingError(message, peek().line());
    }

    /**
     * Checks if the next token is of the given type. A fixed lookahead of one token means we are doing LL(1) parsing.
     *
     * @param type The type to check for.
     * @return True if types match, false otherwise.
     */
    private boolean check(TokenType type) {
        if (isAtEnd()) return false;
        return peek().type() == type;
    }

    /**
     * Advances to the next token if not at the end.
     *
     * @return The previous token.
     */
    private Token advance() {
        if (!isAtEnd()) current++;
        return previous();
    }

    /**
     * Checks if the parser has reached the end of the token list.
     *
     * @return True if at the end, false otherwise.
     */
    private boolean isAtEnd() {
        return peek().type() == EOF;
    }

    /**
     * Returns the current token without consuming it.
     *
     * @return The current token.
     */
    private Token peek() {
        return tokens.get(current);
    }

    /**
     * Returns the previous token.
     *
     * @return The previous token.
     */
    private Token previous() {
        return tokens.get(current - 1);
    }

    /**
     * Checks if the current token matches any of the given types and advances if true.
     *
     * @param types The token types to match against.
     * @return {@code true} if a match is found, otherwise {@code false}.
     */
    private boolean match(TokenType... types) {
        for (var type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }

        return false;
    }
}
