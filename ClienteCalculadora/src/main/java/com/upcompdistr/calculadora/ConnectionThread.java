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
        int[] ports = get_port_list();
        boolean connection_succesful = true;
        Random rnd = new Random();
        int i=rnd.nextInt(ports.length); // se conecta a uno de los puertos disponibles
        do{
            i = (i%ports.length); //mantiene el inice valido
            int port = ports[i]; //elige el puerto del indice elegido
            try {
                socket = new Socket(SERVER_ADDRESS, port);
                out = new ObjectOutputStream(socket.getOutputStream());
                in = new ObjectInputStream(socket.getInputStream());
                out.writeObject("CLIENT");
                PrimaryController.setOut(out); //pass out to controllers that need it

                Thread listenerThread = new Thread(() -> {
                    listener();
                });
                listenerThread.setDaemon(true);
                listenerThread.start();
                System.out.println("Connected client to MOM on port: " + port);
            } catch (IOException e) {
                //e.printStackTrace();
                connection_succesful = false;
                i++; //si hay error al conectar se pasa a conectar al siguiente puerto disponible
                System.out.println("Couldn't connect to MOM on port: " + port);
            }
        }while(!connection_succesful);
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

    private int[] get_port_list(){
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
        int[] ports = new int[portList.size()];
        for (int i = 0; i < portList.size(); i++) {
            ports[i] = portList.get(i);
        }

        return ports;
    }
}

