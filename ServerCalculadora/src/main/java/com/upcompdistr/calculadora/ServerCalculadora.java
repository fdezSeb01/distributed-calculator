package com.upcompdistr.calculadora;

public class ServerCalculadora {

    public static void main(String[] args) {
        //cada MOM se levanta junto con uno de estos
        // Cada servidor es independiente
        // Cuando hay una operacion todos deben responder (si o no pueden realizar, y si si, el resultado)
        
        ConnectionHandlerThread.port = Integer.parseInt(args[0]);
        AdditionServer.start();
        SubstractionServer.start();
        MultiplicationServer.start();
        DivisionServer.start();
        while(true);
    }
}
