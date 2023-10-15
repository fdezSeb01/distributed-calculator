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
            //System.out.println("Other MOM connected: " + id);
            MOM_connection mc;

            while (true) {
                try {
                    mc = (MOM_connection) in.readObject();
                    final MOM_connection output = mc;
                    System.out.println("Received from client " + id + " a message: " + mc.toString());  
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
