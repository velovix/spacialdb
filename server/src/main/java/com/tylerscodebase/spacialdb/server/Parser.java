package com.tylerscodebase.spatialdb.server;

import java.util.ArrayList;

/**
 * Implements a parser for spatial database queries.
 */
@SuppressWarnings("unchecked")
public class Parser {

    /**
     * The current state of the parser. That is, what the parser expects the
     * next token to be.
     */
    public enum State {
        KEYWORD, STATEMENT_SPECIFIC
    }

    private Lexer lexer;
    private State state;
    private Statement statement;
    private boolean isFinished;

    /**
     * Creates a new parser that will get tokens from the given lexer.
     * @param lexer token source
     */
    public Parser(Lexer lexer) {
        this.lexer = lexer;
        state = State.KEYWORD;
        isFinished = false;
    }

    /**
     * Parses tokens from the lexer into a valid statement. If the lexer
     * doesn't contain enough tokens to complete a statement, the method will
     * return in an unfinished state. This method can be run again to finish
     * the statement if more tokens have been added to the lexer.
     * @throws ParseException if a valid statement can't be formed
     */
    public void parse() throws ParseException {
        Token token;

        // Loop through every token
        while ((token = lexer.nextToken()) != null) {
            switch (state) {
            case KEYWORD:
                // The parser expects a valid action specifier token

                if (token.getType() != Token.TokenType.KEYWORD) {
                    // No action is specified as the first token
                    throw new ParseException("expected valid action as first token");
                }

                // Get the string data of the action specifier
                String data = (String) token.getData();
                if (data.toUpperCase().equals("SET")) {
                    // We have a set statement
                    statement = new SetStatement();
                    state = State.STATEMENT_SPECIFIC;
                } else if (data.toUpperCase().equals("CONFIG")) {
                    // We have a config statement
                    statement = new ConfigStatement();
                    state = State.STATEMENT_SPECIFIC;
                } else if (data.toUpperCase().equals("PRINT")) {
                    // We have a print statement
                    statement = new PrintStatement();
                    state = State.STATEMENT_SPECIFIC;
                } else if (data.toUpperCase().equals("COLLIDES")) {
                    // We have a collides statement
                    statement = new CollidesStatement();
                    state = State.STATEMENT_SPECIFIC;
                } else {
                    throw new ParseException("unknown action type " + data.toUpperCase());
                }
                break;
            case STATEMENT_SPECIFIC:
                // The parser expects whatever token the statement type expects

                // Set to true if we've found where the token should go in the statement
                boolean fitTokenInStatement = false;

                // Because some portions of some statements are optional, the
                // amount of tokens we get may not equal the amount of
                // arguments a statement expects. So, we have to keep trying to
                // apply the same token to the statement until we find a use
                // for it or an error occurs.
                while (!fitTokenInStatement) {
                    if (token.getType() != statement.nextExpectedTokenType()) {
                        // The token is not what the statment expects
                        if (statement.isNextExpectedTokenOptional()) {
                            // If the expected token is optional, we can set the
                            // value to default and move on, otherwise throw an
                            // exception
                            statement.giveDefault();
                        } else {
                            throw new ParseException("unexpected token type");
                        }
                    } else {
                        // Tokens may contain any type of value, so we check it and
                        // feed the data into the statement. If the statement doesn't
                        // expect that type, it will throw a ParseException.
                        if (token.getData() instanceof String) {
                            if (token.getType() == Token.TokenType.STRING) {
                                statement.giveString((String) token.getData());
                            } else if (token.getType() == Token.TokenType.KEYWORD) {
                                statement.giveKeyword((String) token.getData());
                            }
                        } else if (token.getData() instanceof ArrayList) {
                            ArrayList<String> tuplesData = (ArrayList<String>) token.getData();
                            ArrayList<Tuple> tuples = new ArrayList<>(tuplesData.size());

                            for (int i=0; i<tuplesData.size(); i++) {
                                String tupleData = tuplesData.get(i);
                               tuples.add(parseTuple(tupleData));
                            }

                            statement.giveTuples(tuples);
                        } else {
                            throw new RuntimeException("unexpected token type");
                        }

                        // We found where the token should go, so we can move on
                        fitTokenInStatement = true;
                    }
                }
            }
        }

        // If the statement doesn't expect more tokens, then the parse is finished
        if (statement != null && statement.nextExpectedTokenType() == null) {
            isFinished = true;
        } else {
            isFinished = false;
        }
    }

    private Tuple parseTuple(String data) throws ParseException {
        ArrayList<Double> nums = new ArrayList<>(2);
        String num = "";

        // Add a comma to the end to ease parsing
        data += ',';

        // Loop through every character
        for (int i=0; i<data.length(); i++) {
            if (Character.isDigit(data.charAt(i)) || data.charAt(i) == '-' || data.charAt(i) == '.') {
                // The character appears to be part of a valid number
                num += data.charAt(i);
            } else if (data.charAt(i) == ',') {
                // A comma separates numbers
                try {
                    nums.add(Double.parseDouble(num));
                } catch (NumberFormatException e) {
                    throw new ParseException("invalid tuple value");
                }
                num = "";
            } else if (Character.isSpaceChar(data.charAt(i))) {
               // Space characters should be ignored
            } else {
                throw new ParseException("unexpected non-number character");
            }
        }

        return new Tuple(nums);
    }

    /**
     * Returns a constructed statement if the statement has been finished, or
     * else null.
     * @return constructed statement
     */
    public Statement getStatement() {
        if (statement != null && statement.nextExpectedTokenType() == null) {
            return statement;
        } else {
            return null;
        }
    }

    /**
     * Attempts to finish construction of the statement even if the statement
     * could accept additional tokens. In some cases, this will lead to a
     * completely valid statement. If not, a parse exception is thrown. This
     * method should be called if the interface is given some kind of
     * indication that the user does not intend to give any more input.
     */
    public void truncateStatement() throws ParseException {
        System.out.println("looks to me like a statement is being truncated");
        if (lexer.nextToken() != null) {
            // This method should only be used if all the lexer tokens have
            // been exausted
            throw new RuntimeException("attempt to truncate a statement before all lexer tokens have been used");
        }

        while (statement.nextExpectedTokenType() != null) {
            if (!statement.isNextExpectedTokenOptional()) {
                throw new ParseException("unexpected end of input");
            }
            statement.giveDefault();
        }

        System.out.println("Result: " + statement.nextExpectedTokenType());
    }

    /**
     * Returns true if the parser could construct a valid, complete statement.
     * @return true if the parser has completed a statement
     */
    public boolean isFinished() {
        return isFinished;
    }

}
