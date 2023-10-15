package com.upcompdistr.calculadora;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import com.upcompdistr.calculadora.Models.Streams;

public class MOMCalculadora {
    public static Map<String, Streams> connectedClients = new HashMap<>();
    public static Map<String, Streams> availableServers = new HashMap<>();
    public static int port;
    public static void main(String[] args){
        ServerSocket serverSocket = null; // Initialize serverSocket outside the try-catch block.

        try {
            Random random = new Random();
            port = random.nextInt(9000) + 1000;
            add_port_to_file(port);
            serverSocket = new ServerSocket(port);
            System.out.println("Server started on port " + port);
            //Create 4 operation servers for this MOM
            String command = "java -jar /media/hdd/sebastianf/Documents/UP/Semestre7/ComputoDistribuido/Calculadora/JARS/servers.jar "+port;
            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.command("bash", "-c", command);
            try {
                Process process = processBuilder.start();
                //int exitCode = process.waitFor();
                //System.out.println("Command exited with code: " + exitCode);
            } catch (IOException e) {
                e.printStackTrace();
            }

            while (true) {
                Socket socket = serverSocket.accept();

                Streams streams = new Streams(new ObjectInputStream(socket.getInputStream()), new ObjectOutputStream(socket.getOutputStream()));

                try {
                    String type = (String) streams.getIn().readObject();
                    String id = UUID.randomUUID().toString();
                    
                    if ("CLIENT".equals(type)) {
                        connectedClients.put(id, streams);
                        ClientHandler handlerThread = new ClientHandler(socket, id, streams);
                        handlerThread.start();
                    } else if ("SERVER".equals(type)) {
                        availableServers.put(id, streams);
                        ServerHandler handlerThread = new ServerHandler(socket, id, streams);
                        handlerThread.start();
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
            System.err.println("Can't initialize MOM on port " + port);
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

    private static void add_port_to_file(int port){
        String filename = "/media/hdd/sebastianf/Documents/UP/Semestre7/ComputoDistribuido/Calculadora/JARS/ports.config"; // Replace with the actual file path

        try {
            // Create a FileWriter and BufferedWriter
            FileWriter fileWriter = new FileWriter(filename, true); // "true" for append mode
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            // Write the new port to the file
            bufferedWriter.write(Integer.toString(port));
            bufferedWriter.newLine(); // Add a new line for clarity

            // Close the writers to save the changes
            bufferedWriter.close();
            fileWriter.close();

            System.out.println("Port " + port + " added to " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
