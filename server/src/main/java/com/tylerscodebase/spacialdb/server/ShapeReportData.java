package com.tylerscodebase.spatialdb.server;

import javax.json.*;

/**
 * Represents shape data as a response to a query.
 */
public class ShapeReportData implements ExecutionReportData {

    private final String REPORT_TYPE = "shape";

    private Shape shape;

    /**
     * Creates a new shape report data object containing the given shape.
     * @param shape shape data
     */
    public ShapeReportData(Shape shape) {
        this.shape = shape;
    }

    public JsonObject getJSONObj() {
        JsonObject shapeObj;

        if (shape instanceof Point) {
            // The stored shape is a point
            Point point = (Point) shape;
            shapeObj = Json.createObjectBuilder()
                .add("type", "point")
                .add("x", point.getX())
                .add("y", point.getY())
                .build();
        } else if (shape instanceof Line) {
            // The stored shape is a line
            Line line = (Line) shape;
            shapeObj = Json.createObjectBuilder()
                .add("type", "line")
                .add("x1", line.getP1().getX())
                .add("y1", line.getP1().getY())
                .add("x2", line.getP2().getX())
                .add("y2", line.getP2().getY())
                .build();
        } else if (shape instanceof Circle) {
            // The stored shape is a circle
            Circle circle = (Circle) shape;
            shapeObj = Json.createObjectBuilder()
                .add("type", "circle")
                .add("x", circle.getCenter().getX())
                .add("y", circle.getCenter().getY())
                .add("r", circle.getRadius())
                .build();
        } else if (shape instanceof Rectangle) {
            // The stored shape is a rectangle
            Rectangle rectangle = (Rectangle) shape;
            shapeObj = Json.createObjectBuilder()
                .add("type", "rectangle")
                .add("x", rectangle.getPos().getX())
                .add("y", rectangle.getPos().getY())
                .add("w", rectangle.getWidth())
                .add("h", rectangle.getHeight())
                .build();
        } else if (shape instanceof Polygon) {
            // The stored shape is a polygon
            Polygon polygon = (Polygon) shape;
            JsonArrayBuilder arrBuilder = Json.createArrayBuilder();
            for (Point p : polygon.getPoints()) {
                arrBuilder.add(
                        Json.createObjectBuilder()
                                .add("x", p.getX())
                                .add("y", p.getY()));
            }
            shapeObj = Json.createObjectBuilder()
                .add("type", "polygon")
                .add("points", arrBuilder)
                .build();
        } else {
            throw new RuntimeException("unsupported shape type " + shape);
        }

        return shapeObj;
    }

    /**
     * Returns the data represented in JSON.
     * @return JSON object
     */
    public JsonObject getJSON() {
        return Json.createObjectBuilder()
            .add("type", REPORT_TYPE)
            .add("data", getJSONObj())
            .build();
    }
}
