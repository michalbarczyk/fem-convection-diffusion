package com.michalbarczyk.fem;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.linear.RealMatrix;

public class ResultFunction implements UnivariateFunction {

    private double[] W;
    private TestFunction[] testFunctions;

    ResultFunction(RealMatrix W, TestFunction[] testFunctions) {
        this.W = W.getColumn(0);
        this.testFunctions = testFunctions;
    }

    @Override
    public double value(double x){

        double v = 0.0 ;//(1.0-x) * 5.0;

        for (int i = 0; i < W.length; i++) {
            v += W[i] * testFunctions[i+1].value(x);
        }

        return v;
    }
}
