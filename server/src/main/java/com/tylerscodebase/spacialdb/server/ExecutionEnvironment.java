package com.tylerscodebase.spatialdb.server;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.UUID;

/**
 * An environment that can execute statements and store information from those
 * statements.
 */
public class ExecutionEnvironment {

    private static ExecutionEnvironment instance;

    private Map<String, Shape> shapes;

    private ExecutionEnvironment() {
        shapes = new ConcurrentHashMap<>();
    }

    public static ExecutionEnvironment getInstance() {
        if (instance == null) {
            instance = new ExecutionEnvironment();
        }

        return instance;
    }

    /**
     * Executes a set statement.
     * @return a report of the results
     */
    private ExecutionReport executeSet(SetStatement statement) throws StatementException {
        ExecutionReport report;

        if (statement.getName() == null) {
            Shape shape = ShapeFactory.newShape(statement);
            String name = UUID.randomUUID().toString();
            shapes.put(name, shape);
            report = new ExecutionReport(new EmptyReportData(), "Created a new " + shape.toString() + " named '" + name + "'" , "");
        } else {
            Shape shape = ShapeFactory.newShape(statement);
            shapes.put(statement.getName(), shape);
            report = new ExecutionReport(new EmptyReportData(), "Created a new " + shape.toString() + " named '" + statement.getName() + "'", "");
        }

        return report;
    }

    /**
     * Executes a print statement.
     * @return a report of the results
     */
    private ExecutionReport executePrint(PrintStatement statement) throws StatementException {
        ExecutionReport report;

        if (statement.getType().toUpperCase().equals("SHAPE")) {
            if (statement.getName() == null) {
                // Print the information of all shapes
                report = new ExecutionReport(new ShapesReportData(new ArrayList<Shape>(shapes.values())), "Fetched info on all shapes", "");
            } else {
                // Print the information of a shape
                if (!shapes.containsKey(statement.getName())) {
                    throw new StatementException("no shape exists with the name '" + statement.getName() + "'");
                }

                report = new ExecutionReport(new ShapeReportData(shapes.get(statement.getName())), "Fetched info on the shape named '" + statement.getName() + "'", "");
            }
        } else if (statement.getType().toUpperCase().equals("CONFIG")) {
            // Print configuration information
            if (!Config.contains(statement.getName())) {
                throw new StatementException("no config value exists with the name '" + statement.getName() + "'");
            }

            report = new ExecutionReport(new StringReportData(Config.get(statement.getName())), "Fetched info on the configuration parameter named '" + statement.getName() + "'", "");
        } else {
            throw new StatementException("invalid type " + statement.getType().toUpperCase());
        }

        return report;
    }

    /**
     * Executers a config statement.
     * @return a report of the results
     */
    private ExecutionReport executeConfig(ConfigStatement statement) throws StatementException {
        Config.set(statement.getName(), statement.getValue());

        return new ExecutionReport(new EmptyReportData(), "Set configuration parameter '" + statement.getName() + "' to '" + statement.getValue() + "'", "");
    }

    /**
     * Exectues a statement.
     * @param statement the statement to execute
     * @return a report of what happened
     */
    public ExecutionReport execute(Statement statement) {
        ExecutionReport report;

        try {
            if (statement instanceof SetStatement) {
                SetStatement set = (SetStatement) statement;
                report = executeSet(set);
            } else if (statement instanceof ConfigStatement) {
                ConfigStatement config = (ConfigStatement) statement;
                report = executeConfig(config);
            } else if (statement instanceof PrintStatement) {
                PrintStatement print = (PrintStatement) statement;
                report = executePrint(print);
            } else {
                System.out.println(statement);
                throw new RuntimeException("attempt to execute an unsupported statement type");
            }
        } catch (StatementException e) {
            report = new ExecutionReport(new EmptyReportData(), "An error has occurred", e.getMessage());
        }
        
        return report;
    }

}
