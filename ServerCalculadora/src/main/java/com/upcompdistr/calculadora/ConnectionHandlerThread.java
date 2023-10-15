package com.upcompdistr.calculadora;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.upcompdistr.calculadora.Models.MsgStruct;
import com.upcompdistr.calculadora.Models.OperationResult;

public class ConnectionHandlerThread extends Thread {
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private String type;
    private Short type_sh;
    public static int port;

    public ConnectionHandlerThread(String type) {
        // Constructor can be empty or used to initialize any other variables.
        this.type=type;
                switch (type) { // Assuming you have a getType() method in MsgStruct to get the type
                    case "+":
                        this.type_sh=1;
                        break;
                    case "-":
                        this.type_sh=2;
                        break;
                    case "*":
                        this.type_sh=3;
                        break;
                    case "/":
                        this.type_sh=4;
                        break;
                    default:
                        this.type_sh=0;
                        break;
                }
    }

    @Override
    public void run() {
        final String SERVER_ADDRESS = "localhost";

        try {
            socket = new Socket(SERVER_ADDRESS, port);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            out.writeObject("SERVER");
            //pass out to whichever class is gonna use
                switch (type) { // Assuming you have a getType() method in MsgStruct to get the type
                    case "+":
                        AdditionServer.setOut(out);
                        break;
                    case "-":
                        SubstractionServer.setOut(out);
                        break;
                    case "*":
                        MultiplicationServer.setOut(out);
                        break;
                    case "/":
                        DivisionServer.setOut(out);
                        break;
                    default:
                        System.out.println("Unexpected type");
                        break;
                }
            Thread listenerThread = new Thread(() -> {
                listener();
            });
            listenerThread.setDaemon(true);
            listenerThread.start();
            System.out.println("Connected "+type+" server to MOM on port: " + port);
        } catch (IOException e) {
            //e.printStackTrace();
            System.out.println("Couldn't connect to MOM on port: " + port);
        }
    }

    public void disconnect() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
                System.out.println("Socket closed.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void listener() {
        MsgStruct msg;
        boolean shouldExit = false; // Add a flag to control the loop

        while (!shouldExit) { // Use the flag as the loop condition
            try {
                msg = (MsgStruct) in.readObject(); 
                System.out.println("Se recibio para operar: " + msg.toString());
                if(type_sh != msg.getType()){
                    OperationResult or = new OperationResult(false);
                    out.writeObject(or);
                } else{
                    switch (msg.getType()) { // Assuming you have a getType() method in MsgStruct to get the type
                        case 1:
                            AdditionServer.add_and_send(msg.getMessage().getOps());
                            break;
                        case 2:
                            SubstractionServer.sub_and_send(msg.getMessage().getOps());
                            break;
                        case 3:
                            MultiplicationServer.mult_and_send(msg.getMessage().getOps());
                            break;
                        case 4:
                            DivisionServer.div_and_send(msg.getMessage().getOps());
                            break;
                        default:
                            System.out.println("Unexpected type");
                            break;
                    }
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                shouldExit = true; // Set the flag to true to exit the loop on exception
            } catch (IOException e) {
                e.printStackTrace();
                shouldExit = true; // Set the flag to true to exit the loop on exception
            }
        }
    }

}
