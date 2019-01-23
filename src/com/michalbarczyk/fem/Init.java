package com.michalbarczyk.fem;

import org.jfree.ui.RefineryUtilities;

public class Init {

    public static void main(String[] args) {

        FEMEngine femEngine = new FEMEngine(Integer.valueOf(args[0]), Double.valueOf(args[1]), "conv-diff");
        femEngine.calculate();
        femEngine.pack();
        RefineryUtilities.centerFrameOnScreen(femEngine);
        femEngine.setVisible(true);
    }
}
