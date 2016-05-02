package com.tylerscodebase.spacialdb;

import java.util.ArrayList;

/**
 * Represents a statement of some kind.
 */
public interface Statement {

    /**
     * Returns the next expected token type of the statement.
     * @return next expected token type
     */
    public Token.TokenType nextExpectedTokenType();

    /**
     * Returns true if the next expected token type is optional. If there's no
     * data to give the optional token type, giveDefault should be called.
     * @return true if the next token type is optional
     */
    public boolean isNextExpectedTokenOptional();

    /**
     * Gives the statement a string.
     * @param string data to give the statement
     * @throws if the statement doesn't expect a string
     */
    public void giveString(String string) throws ParseException;

    /**
     * Gives the statement a keyword.
     * @param keyword data to give the statement
     * @throws if the statement doesn't expect a keyword
     */
    public void giveKeyword(String keyword) throws ParseException;

    /**
     * Gives the statement tuples.
     * @param tuples data to give the statement
     * @throws if the statement doesn't expect coordinates
     */
    public void giveTuples(ArrayList<Tuple> tuples) throws ParseException;

    /**
     * Gives the default value to the next parameter.
     * @throws a runtime exception if the next parameter isn't optional
     */
    public void giveDefault();

}
