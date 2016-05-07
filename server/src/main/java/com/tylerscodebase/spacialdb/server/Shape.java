package com.tylerscodebase.spatialdb.server;

public interface Shape {

    public boolean collidesWith(Shape shape);

    public String toString();

}
