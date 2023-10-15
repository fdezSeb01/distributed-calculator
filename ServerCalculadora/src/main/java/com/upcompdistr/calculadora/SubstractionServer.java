package com.upcompdistr.calculadora;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

import com.upcompdistr.calculadora.Models.*;

public class SubstractionServer {

    private static ObjectOutputStream out;

    public static void start(){
        ConnectionHandlerThread handler = new ConnectionHandlerThread("-");
        handler.setDaemon(true);
        handler.start();
        isUpConfirmation();
    }

    public static void isUpConfirmation(){
        System.out.println("Substraction server is up and running");
    }
    public static void sub_and_send(List<Short> nums) throws IOException{
        double res=(double)nums.get(0);
        nums.remove(0);
        String txt=res+"-";
        for(short n : nums){
            res-=n;
            txt += n + "-";
        }
        txt = txt.substring(0, txt.length()-1);
        if(out != null){
            OperationResult opRes = new OperationResult(true,txt+"=", res);
            out.writeObject(opRes);
        }
    }

    public static void setOut(ObjectOutputStream Out){
        out=Out;
    }
}
