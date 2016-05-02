package com.tylerscodebase.spacialdb.server;

import java.util.ArrayList;

public class Tuple {

    private ArrayList<Double> vals;

    public Tuple(ArrayList<Double> vals) {
        this.vals = vals;
    }

    public double get(int i) {
        return vals.get(i);
    }

    public double size() {
        return vals.size();
    }

}
