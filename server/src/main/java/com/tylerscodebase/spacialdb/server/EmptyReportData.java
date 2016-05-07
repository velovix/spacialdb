package com.tylerscodebase.spatialdb.server;

import javax.json.*;

/**
 * Represents empty data as a response to a query.
 */
public class EmptyReportData implements ExecutionReportData {

    private final String REPORT_TYPE = "empty";

    /**
     * An empty object is returned.
     * @return empty JSON object
     */
    public JsonObject getJSON() {
        return Json.createObjectBuilder()
            .add("type", REPORT_TYPE)
            .add("data",
                    Json.createObjectBuilder()
                    .build()
                    )
            .build();
    }
}
