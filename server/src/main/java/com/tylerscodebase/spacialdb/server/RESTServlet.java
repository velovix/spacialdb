package com.tylerscodebase.spatialdb.server;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.nio.charset.StandardCharsets;
import java.io.InputStream;
import java.io.IOException;

public class RESTServlet extends HttpServlet {

    /**
     * The maximum query size in bytes.
     */
    private final int MAX_QUERY_SIZE = 512;

    public void init() throws ServletException {
    }

    public void destroy() {
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        // Grab the execution environment
        ExecutionEnvironment environment = ExecutionEnvironment.getInstance();

        // Initiate parsing tools
        Scanner scanner = new Scanner();
        Lexer lexer = new Lexer(scanner);
        Parser parser = new Parser(lexer);

        // Read up to the maximum number of bytes from the request and convert
        // it to a string
        InputStream body = req.getInputStream();
        byte[] inputData = new byte[MAX_QUERY_SIZE];
        body.read(inputData);
        String query = new String(inputData, StandardCharsets.UTF_8);
        System.out.println("Got: '" + query + "'");

        // Put the query in the scanner
        scanner.add(query + "\n");

        // Parse and run the query
        try {
            // Create lexical tokens
            lexer.parseTokens();
            // Parse the tokens
            parser.parse();
            // Statements cannot span multiple requests, so the user is
            // finished entering input
            parser.truncateStatement();
            // Execute the finished statement
            ExecutionReport report = environment.execute(parser.getStatement());
            // Send the results
            resp.setContentType("application/json");
            resp.getWriter().print(report.getJSON());
            System.out.println("Sent: " + report.getJSON());
        } catch (ParseException e) {
            // The user entered an invalid statement
            ExecutionReport report = new ExecutionReport(new EmptyReportData(), "A parsing error has occurred", e.getMessage());
            // Send the results
            resp.setContentType("application/json");
            resp.getWriter().println(report.getJSON());
            System.out.println("Error: " + report.getJSON());
        }
    }

}
