package com.upcompdistr.calculadora;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

import com.upcompdistr.calculadora.Models.*;

public class MultiplicationServer {

    private static ObjectOutputStream out;

    public static void start(){
        ConnectionHandlerThread handler = new ConnectionHandlerThread("*");
        handler.setDaemon(true);
        handler.start();
        isUpConfirmation();
    }

    public static void isUpConfirmation(){
        System.out.println("Multiplication server is up and running");
    }
    public static void mult_and_send(List<Short> nums) throws IOException{
        double res=1;
        String mult="";
        for(short n : nums){
            res*=n;
            mult += n + "*";
        }
        mult = mult.substring(0, mult.length()-1);
        if(out != null){
            OperationResult opRes = new OperationResult(mult+"=", res);
            out.writeObject(opRes);
        }
    }

    public static void setOut(ObjectOutputStream Out){
        out=Out;
    }
}
