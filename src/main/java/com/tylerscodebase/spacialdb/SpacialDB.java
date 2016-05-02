package com.tylerscodebase.spacialdb;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

/**
 * The main class. Provides the entry point of this application
 */
public class SpacialDB {

    private static final String COLOR_RESET = "\u001B[0m";
    private static final String COLOR_RED = "\u001B[31m";

    /**
     * Emits an error message with special formatting to standard output.
     * @param message the error message
     */
    private static void error(String message) {
        System.out.println(COLOR_RED + "Error: " + COLOR_RESET + message);
    }

    /**
     * Entry point of the program.
     * @param args unused
     */
    public static void main(String[] args) throws IOException, ParseException, StatementException {
        // Initialize standard input buffer
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

        // Grab the execution environment
        ExecutionEnvironment environment = ExecutionEnvironment.getInstance();

        // Start the main loop
        while (true) {
            Scanner scanner = new Scanner();
            Lexer lexer = new Lexer(scanner);
            Parser parser = new Parser(lexer);

            // Wait for user input
            System.out.print("> ");
            scanner.add(stdin.readLine() + "\n");

            try {
                // Create lexical tokens
                lexer.parseTokens();
                while (!lexer.isSeeminglyFinished()) {
                    // Keep asking for input if the lexer doesn't think the
                    // user is finished entering input.
                    System.out.print(">> ");
                    scanner.add(stdin.readLine() + "\n");
                    lexer.parseTokens(); // Lex for new tokens
                }
                // Parse the tokens
                parser.parse();
                while (!parser.isFinished()) {
                    // Keep asking for input if the parser doesn't think the
                    // user is finished.
                    System.out.print(">> ");
                    scanner.add(stdin.readLine() + "\n");
                    lexer.parseTokens(); // Lex for new tokens
                    parser.parse(); // Parse the new tokens
                }

                // Execute the finished statement
                environment.execute(parser.getStatement());
            } catch (ParseException e) {
                // The user entered an invalid statement
                error(e.getMessage());
            }
        }

    }

}
