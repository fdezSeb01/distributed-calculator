package com.upcompdistr.calculadora;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.upcompdistr.calculadora.Models.*;

public class ClientHandler extends Thread {
    private Socket socket;
    private String id;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public ClientHandler(Socket socket, String id, Streams streams){
        this.socket=socket;
        this.id=id;
        this.in=streams.getIn();
        this.out=streams.getOut();
    }

    @Override
    public void run() {
        try{
            System.out.println("Client connected: " + id);
            MsgStruct msg;

            while (true) {
                try {
                    msg = (MsgStruct) in.readObject();
                    final MsgStruct output = msg;
                    System.out.println("Received from client " + id + " a message: " + msg.toString());
                    MOMCalculadora.queueHandlerThread.addToQueue(output,out);
                    //out.writeObject(new OperationResult()); //mandar acuse de recibido
                    //for (Streams str : MOMCalculadora.availableServers.values()){
                    //    ObjectOutputStream outExt = str.getOut();
                    //    sendMessage2Server(output,outExt);
                    //}   
                } catch (ClassNotFoundException e) {
                    System.out.println("Can't deserialize input into MsgStruct");
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            //System.out.println("Problem handling client " + id);
        } finally {
            try {
                if (!socket.isClosed() && socket != null) {
                    socket.close();
                    MOMCalculadora.connectedClients.remove(id);
                    System.out.println("Client disconnected: " + id);
                }
            } catch (IOException e) {
                System.out.println("Problem closing socket with id " + id);
            }
        }
    }
    // private void sendMessage2Server(MsgStruct msg, ObjectOutputStream out) throws IOException {
    //     try {

    //         out.writeObject(msg);
    //         //out.flush();
    //     } catch (IOException e) {
    //         System.out.println("Can't send message to server.");
    //     }
    // }
}
