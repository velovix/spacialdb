package com.tylerscodebase.spatialdb.server;

@SuppressWarnings("serial")
public class ParseException extends Exception {

    public ParseException(String message) {
        super(message);
    }

    public ParseException(Char c, String message) {
        super("at line " + c.getLine() + ", character " + c.getPos() + ": " + message + " for '" + c.getChar() + "'");
    }

}
