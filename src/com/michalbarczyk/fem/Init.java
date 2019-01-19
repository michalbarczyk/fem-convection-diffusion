package com.michalbarczyk.fem;

import org.jfree.ui.RefineryUtilities;

public class Init {

    public static void main(String[] args) {

        final FEMEngine femEngine = new FEMEngine(3,1, "conv-diff");
        femEngine.calculate();
        femEngine.pack();
        RefineryUtilities.centerFrameOnScreen(femEngine);
        femEngine.setVisible(true);


    }
}
