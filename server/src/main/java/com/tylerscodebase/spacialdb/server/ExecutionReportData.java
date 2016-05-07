package com.tylerscodebase.spatialdb.server;

import javax.json.*;

/**
 * Represents data that is a result of a query.
 */
public interface ExecutionReportData {

    public JsonObject getJSON();

}
