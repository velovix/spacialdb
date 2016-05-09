package com.tylerscodebase.spatialdb.server;

import java.util.List;

/**
 * Represents a single point.
 */
public class Point implements Shape {

    // When using ray casting, we can't have a line of actual infinite length.
    // This constant decides how long the ray will actually be.
    private final double INFINITE_RAY_LENGTH = 99999;

    private double x, y;

    /**
     * Constructs a point at the given position.
     * @param x position in the x direction
     * @param y position in the y direction
     */
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the x value of the point.
     * @return x value
     */
    public double getX() {
        return x;
    }

    /**
     * Returns the y value of the point.
     * @return y value
     */
    public double getY() {
        return y;
    }

    /**
     * Calculates the dot product of the two points.
     * @param p point to calculate the dot product with
     * @return dot product
     */
    public double dotProduct(Point p) {
        return x * p.x + y * p.y;
    }

    /**
     * Calculates the distance between this point and the given point.
     * @param p point to calculate the distance from
     * @return distance
     */
    public double distance(Point p) {
        return Math.sqrt(Math.pow(p.x - x, 2) + Math.pow(p.y -  y, 2));
    }

    /**
     * Pretty-prints the point.
     * @return formatted point
     */
    public String toString() {
        return "Point(x: " + getX() + ", y: " + getY() + ")";
    }

    public double sign(Point p1, Point p2) {
        return (getX() - p2.getX()) * (p1.getY() - p2.getY()) - (p1.getX() - p2.getX()) * (getY() - p2.getY());
    }

    /**
     * Returns true if the point collides with the given shape.
     * @param shape the shape to test against
     * @return true if the shapes collide
     * @throws UnsupportedOperationException if the point method doesn't know how to test collisions with the given shape
     */
    public boolean collidesWith(Shape shape) {
        if (shape instanceof Point) {
            // A point colliding with a point.
            Point point = (Point) shape;
            String configKey = "PointToPointCollision";

            if (Config.get(configKey) == "simple") {
                // Check if the two points share the same position
                if (point.getX() == getX() && point.getY() == getY()) {
                    return true;
                } else {
                    return false;
                }
            } else {
                throw new RuntimeException("invalid " + configKey + " value " + Config.get("PointToPointCollision"));
            }
        } else if (shape instanceof Line) {
            // A point colliding with a line
            Line line = (Line) shape;
            String configKey = "PointToLineCollision";

            if (Config.get(configKey) == "cross product") {
                // Calculate the cross product of the vectors linePnt1 -> point
                // and linePnt1 -> linePnt2
                double dxc = getX() - line.getP1().getX();
                double dyc = getY() - line.getP1().getY();
                double dx1 = line.getP2().getX() - line.getP1().getX();
                double dy1 = line.getP2().getY() - line.getP1().getY();

                double cross = dxc * dy1 - dyc * dx1;
                return cross == 0;
            } else {
                throw new RuntimeException("invalid " + configKey + " value" + Config.get(configKey));
            }
        } else if (shape instanceof Rectangle) {
            // A point colliding with a rectangle
            Rectangle rectangle = (Rectangle) shape;
            String configKey = "PointToRectangleCollision";

            if (Config.get(configKey) == "simple") {
                // Calculate if the point is between the start of the rectangle
                // and the end points
                if (rectangle.getPos().getX() >= getX() && rectangle.getPos().getX() + rectangle.getWidth() <= getX() &&
                    rectangle.getPos().getY() >= getY() && rectangle.getPos().getY() + rectangle.getHeight() <= getY()) {
                    return true;
                } else {
                    return false;
                }
            } else {
                throw new RuntimeException("invalid " + configKey + " value " + Config.get(configKey));
            }
        } else if (shape instanceof Circle) {
            // A point colliding with a circle
            Circle circle = (Circle) shape;
            String configKey = "PointToCircleCollision";

            if (Config.get(configKey) == "pythagorean") {
                // Calculate if the distance between the circle's center and
                // the point is less than or equal to the radius of the circle.
                double dist = Math.pow(getX() - circle.getCenter().getX(), 2) + Math.pow(getY() - circle.getCenter().getY(), 2);
                return dist <= Math.pow(circle.getRadius(), 2);
            } else {
                throw new RuntimeException("invalid " + configKey + " value " + Config.get(configKey));
            }
        } else if (shape instanceof Polygon) {
            // A point colliding with a polygon
            Polygon polygon = (Polygon) shape;
            String configKey = "PointToPolygonCollision";

            if (Config.get(configKey) == "ray casting") {
                // Create a ray that runs from the point to the right and check
                // how many of the polygon's lines it crosses.
                Line ray = new Line(getX(), getY(), getX() + INFINITE_RAY_LENGTH, getY() + INFINITE_RAY_LENGTH);
                List<Line> lines = polygon.getLines();

                int intersectionPoint = 0;
                for (Line line : lines) {
                    if (ray.collidesWith(line)) {
                        intersectionPoint++;
                    }
                }

                return intersectionPoint % 2 == 1;
            } else {
                throw new RuntimeException("invalid " + configKey + " value " + Config.get(configKey));
            }
        } else {
            throw new UnsupportedOperationException();
        }
    }

}
