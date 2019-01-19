package com.michalbarczyk.fem;

import org.apache.commons.math3.analysis.UnivariateFunction;

public class CombinedSingleFunction implements UnivariateFunction {

    private double k;
    private UnivariateFunction first;

    CombinedSingleFunction(double k, UnivariateFunction first) {
        this.k = k;
        this.first = first;
    }

    @Override
    public double value(double x) {
        return k * first.value(x) ;
    }
}
