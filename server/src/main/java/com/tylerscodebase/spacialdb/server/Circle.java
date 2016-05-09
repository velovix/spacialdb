package com.tylerscodebase.spatialdb.server;

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
        if (shape instanceof Point) {
            // A circle colliding with a point
            Point point = (Point) shape;
            return point.collidesWith(this);
        } else if (shape instanceof Line) {
            // A circle colliding with a line
            Line line = (Line) shape;
            return line.collidesWith(this);
        } else if (shape instanceof Rectangle) {
            // A circle colliding with a rectangle
            Rectangle rectangle = (Rectangle) shape;
            return rectangle.collidesWith(this);
        } else if (shape instanceof Circle) {
            // A circle colliding with a circle
            Circle circle = (Circle) shape;
            String configKey = "CircleToCircleCollision";
            
            if (Config.get(configKey) == "distance") {
                // Find if the distance between the two centers is less than
                // the sum of the radii.
                double dist = center.distance(circle.center);

                if (dist <= r + circle.r) {
                    return true;
                }

                return false;
            } else {
                throw new RuntimeException("invalid " + configKey + " value " + Config.get(configKey));
            }
        } else if (shape instanceof Polygon) {
            // A circle colliding with a polygon
            Polygon polygon = (Polygon) shape;
            String configKey = "CircleToPolygonCollision";

            if (Config.get(configKey) == "side collisions") {
                // Check if the center of the circle is in the polygon
                if (center.collidesWith(polygon)) {
                    return true;
                }

                // Check each of the sides of the polygon for collision with
                // the circle.
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
