package com.michalbarczyk.fem;

public class Init {

    public static void main(String[] args) {

        FEMEngine femEngine = new FEMEngine(2,1);

        femEngine.calculate();
    }
}
