package com.tylerscodebase.spatialdb.server;

import javax.json.*;

/**
 * Represents boolean data as a response to a query.
 */
public class BooleanReportData implements ExecutionReportData {

    private final String REPORT_TYPE = "boolean";

    private boolean bool;

    /**
     * Creates a new boolean report data object containing the given boolean.
     * @param boolean boolean data
     */
    public BooleanReportData(boolean bool) {
        this.bool = bool;
    }

    /**
     * Returns the data represented in JSON.
     * @return JSON object
     */
    public JsonObject getJSON() {
        return Json.createObjectBuilder()
            .add("type", REPORT_TYPE)
            .add("data", bool)
            .build();
    }
}
