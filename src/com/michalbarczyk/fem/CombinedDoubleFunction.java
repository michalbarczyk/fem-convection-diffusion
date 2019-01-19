package com.michalbarczyk.fem;

import org.apache.commons.math3.analysis.UnivariateFunction;

public class CombinedDoubleFunction implements UnivariateFunction {

    private double k;
    private UnivariateFunction first;
    private UnivariateFunction second;

    CombinedDoubleFunction(double k, UnivariateFunction first, UnivariateFunction second) {
        this.k = k;
        this.first = first;
        this.second = second;
    }

    @Override
    public double value(double x) {
        return k * first.value(x) * second.value(x);
    }
}
