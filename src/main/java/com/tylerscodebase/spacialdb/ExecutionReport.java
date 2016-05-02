package com.tylerscodebase.spacialdb;

import javax.json.*;

/**
 * A data structure representing information on the result of an action.
 */
public class ExecutionReport {

    private String data;
    private String info;
    private String error;

    public ExecutionReport(String data, String info, String error) {
        this.data = data;
        this.info = info;
        this.error = error;
    }

    /**
     * Returns the data that the action requested, if any.
     * @return data
     */
    public String getData() {
        return data;
    }

    /**
     * Returns a description of what the action did. This is useful to show to
     * users.
     * @return info
     */
    public String getInfo() {
        return info;
    }

    /**
     * Returns the error information if an error ocurred, or an empty string.
     * If an error occurred the caller is able to assume that the operation had
     * no effect.
     * @return error
     */
    public String getError() {
        return error;
    }

    /**
     * Constructs a JSON string from the report data.
     * @return JSON string
     */
    public String getJSON() {
        return Json.createObjectBuilder()
            .add("data", data)
            .add("info", info)
            .add("error", error)
            .build()
            .toString();
    }
}
