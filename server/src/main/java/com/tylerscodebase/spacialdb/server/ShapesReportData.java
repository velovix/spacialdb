package com.tylerscodebase.spatialdb.server;

import javax.json.*;
import java.util.List;

/**
 * Represents the data of multiple shapes a response to a query.
 */
public class ShapesReportData implements ExecutionReportData {

    private final String REPORT_TYPE = "shapes";

    private List<Shape> shapes;

    /**
     * Creates a new shapes report data object containing the given shapes.
     * @param shape shape data
     */
    public ShapesReportData(List<Shape> shapes) {
        this.shapes = shapes;
    }

    /**
     * Returns the data represented in JSON.
     * @return JSON object
     */
    public JsonObject getJSON() {
        JsonArrayBuilder arrBuilder = Json.createArrayBuilder();

        for (Shape shape : shapes) {
            ShapeReportData report = new ShapeReportData(shape);
            arrBuilder.add(report.getJSONObj());
        }

        return Json.createObjectBuilder()
            .add("type", REPORT_TYPE)
            .add("data", arrBuilder)
            .build();
    }
}
