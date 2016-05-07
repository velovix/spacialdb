package com.tylerscodebase.spatialdb.server;

public class Triangle implements Shape {

    private Point p1;
    private Point p2;
    private Point p3;

    public Triangle(double x1, double y1, double x2, double y2, double x3, double y3) {
        p1 = new Point(x1, y1);
        p2 = new Point(x2, y2);
        p3 = new Point(x3, y3);
    }

    public Point getP1() {
        return p1;
    }

    public Point getP2() {
        return p2;
    }

    public Point getP3() {
        return p3;
    }

    public boolean collidesWith(Shape shape) {
        if (shape instanceof Point) {
            // A triangle colliding with a point
            Point point = (Point) shape;
            return point.collidesWith(this);
        } else {
            throw new UnsupportedOperationException();
        }
    }

}
