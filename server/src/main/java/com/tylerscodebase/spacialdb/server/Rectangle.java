package com.tylerscodebase.spatialdb.server;

import java.util.List;
import java.util.ArrayList;

/**
 * Represents a bounding rectangle, meaning a rectangle that is not skewed.
 */
public class Rectangle implements Shape {

    private Point pos;
    private double w,h;

    /**
     * Constructs a new rectangle starting at the given points and extending
     * to the given width and height
     * @param x bottom left x value
     * @param y bottom left y value
     * @param w width of the rectangle
     * @param h height of the rectangle
     */
    public Rectangle(double x, double y, double w, double h) {
        pos = new Point(x, y);
        this.w = w;
        this.h = h;
    }

    /**
     * Returns a list of all four points on the rectangle.
     * @return points
     */
    public List<Point> getPoints() {
        List<Point> points = new ArrayList<>(4);

        points.add(pos);
        points.add(new Point(pos.getX() + w, pos.getY()));
        points.add(new Point(pos.getX() + w, pos.getY() + h));
        points.add(new Point(pos.getX(), pos.getY() + h));

        return points;
    }

    /**
     * Returns the bottom left position of the rectangle.
     * @return bottom left position
     */
    public Point getPos() {
        return pos;
    }

    /**
     * Returns the width of the rectangle.
     * @return width
     */
    public double getWidth() {
        return w;
    }

    /**
     * Returns the height of the rectangle.
     * @return height
     */
    public double getHeight() {
        return h;
    }


    /**
     * Returns a list of lines creating the border of the rectangle.
     * @return side lines
     */
    public List<Line> getSides() {
        List<Line> out = new ArrayList<Line>(4);

        out.add(new Line(pos.getX(), pos.getY(), pos.getX()+w, pos.getY()));
        out.add(new Line(pos.getX()+w, pos.getY(), pos.getX()+w, pos.getY()+h));
        out.add(new Line(pos.getX()+w, pos.getY()+h, pos.getX(), pos.getY()+h));
        out.add(new Line(pos.getX(), pos.getY()+h, pos.getX(), pos.getY()));

        return out;
    }

    /**
     * Pretty-prints the rectangle.
     * @return formatted rectangle
     */
    public String toString() {
        return "Rectangle(pos: " + getPos() + ", width: " + getWidth() + ", height: " + getHeight() + ")";
    }

    public boolean collidesWith(Shape shape) {
        if (shape instanceof Point) {
            // A rectangle colliding with a point
            Point point = (Point) shape;
            return point.collidesWith(this);
        } else if (shape instanceof Line) {
            // A rectangle colliding with a line
            Line line = (Line) shape;
            return line.collidesWith(this);
        } else if (shape instanceof Rectangle) {
            // A rectangle colliding with a rectangle
            Rectangle rectangle = (Rectangle) shape;
            String configKey = "RectangleToRectangleCollision";

            if (Config.get(configKey) == "point collisions") {
                // Check if this rectangle's points are inside the other rectangle.
                for (Point p : getPoints()) {
                    if (p.collidesWith(rectangle)) {
                        return true;
                    }
                }

                // Check if the other rectangle's points are inside this rectangle.
                for (Point p : rectangle.getPoints()) {
                    if (p.collidesWith(this)) {
                        return true;
                    }
                }

                return false;
            } else {
                throw new RuntimeException("invalid " + configKey + " value " + Config.get(configKey));
            }
        } else if (shape instanceof Circle) {
            // A rectangle colliding with a circle
            Circle circle = (Circle) shape;
            String configKey = "RectangleToCircleCollision";

            if (Config.get(configKey) == "side collisions") {
                // Check if the center of the circle is in the rectangle.
                if (circle.getCenter().collidesWith(this)) {
                    return true;
                }

                // Check if the circle collides with any sides
                List<Line> sides = getSides();
                for (Line l : sides) {
                    if (l.collidesWith(circle)) {
                        return true;
                    }
                }

                return false;
            } else {
                throw new RuntimeException("invalid " + configKey + " value " + Config.get(configKey));
            }
        } else if (shape instanceof Polygon) {
            // A rectangle colliding with a polygon
            Polygon polygon = (Polygon) shape;
            String configKey = "RectangleToPolygonCollision";

            if (Config.get(configKey) == "side collisions") {
                // Check if any polygon points are in the rectangle.
                for (Point p : polygon.getPoints()) {
                    if (p.collidesWith(this)) {
                        return true;
                    }
                }

                // Check if any rectangle points are in the polygon.
                for (Point p : getPoints()) {
                    if (p.collidesWith(polygon)) {
                        return true;
                    }
                }

                // Check if any rectangle lines intersect with polygon lines.
                for (Line pl : polygon.getLines()) {
                    for (Line rl : getSides()) {
                        if (rl.collidesWith(pl)) {
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
