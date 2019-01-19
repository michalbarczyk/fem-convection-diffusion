package com.michalbarczyk.fem;

import org.apache.commons.math3.analysis.UnivariateFunction;

import static java.lang.Double.max;

public class TestFunction implements UnivariateFunction {

    private int N;
    private double nodeX;

    TestFunction(int N, double nodeX) {
        this.N = N;
        this.nodeX = nodeX;
    }

    @Override
    public double value(double x) {
        return max(0, 1 - N * Math.abs(x - nodeX));
    }

}
