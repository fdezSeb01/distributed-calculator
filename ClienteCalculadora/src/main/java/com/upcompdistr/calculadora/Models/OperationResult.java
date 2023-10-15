package com.upcompdistr.calculadora.Models;

import java.io.Serializable;

public class OperationResult implements Serializable {
    private boolean solved;
    private String log;
    private double res;
    public String getLog() {
        return log;
    }
    public double getRes() {
        return res;
    }
    public boolean isSolved() {
        return solved;
    }  
    public OperationResult(boolean solved, String log, double res) {
        this.solved = solved;
        this.log = log;
        this.res = res;
    }
    public OperationResult(boolean solved) {
        this.solved = solved;
        this.log = "Serveridor no capaz de hacer esta operación";
        this.res = 0;
    }
    @Override
    public String toString() {
        return "OperationResult [log=" + log + ", res=" + res + "]";
    }   
}
