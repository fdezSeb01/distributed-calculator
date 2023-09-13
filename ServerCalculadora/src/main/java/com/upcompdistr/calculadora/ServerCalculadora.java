package com.upcompdistr.calculadora;

public class ServerCalculadora {

    public static void main(String[] args) {
        AdditionServer.start();
        SubstractionServer.start();
        MultiplicationServer.start();
        DivisionServer.start();
        while(true);
    }
}
