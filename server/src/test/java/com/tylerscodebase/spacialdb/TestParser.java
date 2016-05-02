package com.tylerscodebase.spacialdb.server;

import java.util.ArrayList;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.framework.Assert;

public class TestParser extends TestCase {

    public TestParser(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(TestParser.class);
    }

    public void testSetStatement() throws ParseException {
        Scanner scanner = new Scanner();
        Lexer lexer = new Lexer(scanner);
        Parser parser = new Parser(lexer);

        scanner.add("set \"name\" rectangle ((3, 5) (3, 24))");

        lexer.parseTokens();
        parser.parse();

        Statement statement = parser.getStatement();
        if (statement instanceof SetStatement) {
            SetStatement setStatement = (SetStatement) statement;

            ArrayList<Tuple> tuples = setStatement.getTuples();

            Assert.assertEquals("name", setStatement.getName());
            Assert.assertEquals("rectangle", setStatement.getShape());
            Assert.assertEquals(2, tuples.size());
            Assert.assertEquals(3.0, tuples.get(0).get(0));
            Assert.assertEquals(5.0, tuples.get(0).get(1));
            Assert.assertEquals(3.0, tuples.get(1).get(0));
            Assert.assertEquals(24.0, tuples.get(1).get(1));
        } else {
            fail("constructed statement is not of the type set");
        }
    }

    public void testSetStatementNoShape() throws ParseException {
        Scanner scanner = new Scanner();
        Lexer lexer = new Lexer(scanner);
        Parser parser = new Parser(lexer);

        scanner.add("set \"name\" ((3, -5) (14, 24))");

        lexer.parseTokens();
        parser.parse();

        Statement statement = parser.getStatement();
        if (statement instanceof SetStatement) {
            SetStatement setStatement = (SetStatement) statement;

            ArrayList<Tuple> tuples = setStatement.getTuples();

            Assert.assertEquals("name", setStatement.getName());
            Assert.assertNull(setStatement.getShape());
            Assert.assertEquals(2, tuples.size());
            Assert.assertEquals(3.0, tuples.get(0).get(0));
            Assert.assertEquals(-5.0, tuples.get(0).get(1));
            Assert.assertEquals(14.0, tuples.get(1).get(0));
            Assert.assertEquals(24.0, tuples.get(1).get(1));
        } else {
            fail("constructed statement is not of the type set");
        }
    }

    public void testSetStatementNoName() throws ParseException {
        Scanner scanner = new Scanner();
        Lexer lexer = new Lexer(scanner);
        Parser parser = new Parser(lexer);

        scanner.add("set rectangle ((3, -5) (14, 24))");

        lexer.parseTokens();
        parser.parse();

        Statement statement = parser.getStatement();
        if (statement instanceof SetStatement) {
            SetStatement setStatement = (SetStatement) statement;

            ArrayList<Tuple> tuples = setStatement.getTuples();

            Assert.assertNull(setStatement.getName());
            Assert.assertEquals(2, tuples.size());
            Assert.assertEquals(3.0, tuples.get(0).get(0));
            Assert.assertEquals(-5.0, tuples.get(0).get(1));
            Assert.assertEquals(14.0, tuples.get(1).get(0));
            Assert.assertEquals(24.0, tuples.get(1).get(1));
        } else {
            fail("constructed statement is not of the type set");
        }
    }

    public void testSetStatementNoNameNoShape() throws ParseException {
        Scanner scanner = new Scanner();
        Lexer lexer = new Lexer(scanner);
        Parser parser = new Parser(lexer);

        scanner.add("set ((3, -5) (14, 24))");

        lexer.parseTokens();
        parser.parse();

        Statement statement = parser.getStatement();
        if (statement instanceof SetStatement) {
            SetStatement setStatement = (SetStatement) statement;

            ArrayList<Tuple> tuples = setStatement.getTuples();

            Assert.assertNull(setStatement.getName());
            Assert.assertNull(setStatement.getShape());
            Assert.assertEquals(2, tuples.size());
            Assert.assertEquals(3.0, tuples.get(0).get(0));
            Assert.assertEquals(-5.0, tuples.get(0).get(1));
            Assert.assertEquals(14.0, tuples.get(1).get(0));
            Assert.assertEquals(24.0, tuples.get(1).get(1));
        } else {
            fail("constructed statement is not of the type set");
        }
    }

}
