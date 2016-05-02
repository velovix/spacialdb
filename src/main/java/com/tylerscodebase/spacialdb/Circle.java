package com.tylerscodebase.spacialdb;

public class Circle implements Shape {

    private Point center;
    private double r;

    public Circle(double x, double y, double r) {
        center = new Point(x, y);
        this.r = r;
    }

    public Point getCenter() {
        return center;
    }

    public double getRadius() {
        return r;
    }

    /**
     * Pretty-prints the circle.
     * @return formatted circle
     */
    public String toString() {
        return "Circle(center: " + getCenter() + ", radius: " + getRadius() + ")";
    }

    public boolean collidesWith(Shape shape) {
        throw new UnsupportedOperationException();
    }

}
