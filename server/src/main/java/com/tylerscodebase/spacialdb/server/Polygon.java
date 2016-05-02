package com.tylerscodebase.spacialdb.server;

import java.util.ArrayList;

public class Polygon implements Shape {

    ArrayList<Point> points;
    ArrayList<Line> lines;

    public Polygon(ArrayList<Double> coords) {
        // Create the points the polygon is made of
        points = new ArrayList<>();
        for (int i=0; i<coords.size(); i+=2) {
            points.add(new Point(coords.get(i), coords.get(i+1)));
        }

        // Create the sides the polygon creates
        lines = new ArrayList<>();
        for (int i=0; i<coords.size(); i+=2) {
            if (i < coords.size()-1) {
                lines.add(new Line(coords.get(i), coords.get(i+1), coords.get(i+2), coords.get(i+3)));
            } else {
                // Wrap around from the last point to the first
                lines.add(new Line(coords.get(i), coords.get(i+1), coords.get(0), coords.get(1)));
            }
        }
    }

    public ArrayList<Line> getLines() {
        return lines;
    }

    /**
     * Pretty-prints the circle.
     * @return formatted circle
     */
    public String toString() {
        String out = "Polygon(";

        for (int i=0; i<points.size(); i++) {
            out += "x" + i + ": " + points.get(i).getX() + ", y" + i + ": " + points.get(i).getY();
            if (i < points.size()-1) {
                out += ", ";
            }
        }

        out += ")";
        return out;
    }

    public boolean collidesWith(Shape shape) {
        throw new UnsupportedOperationException();
    }

}
