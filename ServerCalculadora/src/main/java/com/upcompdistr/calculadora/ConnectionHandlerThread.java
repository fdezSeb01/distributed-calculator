package com.upcompdistr.calculadora;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.upcompdistr.calculadora.Models.MsgStruct;

public class ConnectionHandlerThread extends Thread {
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private String type;

    public ConnectionHandlerThread(String type) {
        // Constructor can be empty or used to initialize any other variables.
        this.type=type;
    }

    @Override
    public void run() {
        final String SERVER_ADDRESS = "localhost";
        final int SERVER_PORT = 1234;

        try {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            out.writeObject("SERVER"+type);
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
            System.out.println("Connected "+type+" server to MOM on port: " + SERVER_PORT);
        } catch (IOException e) {
            //e.printStackTrace();
            System.out.println("Couldn't connect to MOM on port: " + SERVER_PORT);
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
                
                switch (type) { // Assuming you have a getType() method in MsgStruct to get the type
                    case "+":
                        AdditionServer.add_and_send(msg.getMessage().getOps());
                        break;
                    case "-":
                        SubstractionServer.sub_and_send(msg.getMessage().getOps());
                        break;
                    case "*":
                        MultiplicationServer.mult_and_send(msg.getMessage().getOps());
                        break;
                    case "/":
                        DivisionServer.div_and_send(msg.getMessage().getOps());
                        break;
                    default:
                        System.out.println("Unexpected type");
                        break;
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
