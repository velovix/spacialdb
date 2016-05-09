package com.tylerscodebase.spatialdb.server;

import java.util.List;
import java.util.ArrayList;

public class Polygon implements Shape {

    List<Point> points;
    List<Line> lines;

    public Polygon(List<Double> coords) {
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

    /**
     * Returns all the points that make up the polygon.
     * @return points
     */
    public List<Point> getPoints() {
        return points;
    }

    /**
     * Returns all the lines that make up the polygon.
     * @return lines
     */
    public List<Line> getLines() {
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
        if (shape instanceof Point) {
            // A polygon colliding with a point
            Point point = (Point) shape;
            return point.collidesWith(this);
        } else if (shape instanceof Line) {
            // A polygon colliding with a line
            Line line = (Line) shape;
            return line.collidesWith(this);
        } else if (shape instanceof Rectangle) {
            // A polygon colliding with a rectangle
            Rectangle rectangle = (Rectangle) shape;
            return rectangle.collidesWith(this);
        } else if (shape instanceof Circle) {
            // A polygon colliding with a circle
            Circle circle = (Circle) shape;
            return circle.collidesWith(this);
        } else if (shape instanceof Polygon) {
            // A polygon colliding with a polygon
            Polygon polygon = (Polygon) shape;
            String configKey = "PolygonToPolygonCollision";

            if (Config.get(configKey) == "side collisions") {
                // Check the lines of each polygon against each other
                for (Line p1l : getLines()) {
                    for (Line p2l : polygon.getLines()) {
                        if (p1l.collidesWith(p2l)) {
                            return true;
                        }
                    }
                }

                return false;
            } else {
                throw new RuntimeException("invalid " + configKey + " value " + Config.get(configKey));
            }
        } else {
            throw new UnsupportedOperationException();
        }
    }

}
