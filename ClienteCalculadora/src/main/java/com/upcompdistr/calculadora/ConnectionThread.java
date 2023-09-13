package com.upcompdistr.calculadora;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.upcompdistr.calculadora.Models.OperationResult;

public class ConnectionThread extends Thread {
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public ConnectionThread() {
        // Constructor can be empty or used to initialize any other variables.
    }

    @Override
    public void run() {
        final String SERVER_ADDRESS = "localhost";
        final int SERVER_PORT = 1234;

        try {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            out.writeObject("CLIENT");
            PrimaryController.setOut(out); //pass out to controllers that need it

            Thread listenerThread = new Thread(() -> {
                listener();
            });
            listenerThread.setDaemon(true);
            listenerThread.start();
            System.out.println("Connected client to MOM on port: " + SERVER_PORT);
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
        OperationResult opRes;
        while (true) {
            try {
                opRes = (OperationResult) in.readObject(); 
                System.out.println(opRes.toString());
                PrimaryController.GotResult(opRes);

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                return;
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }
}

