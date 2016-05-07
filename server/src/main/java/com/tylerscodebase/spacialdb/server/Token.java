package com.tylerscodebase.spatialdb.server;

public class Token {

    public enum TokenType {
        KEYWORD, STRING, TUPLES
    }

    private TokenType type;
    private Object data;

    public Token(TokenType type, Object data) {
        this.type = type;
        this.data = data;
    }

    public TokenType getType() {
        return type;
    }

    public Object getData() {
        return data;
    }

}
