package com.tylerscodebase.spatialdb.server;

public class Char {
    char c;
    int line;
    int pos;

    public Char(char c, int line, int pos) {
        this.c = c;
        this.line = line;
        this.pos = pos;
    }

    public char getChar() {
        return c;
    }

    public int getLine() {
        return line;
    }

    public int getPos() {
        return pos;
    }
}
