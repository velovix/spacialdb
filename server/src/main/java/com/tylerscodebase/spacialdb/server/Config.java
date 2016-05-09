package com.tylerscodebase.spatialdb.server;

import java.util.Map;
import java.util.HashMap;

public class Config {

    private static Map<String, String> config;

    static {
        config = new HashMap<String, String>();

        config.put("PointToPointCollision", "simple");
        config.put("PointToLineCollision", "cross product");
        config.put("PointToRectangleCollision", "simple");
        config.put("PointToCircleCollision", "pythagorean");
        config.put("PointToPolygonCollision", "ray casting");

        config.put("LineToLineCollision", "cross product");
        config.put("LineToRectangleCollision", "side intersection");
        config.put("LineToCircleCollision", "discriminant");
        config.put("LineToPolygonCollision", "side intersection");

        config.put("RectangleToRectangleCollision", "point collisions");
        config.put("RectangleToCircleCollision", "side collisions");
        config.put("RectangleToPolygonCollision", "side collisions");

        config.put("CircleToCircleCollision", "side collisions");
        config.put("CircleToPolygonCollision", "side collisions");

        config.put("PolygonToPolygonCollision", "side collisions");
    }

    /**
     * Returns true if a configuration item with the given name exists.
     * @return true if config exists
     */
    public static boolean contains(String name) {
        return config.containsKey(name);
    }

    /**
     * Sets the configuration item with the given name to the given value.
     * @param name name of config item
     * @param value value to set config item to
     */
    public static void set(String name, String value) {
        config.put(name, value);
    }

    /**
     * Returns the value of the configuration item.
     * @return config value
     */
    public static String get(String name) {
        if (config.containsKey(name)) {
            return config.get(name);
        } else {
            return null;
        }
    }

}
