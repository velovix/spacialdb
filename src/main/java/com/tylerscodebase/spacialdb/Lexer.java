package com.tylerscodebase.spacialdb;

import java.util.ArrayList;

/**
 * Lexes spacial database query statements.
 */
public class Lexer {

    /**
     * The current state of the lexer, meaning what it is in the middle of
     * parsing.
     */
    private enum State {
        NONE, KEYWORD, STRING, TUPLES, TUPLE
    }

    private Scanner scanner;
    private State state;
    private ArrayList<Token> tokens;
    private int currToken;
    private boolean seemsFinished;

    // Data used when putting together a token. These may be left unfinished
    // after parseTokens if there is an uncomplete token before end of scanner
    // input but you can be rest assured this is not the case if
    // isSeeminglyFinished returns true.
    private String strData;
    private ArrayList<String> coordData;

    /**
     * Creates a new lexer that will use the given scanner for input.
     * @param scanner scanner to receive data from
     */
    public Lexer(Scanner scanner) {
        this.scanner = scanner;
        state = State.NONE;
        tokens = new ArrayList<>();
        currToken = 0;
        seemsFinished = false;

        strData = "";
        coordData = new ArrayList<String>();
    }

    /**
     * Parses tokens using the data from the lexer's scanner. If the scanner
     * runs out of input before the end of a token, this method will exit with
     * an incomplete token. Calling this method again with more input will
     * resume parsing that token.
     * @throws ParseException if input does not follow the basic pattern of a
     *                        known token. The lexer only does so much error
     *                        checking in this respect and tokens from the
     *                        lexer are not guaranteed to make an actual valid
     *                        statement.
     */
    public void parseTokens() throws ParseException {
        Char c;

        while ((c = scanner.next()) != null) {
            switch (state) {

            case NONE:
                // We're not lexing anything yet
                if (c.getChar() == '\"') {
                    // Looks like the start of a string
                    state = State.STRING;
                    strData = "";
                } else if (c.getChar() == '(') {
                    // Looks like the start of a set of coordinates
                    state = State.TUPLES;
                    coordData = new ArrayList<String>();
                } else if (Character.isLetter(c.getChar())) {
                    // Looks like the start of an action
                    state = State.KEYWORD;
                    strData = "" + c.getChar();
                } else if (Character.isSpaceChar(c.getChar()) || c.getChar() == '\0') {
                    // Space characters should be ignored
                } else {
                    System.out.println("Stuck at '" + c.getChar() + "'");
                    throw new ParseException(c, "unexpected character");
                }
                break;

            case KEYWORD:
                // We're lexing a keyword
                if (Character.isLetter(c.getChar()) || Character.isDigit(c.getChar())) {
                    // Character is part of the action name
                    strData += c.getChar();
                } else if (Character.isSpaceChar(c.getChar()) || c.getChar() == '\0') {
                    // A space denotes the end of the action token so we spit an action token out
                    tokens.add(new Token(Token.TokenType.KEYWORD, strData));
                    state = State.NONE; // Wait for next token
                } else {
                    throw new ParseException(c, "unexpected character");
                }
                break;

            case STRING:
                // We're lexing a string
                if (c.getChar() == '\n') {
                    // As a rule for typeability, strings should not have newlines
                    throw new ParseException(c, "newlines are not allowed in names");
                } else if (c.getChar() == '\"') {
                    // A quote denotes the end of the string token
                    tokens.add(new Token(Token.TokenType.STRING, strData));
                    state = State.NONE; // Wait for next token
                } else {
                    // Character is part of the string
                    strData += c.getChar();
                }
                break;

            case TUPLES:
                // We're lexing a set of tuples
                if (c.getChar() == ')') {
                    // Closing paran denotes the end of the coordinate list
                    tokens.add(new Token(Token.TokenType.TUPLES, coordData));
                    state = State.NONE;
                } else if (c.getChar() == '(') {
                    // Opening paran denotes the beginning of a new coordinate
                    state = State.TUPLE; // We are inside a single coordinate
                    strData = "";
                } else if (Character.isSpaceChar(c.getChar()) || c.getChar() == '\0') {
                    // Space characters should be ignored
                } else {
                    throw new ParseException(c, "unexpected character");
                }
                break;

            case TUPLE:
                // We're lexing a single tuple
                if (c.getChar() == ')') {
                    // Closing paran denotes the end of the single coordinate
                    coordData.add(strData);
                    state = State.TUPLES;
                } else if (Character.isDigit(c.getChar()) || c.getChar() == ',' || c.getChar() == '-' ||
                        Character.isSpaceChar(c.getChar()) || c.getChar() == '\0') {
                    strData += c.getChar();
                } else {
                    throw new ParseException(c, "unexpected character");
                }

            }
        }

        // If the lexer isn't in an unfinished state parsing a token, the
        // statement seems finished as far as the lexer's concerned
        seemsFinished = (state == State.NONE);
    }

    /**
     * Returns true if the statement seems finished from the lexer's
     * perspective. This does not necessarily mean that the tokens form an
     * actual finished statement.
     * @return true if the statement seems finished
     */
    public boolean isSeeminglyFinished() {
        return seemsFinished;
    }

    /**
     * Returns the next token in a list of parsed tokens. Incomplete tokens are
     * not returned.
     * @return parsed tokens
     */
    public Token nextToken() {
        try {
            return tokens.get(currToken++);
        } catch (IndexOutOfBoundsException e) {
            currToken--;
            return null;
        }
    }

}
