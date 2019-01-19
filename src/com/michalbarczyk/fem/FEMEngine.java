package com.michalbarczyk.fem;

import org.apache.commons.math3.analysis.integration.SimpsonIntegrator;
import org.apache.commons.math3.analysis.integration.TrapezoidIntegrator;
import org.apache.commons.math3.analysis.integration.UnivariateIntegrator;
import org.apache.commons.math3.linear.*;

public class FEMEngine {

    private int N;
    private double k;
    private TestFunction[] testFunctions;
    private TestFunctionDerivative[] testFunctionDerivatives;
    private RealMatrix B;
    private RealMatrix L;
    private RealMatrix W;

    public FEMEngine(int N, int k) {
        this.N = N;
        this.k = k;
        this.testFunctions = new TestFunction[N+1];
        this.testFunctionDerivatives = new TestFunctionDerivative[N+1];
        this.B = new Array2DRowRealMatrix();
        this.L = new Array2DRowRealMatrix();
        this.W = new Array2DRowRealMatrix();
    }

    public void calculate() {

        createFunctions();

        createMatrixB();

        createMatrixL();

        solve();

        System.out.print(W);

        /*
        System.out.println(testFunctions[0].value(0.0));
        System.out.println(testFunctions[1].value(0.5));
        System.out.println(testFunctions[2].value(0.75));

        System.out.println(testFunctionDerivatives[0].value(0.1));
        System.out.println(testFunctionDerivatives[1].value(0.1));
        System.out.println(testFunctionDerivatives[2].value(0.1));
        System.out.println(testFunctionDerivatives[3].value(0.1));
        System.out.println(testFunctionDerivatives[4].value(0.1));
        */

    }

    private void createFunctions() {
        double elementWidth = 1.0 / (double)N;

        for (int i = 0; i < N + 1; i++) {
            double nodeX = (double)i * elementWidth;
            testFunctions[i] = new TestFunction(N, nodeX);
            testFunctionDerivatives[i] = new TestFunctionDerivative(N, nodeX);
        }
    }

    private void createMatrixB() {
        B = B.createMatrix(N, N);

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                B.setEntry(i, j, calcB(i+1, j+1));
            }
        }
    }

    private void createMatrixL() {

        for (int j = 0; j < N; j++) {
            L = L.createMatrix(N, 1);

            L.setEntry(j, 0, calcL(j+1));
        }
    }

    private void solve() {
        DecompositionSolver solver = new LUDecomposition(B).getSolver();
        W.createMatrix(N, 1);
        W = solver.solve(L);
    }

    private double calcB(int eI, int eJ) {
        UnivariateIntegrator integrator = new TrapezoidIntegrator();

        double result = 0.0;

        CombinedBFunction function = new CombinedBFunction(this.k, testFunctionDerivatives[eI], testFunctionDerivatives[eJ]);
        result += integrator.integrate(1000000000, function, 0, 1);

        function = new CombinedBFunction(1.0, testFunctionDerivatives[eI], testFunctions[eJ]);
        result += integrator.integrate(1000000000, function, 0, 1);

        function = new CombinedBFunction(this.k, testFunctionDerivatives[eI], testFunctions[eJ]);
        result += function.value(0.0);

        return result;
    }

    private double calcL(int eJ) {
        UnivariateIntegrator integrator = new SimpsonIntegrator();

        double result = 0.0;

        CombinedLFunction function = new CombinedLFunction(testFunctions[eJ]);
        result += integrator.integrate(1000000000, function, 0, 1);

        result += 3.0 * this.k * testFunctions[eJ].value(1.0);

        return result;
    }
}
