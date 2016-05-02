package com.tylerscodebase.spacialdb.server;

public class Rectangle implements Shape {
                
    private Point pos;
    private double w,h;

    public Rectangle(double x, double y, double w, double h) {
        pos = new Point(x, y);
        this.w = w;
        this.h = h;
    }

    public Point getPos() {
        return pos;
    }

    public double getWidth() {
        return w;
    }

    public double getHeight() {
        return h;
    }

    /**
     * Pretty-prints the rectangle.
     * @return formatted rectangle
     */
    public String toString() {
        return "Rectangle(pos: " + getPos() + ", width: " + getWidth() + ", height: " + getHeight() + ")";
    }

    public boolean collidesWith(Shape shape) {
        throw new UnsupportedOperationException();
    }

}
