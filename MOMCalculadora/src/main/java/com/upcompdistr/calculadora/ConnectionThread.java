package com.upcompdistr.calculadora;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.upcompdistr.calculadora.Models.MOM_connection;
import com.upcompdistr.calculadora.Models.OperationResult;

public class ConnectionThread extends Thread {
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private int port;

    public ConnectionThread(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        final String SERVER_ADDRESS = "localhost";
            try {
                socket = new Socket(SERVER_ADDRESS, port);
                out = new ObjectOutputStream(socket.getOutputStream());
                in = new ObjectInputStream(socket.getInputStream());
                out.writeObject("MOM_"+port);

                Thread listenerThread = new Thread(() -> {
                    listener();
                });
                listenerThread.setDaemon(true);
                listenerThread.start();
                System.out.println("Connected MOM to other MOM on port: " + port);
            } catch (IOException e) {
                //e.printStackTrace();
                System.out.println("Couldn't connect to other MOM on port: " + port);
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
        OperationResult opRes;
        while (true) {
            try {
                opRes = (OperationResult) in.readObject();
                final OperationResult output = opRes;
                if (!opRes.isSolved()){
                    System.out.println(opRes.getLog());
                } else{
                    System.out.println("Received from another MOM a message: " + opRes.toString());
                    MOMCalculadora.connectedClients.forEach((key, outExt) -> {
                            try {
                                sendMessage2Server(output, outExt.getOut());
                            } catch (IOException e) {
                                System.out.println("Error sending message to another mom");
                            }
                    });
                }
            } catch (ClassNotFoundException e) {
                System.out.println("Can't deserialize input into MsgStruct");
                e.printStackTrace();
            } catch (IOException e1) {
                //System.out.println("MOM disconnected");
                break;
            }
        }
    }
    private void sendMessage2Server(OperationResult opRes, ObjectOutputStream out) throws IOException {
        try {
            out.writeObject(opRes);
            //out.flush();
        } catch (IOException e) {
            System.out.println("Can't send message to MOM.");
        }
    }
}

