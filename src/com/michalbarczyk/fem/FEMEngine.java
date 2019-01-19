package com.michalbarczyk.fem;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.apache.commons.math3.analysis.integration.IterativeLegendreGaussIntegrator;
import org.apache.commons.math3.analysis.integration.SimpsonIntegrator;
import org.apache.commons.math3.analysis.integration.UnivariateIntegrator;
import org.apache.commons.math3.linear.*;

public class FEMEngine extends ApplicationFrame {

    private int N;
    private double k;
    private TestFunction[] testFunctions;
    private TestFunctionDerivative[] testFunctionDerivatives;
    private RealMatrix B;
    private RealMatrix L;
    private RealMatrix W;
    private  double elementWidth;
    private ResultFunction resultFunction;
    private F f;

    public static void main(String[] args) {


        FEMEngine engine = new FEMEngine(8,1, "test");
        engine.createFunctions();

        System.out.print(engine.calcB(1,2));

    }

    public FEMEngine(int N, int k, String title) {

        super(title);
        this.N = N;
        this.k = k;
        this.testFunctions = new TestFunction[N+1];
        this.testFunctionDerivatives = new TestFunctionDerivative[N+1];
        this.B = new Array2DRowRealMatrix();
        this.L = new Array2DRowRealMatrix();
        this.W = new Array2DRowRealMatrix();
        this.f = new F();

    }

    public void plot() {

        final XYSeries series = new XYSeries("Random Data");

        for (double x = 0.0; x <= 1.0; x += 0.01) {
            series.add(x, resultFunction.value(x));
        }

        final XYSeriesCollection data = new XYSeriesCollection(series);
        final JFreeChart chart = ChartFactory.createXYLineChart(
                "XY Series Demo",
                "X",
                "Y",
                data,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(chartPanel);
    }

    public void calculate() {

        createFunctions();

        createMatrixB();

        createMatrixL();

        solve();

        plot();
    }

    private void createFunctions() {
        elementWidth = 1.0 / (double)N;

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
        //System.out.print(B);
    }

    private void createMatrixL() {

        L = L.createMatrix(N, 1);

        for (int j = 0; j < N; j++) {

            L.setEntry(j, 0, calcL(j+1));
        }

        //System.out.print(L);
    }

    private void solve() {
        DecompositionSolver solver = new LUDecomposition(B).getSolver();
        W.createMatrix(N, 1);
        W = solver.solve(L);
        System.out.print(W);
        this.resultFunction = new ResultFunction(W, testFunctions);
    }

    private double calcB(int eI, int eJ) {
        /*UnivariateIntegrator integrator = new IterativeLegendreGaussIntegrator(10, 0.0001, 0.0001);


        if (Math.abs(eI - eJ) > 1)
            return 0.0;

        double result = 0.0;

        double from = Math.max((Math.min(eI, eJ)-1) * elementWidth, 0);
        double to = Math.min((Math.min(eI, eJ)+1) * elementWidth, 1);

        CombinedDoubleFunction function = new CombinedDoubleFunction(this.k, testFunctionDerivatives[eI], testFunctionDerivatives[eJ]);
        result += integrator.integrate(1000000000, function, 0, 1);

        function = new CombinedDoubleFunction(1.0, testFunctionDerivatives[eI], testFunctions[eJ]);
        result += integrator.integrate(1000000000, function, 0, 1);

        function = new CombinedDoubleFunction(this.k, testFunctionDerivatives[eI], testFunctions[eJ]);
        result += function.value(0.0);

        return result;*/

        if (Math.abs(eI-eJ) > 1)
            return 0;

        double integralP, integralQ;
        if (eI > eJ) {
            integralP = elementWidth * 0.5 * N;
            integralQ = elementWidth * N * N * -1.0;


        }

        if (eI < eJ) {
            integralP = elementWidth * -0.5 * N;
            integralQ = elementWidth * N * N * -1.0;
        }

        else {
            integralP = 0.0;
            integralQ = elementWidth * N * N * 2.0;
        }

        return this.k * integralQ + integralP;
    }

    private double calcL(int eJ) {
        /*UnivariateIntegrator integrator = new IterativeLegendreGaussIntegrator(10, 0.0001, 0.0001);

        double from = Math.max((eJ-1) * elementWidth, 0);
        double to = Math.min((eJ+1) * elementWidth, 1);

        double result = 0.0;

        CombinedDoubleFunction function1 = new CombinedDoubleFunction(1.0, f, testFunctions[eJ]);
        result += integrator.integrate(1000000000, function1, 0, 1);

        CombinedSingleFunction function2 = new CombinedSingleFunction(this.k * 5, testFunctionDerivatives[eJ]);
        result += integrator.integrate(1000000000, function2, 0, 1);

        function2 = new CombinedSingleFunction(5, testFunctions[eJ]);
        result += integrator.integrate(1000000000, function2, 0, 1);

        result += 3.0 * this.k * testFunctions[eJ].value(1.0);

        return result;*/

        UnivariateIntegrator integrator = new SimpsonIntegrator();

        double integralT, integralS = 0.0, integralR;

        if (eJ == 0 || eJ == N) integralT = elementWidth * 2.5;
        else integralT = elementWidth * 5;

        if (eJ == 0) integralS = elementWidth * N * -1.0;
        if (eJ == N) integralS = elementWidth * N;
        integralR = integrator.integrate(1000000000, new CombinedDoubleFunction(1,f, testFunctions[eJ]), 0, 1);

        return integralT + integralS + integralR + 3*k*testFunctions[eJ].value(1);
    }
}
