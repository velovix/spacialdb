package com.tylerscodebase.spacialdb;

import java.util.ArrayList;

/**
 * The scanner for spacial database queries.
 */
class Scanner {

    private ArrayList<Char> chars;
    private int curr, lineCnt;

    /**
     * Creates a new scanner.
     */
    public Scanner() {
        chars = new ArrayList<Char>(256);
        curr = 0;
        lineCnt = 0;
    }

    /**
     * Gives more input into the scanner.
     * @param in data to feed the scanner
     */
    public void add(String in) {
        int charCnt = 0;

        for (int i=0; i<in.length(); i++) {
            if (in.charAt(i) == '\n') {
                chars.add(new Char(' ', lineCnt, charCnt));
                lineCnt++;
                charCnt = 0;
            } else {
                chars.add(new Char(in.charAt(i), lineCnt, charCnt));
            }
            charCnt++;
        }
    }

    /**
     * Returns the next character of the scanner.
     * @return next character
     */
    public Char next() {
        try {
            return chars.get(curr++);
        } catch (IndexOutOfBoundsException e) {
            curr--;
            return null;
        }
    }

}
