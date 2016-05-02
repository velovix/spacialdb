package com.tylerscodebase.spacialdb.server;

public interface Shape {

    public boolean collidesWith(Shape shape);

    public String toString();

}
