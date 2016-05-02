package com.tylerscodebase.spacialdb;

import java.util.ArrayList;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.framework.Assert;

@SuppressWarnings("unchecked")
public class TestLexer extends TestCase {

    public TestLexer(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(TestLexer.class);
    }

    public void testParsesAction() throws ParseException {
        Scanner scanner = new Scanner();
        Lexer lexer = new Lexer(scanner);

        scanner.add("test ");

        lexer.parseTokens();
        Token actionToken = lexer.nextToken();

        Assert.assertEquals(actionToken.getType(), Token.TokenType.KEYWORD);
        Assert.assertEquals((String) actionToken.getData(), "test");
        Assert.assertNull(lexer.nextToken());
    }

    public void testParsesString() throws ParseException {
        Scanner scanner = new Scanner();
        Lexer lexer = new Lexer(scanner);

        scanner.add("\"test\"");

        lexer.parseTokens();
        Token stringToken = lexer.nextToken();

        Assert.assertEquals(Token.TokenType.STRING, stringToken.getType());
        Assert.assertEquals("test", (String) stringToken.getData());
        Assert.assertNull(lexer.nextToken());
    }
    
    public void testParsesTuples() throws ParseException {
        Scanner scanner = new Scanner();
        Lexer lexer = new Lexer(scanner);

        scanner.add("((7, 45) (18, 32))");
        scanner.add("( (7, 45) (18, 32) )");
        scanner.add("((7, 45)(18, 32))");
        scanner.add("(        (7, 45)    (18, 32)    )");

        lexer.parseTokens();
        Token coordToken;

        int i=0;
        while ((coordToken = lexer.nextToken()) != null) {
            Assert.assertTrue(i<4);
            Assert.assertEquals(Token.TokenType.TUPLES, coordToken.getType());
            ArrayList<String> data = (ArrayList<String>) coordToken.getData();
            Assert.assertEquals("for token number " + i, 2, data.size());
            Assert.assertEquals("for token number " + i, "7, 45", data.get(0));
            Assert.assertEquals("for token number " + i, "18, 32", data.get(1));
            i++;
        }

    }

    public void testParsesExpression() throws ParseException {
        Scanner scanner = new Scanner();
        Lexer lexer = new Lexer(scanner);

        scanner.add("set    \"test name\"   ((4, 5, 3) (1, 2, 27) (45, 35, 20) (18, 19, 20))");

        lexer.parseTokens();

        Token actionToken = lexer.nextToken();
        Assert.assertEquals(Token.TokenType.KEYWORD, actionToken.getType());
        Assert.assertEquals("set", (String) actionToken.getData());

        Token nameToken = lexer.nextToken();
        Assert.assertEquals(Token.TokenType.STRING, nameToken.getType());
        Assert.assertEquals("test name", (String) nameToken.getData());

        Token coordsToken = lexer.nextToken();
        Assert.assertEquals(Token.TokenType.TUPLES, coordsToken.getType());
        ArrayList<String> coords = (ArrayList<String>) coordsToken.getData();
        Assert.assertEquals(4, coords.size());
        Assert.assertEquals("4, 5, 3", coords.get(0));
        Assert.assertEquals("1, 2, 27", coords.get(1));
        Assert.assertEquals("45, 35, 20", coords.get(2));
        Assert.assertEquals("18, 19, 20", coords.get(3));

        Assert.assertNull(lexer.nextToken());
    }

}
