package com.upcompdistr.calculadora;

public class ServerCalculadora {

    public static void main(String[] args) {
        System.out.println("Hello World!");
        AdditionServer.start();
        SubstractionServer.start();
        MultiplicationServer.start();
        DivisionServer.start();
        while(true);
    }
}
