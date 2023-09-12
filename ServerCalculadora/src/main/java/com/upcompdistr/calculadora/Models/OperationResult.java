package com.upcompdistr.calculadora.Models;

import java.io.Serializable;

public class OperationResult implements Serializable {
    private String log;
    private double res;
    public OperationResult(String msg, double res) {
        this.log = msg + res;
        this.res = res;
    }
    public String getLog() {
        return log;
    }
    public double getRes() {
        return res;
    }
    @Override
    public String toString() {
        return "OperationResult [log=" + log + ", res=" + res + "]";
    }   
     
}
