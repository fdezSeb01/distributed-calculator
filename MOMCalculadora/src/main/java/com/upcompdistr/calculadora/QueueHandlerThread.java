package com.upcompdistr.calculadora;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.Queue;

import com.upcompdistr.calculadora.Models.MsgStruct;
import com.upcompdistr.calculadora.Models.OperationResult;
import com.upcompdistr.calculadora.Models.Streams;

public class QueueHandlerThread extends Thread{
    private Queue<QueuedMessage> incomingMsgsQueue = new LinkedList<>();

    public class QueuedMessage {
        private ObjectOutputStream objectOutputStream;
        private Object object;
        
        public QueuedMessage(ObjectOutputStream objectOutputStream, Object object) {
            this.objectOutputStream = objectOutputStream;
            this.object = object;
        }
        
        public ObjectOutputStream getObjectOutputStream() {
            return objectOutputStream;
        }
        
        public void setObjectOutputStream(ObjectOutputStream objectOutputStream) {
            this.objectOutputStream = objectOutputStream;
        }
        
        public Object getObject() {
            return object;
        }
        
        public void setObject(Object object) {
            this.object = object;
        }
    }

    public void addToQueue(Object obj, ObjectOutputStream out) {
        incomingMsgsQueue.add(new QueuedMessage(out, obj));
        if (obj instanceof MsgStruct) {
            try {
                out.writeObject(new OperationResult()); //mandar acuse de recibido
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        handleQueueMessages();
    }

    public void handleQueueMessages() {
        while (!incomingMsgsQueue.isEmpty()) {
            QueuedMessage message = incomingMsgsQueue.poll();
            Object obj = message.getObject();
            if (obj instanceof MsgStruct) {
                // Handle MsgStruct message
                for (Streams str : MOMCalculadora.availableServers.values()){
                    ObjectOutputStream outExt = str.getOut();
                    sendMessage2Server((MsgStruct)obj,outExt);
                }  
            } else if (obj instanceof OperationResult) {
                // Handle OperationResult message
                MOMCalculadora.connectedClients.forEach((key, outExt) -> {
                    try {
                        sendMessage2Server((OperationResult)obj, outExt.getOut());
                    } catch (IOException e) {
                        System.out.println("Error sending message to server " + key);
                    }
                });
                MOMCalculadora.connected_moms.forEach((key, outExt) -> { //mandar a otros MOM's
                    try {
                        sendMessage2Server((OperationResult)obj, outExt.getOut());
                    } catch (IOException e) {
                        System.out.println("Error sending message to mom " + key);
                    }
                });
            }
        }
    }

    private void sendMessage2Server(MsgStruct msg, ObjectOutputStream out) {
        try {

            out.writeObject(msg);
            //out.flush();
        } catch (IOException e) {
            System.out.println("Can't send message to server.");
        }
    }

    private void sendMessage2Server(OperationResult opRes, ObjectOutputStream out) throws IOException {
            out.writeObject(opRes);
            //out.flush();

    }
}
