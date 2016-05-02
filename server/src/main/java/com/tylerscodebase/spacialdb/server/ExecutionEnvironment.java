package com.tylerscodebase.spacialdb.server;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.UUID;

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

    public ExecutionReport execute(Statement statement) {
        ExecutionReport report;

        try {
            if (statement instanceof SetStatement) {
                // Execute a set statement
                SetStatement set = (SetStatement) statement;

                if (set.getName() == null) {
                    Shape shape = ShapeFactory.newShape(set);
                    String name = UUID.randomUUID().toString();
                    shapes.put(name, shape);
                    report = new ExecutionReport("", "Created a new " + shape.toString() + " named '" + name + "'" , "");
                } else {
                    Shape shape = ShapeFactory.newShape(set);
                    shapes.put(set.getName(), shape);
                    report = new ExecutionReport("", "Created a new " + shape.toString() + " named '" + set.getName() + "'", "");
                }
            } else if (statement instanceof ConfigStatement) {
                // Execute a config statement
                ConfigStatement config = (ConfigStatement) statement;

                Config.set(config.getName(), config.getValue());

                report = new ExecutionReport("", "Set configuration parameter '" + config.getName() + "' to '" + config.getValue() + "'", "");
            } else if (statement instanceof PrintStatement) {
                // Execute a print statement
                PrintStatement print = (PrintStatement) statement;

                if (print.getType().toUpperCase().equals("SHAPE")) {
                    // Print the information of a shape
                    if (!shapes.containsKey(print.getName())) {
                        throw new StatementException("no shape exists with the name '" + print.getName() + "'");
                    }

                    report = new ExecutionReport(shapes.get(print.getName()).toString(), "Fetched info on the shape named '" + print.getName() + "'", "");
                } else if (print.getType().toUpperCase().equals("CONFIG")) {
                    // Print configuration information
                    if (!Config.contains(print.getName())) {
                        throw new StatementException("no config value exists with the name '" + print.getName() + "'");
                    }

                    report = new ExecutionReport(Config.get(print.getName()), "Fetched info on the configuration parameter named '" + print.getName() + "'", "");
                } else {
                    System.out.println("'"+print.getType().toUpperCase()+"'");
                    throw new StatementException("invalid type " + print.getType().toUpperCase());
                }
            } else {
                throw new RuntimeException("attempt to execute an unsupported statement type");
            }
        } catch (StatementException e) {
            report = new ExecutionReport("", "An error has occurred", e.getMessage());
        }
        
        return report;
    }

}
