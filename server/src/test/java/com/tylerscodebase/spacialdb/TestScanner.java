package com.tylerscodebase.spacialdb.server;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.framework.Assert;

public class TestScanner extends TestCase {
    
    public TestScanner(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(TestScanner.class);
    }

    public void testScanner() {
        Scanner scanner = new Scanner();

        scanner.add("hello world!");

        String out = "";
        Char c;
        while ((c = scanner.next()) != null) {
            out += c.getChar();
        }

        Assert.assertEquals("hello world!", out);
    }

    public void testScannerMultiadd() {
        Scanner scanner = new Scanner();

        scanner.add("hello world!");
        scanner.add(" This is a test!");

        String out = "";
        Char c;
        while ((c = scanner.next()) != null) {
            out += c.getChar();
        }

        Assert.assertEquals("hello world! This is a test!", out);
    }

    public void testScannerCharPos() {
        Scanner scanner = new Scanner();

        scanner.add("hi!\n");
        scanner.add("Test!");

        int[][] expectedData = {{0, 0}, {0, 1}, {0, 2}, {0, 3}, {1, 0}, {1, 1}, {1, 2}, {1, 3}, {1, 4}};

        Char c;
        int i = 0;
        while ((c = scanner.next()) != null) {
            Assert.assertEquals("for character '" + c.getChar() + "'", c.getLine(), expectedData[i][0]);
            Assert.assertEquals("for character '" + c.getChar() + "'", c.getPos(), expectedData[i][1]);
            i++;
        }
    }

}
