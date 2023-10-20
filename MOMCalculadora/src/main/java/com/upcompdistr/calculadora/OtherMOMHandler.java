package com.upcompdistr.calculadora;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.upcompdistr.calculadora.Models.*;

public class OtherMOMHandler extends Thread {
    private Socket socket;
    private String id;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public OtherMOMHandler(Socket socket, String id, Streams streams){
        this.socket=socket;
        this.id=id;
        this.in=streams.getIn();
        this.out=streams.getOut();
    }

    @Override
    public void run() {
        try{
            System.out.println("Another MOM connected: " + id);
            OperationResult opRes;

            while (true) {
                try {
                    opRes = (OperationResult) in.readObject();
                    final OperationResult output = opRes;
                    if (!opRes.isSolved()){
                        System.out.println(opRes.getLog());
                    } else{
                        System.out.println("Received from server " + id + " a message: " + opRes.toString());
                        MOMCalculadora.connectedClients.forEach((key, outExt) -> {
                                try {
                                    sendMessage2Server(output, outExt.getOut());
                                } catch (IOException e) {
                                    System.out.println("Error sending message to server " + id);
                                }
                        });
                    }
                } catch (ClassNotFoundException e) {
                    System.out.println("Can't deserialize input into MsgStruct");
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            //System.out.println("Problem handling another MOM " + id);
        } finally {
            try {
                if (!socket.isClosed() && socket != null) {
                    socket.close();
                    MOMCalculadora.connected_moms.remove(id);
                    System.out.println("A MOM disconnected with id " + id);
                }
            } catch (IOException e) {
                System.out.println("Problem another MOM socket with id " + id);
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
