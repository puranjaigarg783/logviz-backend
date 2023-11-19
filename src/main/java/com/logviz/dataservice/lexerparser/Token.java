package com.logViz.dataservice.lexerparser;

public class Token {
    private final TokenType type;
    private final String value;

    public Token(TokenType type, String value) {
        this.type = type;
        this.value = value;
    }

    // Getters
    public TokenType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }
}
