package com.michalbarczyk.fem;

import org.apache.commons.math3.analysis.UnivariateFunction;

import static java.lang.Double.max;

public class F implements UnivariateFunction {

    @Override
    public double value(double x) {
        return 5.0 * x - 10.0;
    }
}
