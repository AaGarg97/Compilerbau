package de.thm.asc.tiel.interpreter.scanning;

/**
 * The TokenType enum defines the various types of tokens
 * that can be recognized in the source code during lexical analysis.
 */
public enum TokenType {
    // Keywords
    FUN,        // Function definition keyword
    VAR,        // Variable declaration keyword
    IF,         // Conditional statement keyword
    THEN,       // Used in conditional statements
    ELSE,       // Begins an else block in conditional statements
    WHILE,      // Loop keyword
    DO,         // Loop body keyword
    RETURN,     // Return statement keyword
    AND,        // Logical AND operator
    OR,         // Logical OR operator
    NOT,        // Logical NOT operator
    TRUE,       // Boolean literal 'true'
    FALSE,      // Boolean literal 'false'
    NIL,        // Null-like value keyword

    // Symbols and Operators
    LEFT_PAREN,     // (
    RIGHT_PAREN,    // )
    LEFT_BRACE,     // {
    RIGHT_BRACE,    // }
    LEFT_BRACKET,   // [
    RIGHT_BRACKET,  // ]
    COMMA,          // ,
    MINUS,          // -
    PLUS,           // +
    STAR,           // *
    SLASH,          // /
    SEMICOLON,      // ;
    EQUAL,          // =
    EQUAL_EQUAL,    // ==
    NOT_EQUAL,      // !=
    LESS,           // <
    MORE,           // >
    LESS_THAN,
    MORE_THAN,

    // Identifiers and Literals
    IDENTIFIER,     // Variable or function name
    NUMBER,         // Numeric literal
    STRING,         // String literal

    // End of file marker
    EOF            // Signals the end of the source input
}
