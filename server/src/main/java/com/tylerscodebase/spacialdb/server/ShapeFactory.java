package com.tylerscodebase.spatialdb.server;

import java.util.ArrayList;

public class ShapeFactory {

    private static Point makePoint(ArrayList<Tuple> tuples) throws StatementException {
        if (tuples.size() != 1) {
            throw new StatementException("expected one single tuple ((x, y)) but got " + tuples.size());
        }
        if (tuples.get(0).size() != 2) {
            throw new StatementException("expected first tuple to be of length two (x, y), but got " +
                    tuples.get(0).size());
        }

        double x = tuples.get(0).get(0);
        double y = tuples.get(0).get(1);

        return new Point(x, y);
    }

    private static Line makeLine(ArrayList<Tuple> tuples) throws StatementException {
        if (tuples.size() != 2) {
            throw new StatementException("expected two tuples ((x1, y1) (x2, y2)) but got " + tuples.size());
        }
        if (tuples.get(0).size() != 2) {
            throw new StatementException("expected first tuple to be of length two (x, y), but got " +
                    tuples.get(0).size());
        }
        if (tuples.get(1).size() != 2) {
            throw new StatementException("expected second tuple to be of length two (x, y), but got " +
                    tuples.get(1).size());
        }

        double x1 = tuples.get(0).get(0);
        double y1 = tuples.get(0).get(1);
        double x2 = tuples.get(1).get(0);
        double y2 = tuples.get(1).get(1);

        return new Line(x1, y1, x2, y2);
    }

    private static Rectangle makeRectangle(ArrayList<Tuple> tuples) throws StatementException {
        if (tuples.size() != 2) {
            throw new StatementException("expected two tuples ((x, y) (w, h)) but got " + tuples.size());
        }
        if (tuples.get(0).size() != 2) {
            throw new StatementException("expected first tuple to be of length two (x, y), but got " +
                    tuples.get(0).size());
        }
        if (tuples.get(1).size() != 2) {
            throw new StatementException("expected second tuple to be of length two (w, h), but got " +
                    tuples.get(0).size());
        }

        double x = tuples.get(0).get(0);
        double y = tuples.get(0).get(1);
        double w = tuples.get(1).get(0);
        double h = tuples.get(1).get(1);
        if (w < 0) {
            throw new StatementException("rectangle width must be not be a negative number");
        }
        if (h < 0) {
            throw new StatementException("rectangle height must not be a negative number");
        }

        return new Rectangle(x, y, w, h);
    }

    private static Triangle makeTriangle(ArrayList<Tuple> tuples) throws StatementException {
        if (tuples.size() != 3) {
            throw new StatementException("expected three tuples ((x1, y1) (x2, y2) (x3, y3)) but got " + tuples.size());
        }
        if (tuples.get(0).size() != 2) {
            throw new StatementException("expected first tuple to be of length two (x, y), but got " +
                    tuples.get(0).size());
        }
        if (tuples.get(1).size() != 2) {
            throw new StatementException("expected second tuple to be of length two (x, y), but got " +
                    tuples.get(1).size());
        }
        if (tuples.get(2).size() != 2) {
            throw new StatementException("expected third tuple to be of length two (x, y), but got " +
                    tuples.get(2).size());
        }

        double x1 = tuples.get(0).get(0);
        double y1 = tuples.get(0).get(1);
        double x2 = tuples.get(1).get(0);
        double y2 = tuples.get(1).get(1);
        double x3 = tuples.get(2).get(0);
        double y3 = tuples.get(2).get(1);

        return new Triangle(x1, y1, x2, y2, x3, y3);
    }

    private static Circle makeCircle(ArrayList<Tuple> tuples) throws StatementException {
        if (tuples.size() != 2) {
            throw new StatementException("expected two tuples ((x, y) (r)) but got " + tuples.size());
        }
        if (tuples.get(0).size() != 2) {
            throw new StatementException("expected first tuple to be of length two (x, y), but got " +
                    tuples.get(0).size());
        }
        if (tuples.get(1).size() != 1) {
            throw new StatementException("expected second tuple to be of length one (r), but got " +
                    tuples.get(0).size());
        }

        double x = tuples.get(0).get(0);
        double y = tuples.get(0).get(1);
        double r = tuples.get(1).get(0);
        if (r < 0) {
            throw new StatementException("circle radius must not be a negative number");
        }

        return new Circle(x, y, r);
    }

    public static Polygon makePolygon(ArrayList<Tuple> tuples) throws StatementException {
        ArrayList<Double> coords = new ArrayList<>(tuples.size() * 2);

        for (int i=0; i<tuples.size(); i++) {
            if (tuples.get(i).size() != 2) {
                throw new StatementException("expected tuple " + i + " to be of length two (x, y), but got " +
                        tuples.get(i).size());
            }

            coords.add(tuples.get(i).get(0));
            coords.add(tuples.get(i).get(1));
        }

        return new Polygon(coords);
    }

    public static Shape makeShape(ArrayList<Tuple> tuples) throws StatementException {
        switch (tuples.size()) {
        case 0:
            throw new StatementException("no shape can be made with zero tuples");
        case 1:
            System.out.println("Assuming the shape is a point");
            return makePoint(tuples);
        case 2:
            System.out.println("Assuming the shape is a line");
            return makeLine(tuples);
        case 3:
            System.out.println("Assuming the shape is a triangle");
            return makeTriangle(tuples);
        default:
            System.out.println("Assuming the shape is a polygon");
            return makePolygon(tuples);
        }
    }

    public static Shape newShape(SetStatement statement) throws StatementException {
        if (statement.getShape() == null) {
            return makeShape(statement.getTuples());
        }

        switch (statement.getShape().toUpperCase()) {
        case "POINT":
            return makePoint(statement.getTuples());
        case "LINE":
            return makeLine(statement.getTuples());
        case "RECTANGLE":
            return makeRectangle(statement.getTuples());
        case "CIRCLE":
            return makeCircle(statement.getTuples());
        case "TRIANGLE":
            return makeTriangle(statement.getTuples());
        default:
            throw new StatementException("unknown shape type '" + statement.getShape().toUpperCase() + "'");
        }
    }

}
