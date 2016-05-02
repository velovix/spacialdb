package com.tylerscodebase.spacialdb;

import java.util.ArrayList;

/**
 * Represents a PRINT statement.
 */
public class PrintStatement implements Statement {

    private Token.TokenType[] expectedTokens;
    private boolean[] isOptional;
    private int currExpectedToken;

    private String type;
    private String name;

    /**
     * Creates a new PRINT statement.
     */
    public PrintStatement() {
        expectedTokens = new Token.TokenType[]{Token.TokenType.KEYWORD, Token.TokenType.STRING};
        isOptional = new boolean[]{true, false};
        currExpectedToken = 0;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    /**
     * Returns the next expected token type of the statement.
     * @return next token type
     */ 
    public Token.TokenType nextExpectedTokenType() {
        try {
            return expectedTokens[currExpectedToken];
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }
    
    /**
     * Returns true if the next expected token type is optional. If there's no
     * data to give the optional token type, giveDefault should be called.
     * @return true if the next token type is optional
     */
    public boolean isNextExpectedTokenOptional() {
        try {
            return isOptional[currExpectedToken];
        } catch (ArrayIndexOutOfBoundsException e) {
            return true;
        }
    }

    /**
     * Gives the statement a string.
     * @param string data to give the statement
     * @throws if the statement doesn't expect a string
     */
    public void giveString(String in) throws ParseException {
        if (nextExpectedTokenType() == Token.TokenType.STRING) {
            name = in;
            currExpectedToken++;
        } else {
            throw new ParseException("unexpected string in print statement");
        }
    }

    /**
     * Gives the statement a keyword.
     * @param keyword data to give the statement
     * @throws if the statement doesn't expect a keyword
     */
    public void giveKeyword(String in) throws ParseException {
        if (nextExpectedTokenType() == Token.TokenType.KEYWORD) {
            type = in;
            currExpectedToken++;
        } else {
            throw new ParseException("unexpected string in print statement");
        }
    }

    /**
     * Gives the statement tuples.
     * @param tuples data to give the statement
     * @throws if the statement doesn't expect tuples
     */
    public void giveTuples(ArrayList<Tuple> tuples) throws ParseException {
        throw new ParseException("unexpected tuples in print statement");
    }

    /**
     * Gives the default value to the next parameter.
     * @throws a runtime exception if the next parameter isn't optional
     */
    public void giveDefault() {
        if (nextExpectedTokenType() == Token.TokenType.STRING) {
            throw new RuntimeException("attempt to give the default value to a mandatory parameter");
        } else if (nextExpectedTokenType() == Token.TokenType.TUPLES) {
            throw new RuntimeException("attempt to give the default value to a mandatory parameter");
        } else if (nextExpectedTokenType() == Token.TokenType.KEYWORD) {
            type = "shape";
            currExpectedToken++;
        } else {
            throw new RuntimeException("attempt to give the default value to a non-existant parameter");
        }
    }

}
