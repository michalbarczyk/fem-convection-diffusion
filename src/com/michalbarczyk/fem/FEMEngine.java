package com.michalbarczyk.fem;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.apache.commons.math3.linear.*;

public class FEMEngine extends ApplicationFrame {

    private int N;
    private double k;
    private RealMatrix B;
    private RealMatrix L;
    private RealMatrix W;
    private double h;

    public FEMEngine(int N, double k, String title) {

        super(title);
        this.N = N;
        this.k = k;
        this.B = new Array2DRowRealMatrix();
        this.L = new Array2DRowRealMatrix();
        this.W = new Array2DRowRealMatrix();
        this.h = 1.0 / (double)N;
    }

    public void calculate() {

        createMatrixB();

        createMatrixL();

        solve();

        plot();
    }

    public void plot() {

        final XYSeries series = new XYSeries("");

        for (double x = 0.0; x < 1.01; x += 0.01) {

            double ux = getResult(x);
            series.add(x, ux);
            System.out.printf("%4.3f -> %9.6f \n", x, ux);
        }

        final XYSeriesCollection data = new XYSeriesCollection(series);
        final JFreeChart chart = ChartFactory.createXYLineChart(
                "u(x)",
                "x",
                "u",
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

    private void createMatrixB() {
        B = B.createMatrix(N+1, N+1);

        for (int w = 0; w < N+1; w++) {
            for (int c = 0; c < N+1; c++) {
                B.setEntry(w, c,0.0);
            }
        }

        B.setEntry(0,0, 1.0);

        for (int c = 2; c < N+1; c++) {
            B.setEntry(c-1, c, 0.5 - k  /h);
        }

        for (int w = 2; w < N+1; w++) {
            B.setEntry(w, w-1, - 0.5 - k / h);
        }

        for (int w = 1; w < N+1; w++) {
            B.setEntry(w, w,2.0 * k / h);
        }

        B.setEntry(N, N, k / h + 0.5);
    }

    private void createMatrixL() {

        L = L.createMatrix(N+1, 1);

        L.setEntry(0, 0, 0.0);

        for (int w = 1; w < N; w++) {

            L.setEntry(w, 0, calcL(w));
        }

        L.setEntry(N, 0, -5.0 / 6.0 * h * h + 8 * k);
    }

    private void solve() {
        DecompositionSolver solver = new LUDecomposition(B).getSolver();
        W.createMatrix(N+1, 1);
        W = solver.solve(L);
    }

    private void printW() {
        System.out.println("\n");
        for (int i = 0;  i < N+1; i++) {
            System.out.println(i + " : "+ W.getEntry(i,0));
        }
    }

    private void printL() {
        System.out.println("\n");
        for (int i = 0;  i < N+1; i++) {
            System.out.println(i + " : "+ L.getEntry(i,0));
        }
    }

    private void printB() {
        System.out.println("\n");
        for (int i = 0;  i < N+1; i++) {
            for (int j = 0;  j < N+1; j++) {
                System.out.printf("%12.6f",B.getEntry(i,j));
            }
            System.out.println("\n");
        }
    }


    private double getResult(double x) {
        double result = 5.0 * (1.0 - x);

        for (int i = 1; i < N+1; i++) {
            result += this.W.getEntry(i, 0) * getTestFunctionValue(i, x);
        }

        return result;
    }

    private double getTestFunctionValue(int i, double x) {
        double xI = (double)i * h;

        return Double.max(0, 1 - N * Math.abs(xI - x));
    }

    private double calcL(int ei) {

        /*
        double xK0 = (double)(ei-1) * h;
        double xK1 = (double)ei * h;
        double xK2 = (double)(ei+1) * h;

        return N / 2.0 * (2.0 * xK1*xK1 - xK0*xK0 - xK2*xK2) + 2.0 * h;
        */

        return 5.0 * h * (ei * h - 1);
    }
}
