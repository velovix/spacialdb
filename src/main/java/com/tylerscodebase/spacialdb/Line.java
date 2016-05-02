package com.tylerscodebase.spacialdb;

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
            return point.collidesWith(shape);
        } else if (shape instanceof Line) {
            // A line colliding with a line
            Line line = (Line) shape;
            String configKey = "LineToLineCollision";

            if (Config.get(configKey) == "cross product") {
                // Find if the two points of line A are on either side of line
                // B and vise versa by finding the cross product
                /*Line lineA = this;
                Line lineB = line;
                double a = (lineB.getP2().getX() - lineB.getP1().getX()) *
                    (lineA.getP1().getY() - lineB.getP2().getY()) -
                    (lineB.getP2().getY() - lineB.getP1().getY()) *
                    (lineA.getP1().getX() - lineB.getP2().getX());
                double b = (lineB.getP2().getX() - lineB.getP1().getX()) *
                    (lineA.getP2().getY() - libeB.getP2().getY()) -
                    (*/
            }
            return true;
        } else {
            throw new UnsupportedOperationException();
        }
    }

}
