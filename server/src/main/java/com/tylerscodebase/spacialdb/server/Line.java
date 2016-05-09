package com.tylerscodebase.spatialdb.server;

import java.util.List;
import java.util.ArrayList;

public class Line implements Shape {

    private Point p1, p2;

    public Line(double x1, double y1, double x2, double y2) {
        p1 = new Point(x1, y1);
        p2 = new Point(x2, y2);
    }

    public Point getP1() {
        return p1;
    }

    public Point getP2() {
        return p2;
    }

    /**
     * Pretty-prints the line.
     * @return formatted line
     */
    public String toString() {
        return "Line(P1: " + getP1() + ", P2: " + getP2() + ")";
    }

    public boolean collidesWith(Shape shape) {
        if (shape instanceof Point) {
            // A line colliding with a point
            Point point = (Point) shape;
            return point.collidesWith(this);
        } else if (shape instanceof Line) {
            // A line colliding with a line
            Line line = (Line) shape;
            String configKey = "LineToLineCollision";

            if (Config.get(configKey) == "cross product") {
                // Find if the two points of line A are on either side of line
                // B and vise versa by finding the cross product
                Line lineA = this;
                Line lineB = line;
                double a = (lineB.getP2().getX() - lineB.getP1().getX()) *
                    (lineA.getP1().getY() - lineB.getP2().getY()) -
                    (lineB.getP2().getY() - lineB.getP1().getY()) *
                    (lineA.getP1().getX() - lineB.getP2().getX());
                double b = (lineB.getP2().getX() - lineB.getP1().getX()) *
                    (lineA.getP2().getY() - lineB.getP2().getY()) -
                    (lineB.getP2().getY() - lineB.getP1().getY()) *
                    (lineA.getP2().getX() - lineB.getP2().getX());
                if ((a > 0 && b > 0) || (a < 0 && b < 0)) {
                    return true;
                }

                return false;
            } else {
                throw new RuntimeException("invalid " + configKey + " value " + Config.get(configKey));
            }
        } else if (shape instanceof Rectangle) {
            // A line colliding with a rectangle
            Rectangle rectangle = (Rectangle) shape;
            String configKey = "LineToRectangleCollision";

            if (Config.get(configKey) == "side intersection") {
                // Find if the line is colliding with the rectangle by checking
                // if either points of the line are inside the rectangle, then
                // by checking if the line interesects with the rectangle's
                // sides.
                if (p1.collidesWith(rectangle) || p2.collidesWith(rectangle)) {
                    return true;
                }

                List<Line> sides = rectangle.getSides();
                for (Line l : sides) {
                    if (collidesWith(l)) {
                        return true;
                    }
                }

                return false;
            } else {
                throw new RuntimeException("invalid " + configKey + " value " + Config.get(configKey));
            }
        } else if (shape instanceof Circle) {
            // A line colliding with a circle
            Circle circle = (Circle) shape;
            String configKey = "LineToCircleCollision";

            if (Config.get(configKey) == "discriminant") {
                // Check if one of the line's points is inside the sphere and
                // take a shortcut if so.
                if (p1.collidesWith(circle) || p2.collidesWith(circle)) {
                    return true;
                }

                // Direction vector of line
                Point d = new Point(p2.getX() - p1.getX(), p2.getY() - p2.getX());
                // Vector from circle's center to the start of the line
                Point f = new Point(p1.getX() - circle.getCenter().getX(), p1.getY() - circle.getCenter().getY());

                double a = d.dotProduct(d);
                double b = 2.0 * f.dotProduct(d);
                double c = f.dotProduct(f) - Math.pow(circle.getRadius(), 2);

                double discriminant = Math.pow(b, 2) - 4.0 * a * c;
                if (discriminant < 0) {
                    // The line isn't even aimed towards the circle, so we know
                    // it didn't hit.
                    return false;
                }

                discriminant = Math.sqrt(discriminant);

                double t1 = (-b - discriminant) / (2.0 * a);
                double t2 = (-b + discriminant) / (2.0 * a);

                if (t1 >= 0 && t1 <= 1) {
                    // The line has at least entered the circle.
                    return true;
                }

                if (t2 >= 0 && t2 <= 1) {
                    // The line exited the circle. This should theoretically be
                    // caught by other checks but is here just in case.
                    return true;
                }

                return false;
            } else {
                throw new RuntimeException("invalid " + configKey + " value " + Config.get(configKey));
            }
        } else if (shape instanceof Polygon) {
            // A line colliding with a polygon
            Polygon polygon = (Polygon) shape;
            String configKey = "LineToPolygonCollision";

            if (Config.get(configKey) == "side intersection") {
                // Check if one of the line's points is inside the polygon
                // and take a shortcut if so.
                if (p1.collidesWith(polygon) || p2.collidesWith(polygon)) {
                    return true;
                }

                // Check if the line intersects any lines on the polygon
                for (Line l : polygon.getLines()) {
                    if (collidesWith(l)) {
                        return true;
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
