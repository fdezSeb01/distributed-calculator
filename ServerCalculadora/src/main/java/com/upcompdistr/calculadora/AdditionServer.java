package com.upcompdistr.calculadora;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

import com.upcompdistr.calculadora.Models.*;

public class AdditionServer {

    private static ObjectOutputStream out;

    public static void start(){
        ConnectionHandlerThread handler = new ConnectionHandlerThread("+");
        handler.setDaemon(true);
        handler.start();
        isUpConfirmation();
    }

    public static void isUpConfirmation(){
        System.out.println("Addition server is up and running");
    }
    public static void add_and_send(List<Short> nums) throws IOException{
        double res=0;
        String sum="";
        for(short n : nums){
            res+=n;
            sum += n + "+";
        }
        sum = sum.substring(0, sum.length()-1);
        if(out != null){
            OperationResult opRes = new OperationResult(true,sum+"=", res);
            out.writeObject(opRes);
        }
    }

    public static void setOut(ObjectOutputStream Out){
        out=Out;
    }
}
