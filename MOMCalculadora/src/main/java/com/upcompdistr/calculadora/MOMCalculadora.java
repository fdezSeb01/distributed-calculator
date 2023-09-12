package com.upcompdistr.calculadora;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.upcompdistr.calculadora.Models.Streams;

public class MOMCalculadora {
    private static final int PORT = 1234;
    public static Map<String, Streams> connectedClients = new HashMap<>();
    public static Map<Short, Streams> availableServers = new HashMap<>();

    public static void main(String[] args){
        ServerSocket serverSocket = null; // Initialize serverSocket outside the try-catch block.

        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Server started on port " + PORT);

            while (true) {
                Socket socket = serverSocket.accept();
                String id = UUID.randomUUID().toString();
                Streams streams = new Streams(new ObjectInputStream(socket.getInputStream()), new ObjectOutputStream(socket.getOutputStream()));

                try {
                    String type = (String) streams.getIn().readObject();
                    
                    if ("CLIENT".equals(type)) {
                        connectedClients.put(id, streams);
                        ClientHandler handlerThread = new ClientHandler(socket, id, streams);
                        handlerThread.start();
                    } else if ("SERVER+".equals(type)) {
                        if (availableServers.get((short)1) == null){
                            availableServers.put((short)1, streams);
                            ServerHandler handlerThread = new ServerHandler(socket, id, streams);
                            handlerThread.start();
                        }
                    } else if ("SERVER-".equals(type)) {
                        if (availableServers.get((short)2) == null){
                            availableServers.put((short)2, streams);
                            ServerHandler handlerThread = new ServerHandler(socket, id, streams);
                            handlerThread.start();
                        }
                    } else if ("SERVER*".equals(type)) {
                        if (availableServers.get((short)3) == null){
                            availableServers.put((short)3, streams);
                            ServerHandler handlerThread = new ServerHandler(socket, id, streams);
                            handlerThread.start();
                        }
                    } else if ("SERVER/".equals(type)) {
                        if (availableServers.get((short)4) == null){
                            availableServers.put((short)4, streams);
                            ServerHandler handlerThread = new ServerHandler(socket, id, streams);
                            handlerThread.start();
                        }
                    } else {
                        System.out.println("Unknown connection type");
                        socket.close();
                    }
                } catch (ClassNotFoundException e) {
                    System.err.println("Error reading object from input stream.");
                    socket.close();
                }
            }
        } catch (IOException e) {
            System.err.println("Can't initialize MOM on port " + PORT);
        } finally {
            if (serverSocket != null && !serverSocket.isClosed()) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    System.err.println("Error while closing MOM socket.");
                }
            }
            System.out.println("MOM shut down");
        }
    }

}
