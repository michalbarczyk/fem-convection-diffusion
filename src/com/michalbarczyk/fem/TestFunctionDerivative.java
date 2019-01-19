package com.michalbarczyk.fem;

import org.apache.commons.math3.analysis.UnivariateFunction;

public class TestFunctionDerivative implements UnivariateFunction {

    private int N;
    private double nodeX;

    TestFunctionDerivative(int N, double nodeX) {
        this.N = N;
        this.nodeX = nodeX;
    }

    @Override
    public double value(double x) {

        double leftBorder = nodeX - 1.0 / (double)N;
        double rightBorder = nodeX + 1.0 / (double)N;

        if (x > leftBorder && x < nodeX)
            return (double)N;
        if (x < rightBorder && x >= nodeX)
            return -1.0 * ((double)N);
        else
            return 0.0;
    }
}
