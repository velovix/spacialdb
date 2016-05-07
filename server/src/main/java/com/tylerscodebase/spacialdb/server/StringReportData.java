package com.tylerscodebase.spatialdb.server;

import javax.json.*;

/**
 * Represents string data as a response to a query.
 */
public class StringReportData implements ExecutionReportData {

    private final String REPORT_TYPE = "string";

    private String string;

    /**
     * Creates a new string report data object containing the given string.
     * @param string string data
     */
    public StringReportData(String string) {
        this.string = string;
    }

    /**
     * Returns the data represented in JSON.
     * @return JSON object
     */
    public JsonObject getJSON() {
        return Json.createObjectBuilder()
            .add("type", REPORT_TYPE)
            .add("data", string)
            .build();
    }
}
