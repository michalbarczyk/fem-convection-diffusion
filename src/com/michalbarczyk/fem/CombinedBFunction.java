package com.michalbarczyk.fem;

import org.apache.commons.math3.analysis.UnivariateFunction;

public class CombinedBFunction implements UnivariateFunction {

    private double k;
    private UnivariateFunction first;
    private UnivariateFunction second;

    CombinedBFunction(double k, UnivariateFunction first, UnivariateFunction second) {
        this.k = k;
        this.first = first;
        this.second = second;
    }

    @Override
    public double value(double x) {
        return k * (first.value(x) - 5.0) * second.value(x);
    }
}
