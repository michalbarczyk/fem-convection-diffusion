package com.michalbarczyk.fem;

import org.apache.commons.math3.analysis.UnivariateFunction;

public class CombinedLFunction implements UnivariateFunction {

    private UnivariateFunction first;

    CombinedLFunction(UnivariateFunction first) {
        this.first = first;
    }

    @Override
    public double value(double x) {
        return (5.0 * x - 10.0) * first.value(x);
    }
}
