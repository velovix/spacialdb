package com.tylerscodebase.spatialdb.server;

import javax.json.*;

public class NumericReportData implements ExecutionReportData {

    private final String REPORT_TYPE = "numeric";

    private double number;

    /**
     * Creates a new numeric report containing the given number.
     * @param number number to send
     */
    public NumericReportData(double number) {
        this.number = number;
    }

    public JsonObject getJSON() {
        return Json.createObjectBuilder()
            .add("type", REPORT_TYPE)
            .add("data", number)
            .build();
    }
}
