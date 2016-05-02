package com.tylerscodebase.spacialdb;

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
    }

    public static boolean contains(String name) {
        return config.containsKey(name);
    }

    public static void set(String name, String value) {
        config.put(name, value);
    }

    public static String get(String name) {
        if (config.containsKey(name)) {
            return config.get(name);
        } else {
            return null;
        }
    }

}
