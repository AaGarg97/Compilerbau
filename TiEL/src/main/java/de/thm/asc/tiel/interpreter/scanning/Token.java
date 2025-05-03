package de.thm.asc.tiel.interpreter.scanning;

/**
 * A Token represents a lexical token in the source code.
 * @param type Type of the token
 * @param lexeme The actual text of the token as read in the source program.
 * @param value A value that is optional and, hence, may be null.
 * @param line Line number of the source program in which token occurs.
 */
public record Token(TokenType type, String lexeme, Object value, int line) {

    /**
     * Returns a string representation of the token for debugging purposes.
     *
     * @return A formatted string representing the token.
     */
    @Override
    public String toString() {
        return String.format("TOKEN(%s, %s, %s) on line %s", type, lexeme, value, line);
    }
}