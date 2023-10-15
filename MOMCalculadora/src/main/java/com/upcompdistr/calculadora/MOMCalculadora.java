package com.upcompdistr.calculadora;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import com.upcompdistr.calculadora.Models.Streams;

public class MOMCalculadora {
    public static Map<String, Streams> connectedClients = new HashMap<>();
    public static Map<String, Streams> availableServers = new HashMap<>();
    public static int[] available_ports;
    public static Map<String, Streams> connected_moms = new HashMap<>();
    public static List<Integer> used_ports = new ArrayList<>();
    public static int port;
    public static void main(String[] args){
        ServerSocket serverSocket = null; // Initialize serverSocket outside the try-catch block.

        try {
            Random random = new Random();
            port = random.nextInt(9000) + 1000;

            add_port_to_file(port);
            used_ports.add(port);
            connect_to_available_ports();
            

            serverSocket = new ServerSocket(port);
            System.out.println("Server started on port " + port);

            //Create 4 operation servers for this MOM
            create_4_servers();

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
                    } else if ("MOM".equals(type.split("_")[0])) { //falta hacer que los MOM se conecten entre si
                        connected_moms.put(id, streams);
                        OtherMOMHandler handlerThread = new OtherMOMHandler(socket, id, streams);
                        handlerThread.start();
                        connect_to_available_ports();
                    }else {
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
            FileWriter fileWriter = new FileWriter(filename, true); // "true" for append mode
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            bufferedWriter.write(Integer.toString(port));
            bufferedWriter.newLine(); // Add a new line for clarity

            bufferedWriter.close();
            fileWriter.close();

            System.out.println("Port " + port + " added to ports.config");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void connect_to_available_ports(){
        List<Integer> portList = new ArrayList<>();
        String filename = "/media/hdd/sebastianf/Documents/UP/Semestre7/ComputoDistribuido/Calculadora/JARS/ports.config"; // Replace with the actual file path
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    int port = Integer.parseInt(line);
                    portList.add(port);
                } catch (NumberFormatException e) {
                    // Handle invalid lines (not integers) if necessary
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Convert the List<Integer> to an int[]
        available_ports = new int[portList.size()];
        for (int i = 0; i < portList.size(); i++) {
            available_ports[i] = portList.get(i);
            if (!used_ports.contains(available_ports[i])){
                create_connection_thread(available_ports[i]);
            }
            used_ports.add(available_ports[i]);
        }
    }

    private static void create_4_servers(){
            String command = "java -jar /media/hdd/sebastianf/Documents/UP/Semestre7/ComputoDistribuido/Calculadora/JARS/servers.jar "+port;
            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.command("bash", "-c", command);
            try {
                processBuilder.start();
                //int exitCode = process.waitFor();
                //System.out.println("Command exited with code: " + exitCode);
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    public static void create_connection_thread(int p){
        ConnectionThread socketThread = new ConnectionThread(p);
        socketThread.setDaemon(true); 
        socketThread.start();
    }

}
