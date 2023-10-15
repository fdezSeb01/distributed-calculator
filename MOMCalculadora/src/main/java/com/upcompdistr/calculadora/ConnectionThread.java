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
        MOM_connection mc;
        while (true) {
            try {
                mc = (MOM_connection) in.readObject();
                if(mc.isConnected())
                    System.out.println("Connected to other MOM on port " +mc.getPort());
                else
                    System.out.println("Couldn't connect to other MOM on port " +mc.getPort());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                return;
            } catch (IOException e) {
                //e.printStackTrace();
                return;
            }
        }
    }
}

