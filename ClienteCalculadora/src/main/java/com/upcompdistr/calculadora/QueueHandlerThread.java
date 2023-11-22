package com.upcompdistr.calculadora;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.Queue;

import com.upcompdistr.calculadora.Models.MsgStruct;

public class QueueHandlerThread extends Thread {
    public final int min_acuses = 1;
    private int acuses = 1;
    ObjectOutputStream out;

    private Queue<MsgStruct> additionQueue = new LinkedList<>();
    private Queue<MsgStruct> substractionQueue = new LinkedList<>();
    private Queue<MsgStruct> multiplicationQueue = new LinkedList<>();
    private Queue<MsgStruct> divisionQueue = new LinkedList<>();

    public void addMsgToQueue(MsgStruct msg) {
        switch (msg.getType()) {
            case 1:
                additionQueue.add(msg);
                break;
            case 2:
                substractionQueue.add(msg);
                break;
            case 3:
                multiplicationQueue.add(msg);
                break;
            case 4:
                divisionQueue.add(msg);
                break;
            default:
                System.out.println("Invalid message type");
                break;
        }
        System.out.println("Message queued"+msg.toString());
        checkAndSendPendingMessages();

    }
    public void setOut(ObjectOutputStream out) {
        this.out = out;
    }

    public void checkAndSendPendingMessages() {
        if (acuses >= min_acuses) {
            if (!additionQueue.isEmpty() && acuses >= min_acuses) {
                MsgStruct msg_2_send = additionQueue.poll();
                System.out.println("Message dequeued"+msg_2_send.toString());
                try {
                    out.writeObject(msg_2_send);
                } catch (IOException e) {
                    System.out.println("Can't send message to MOM.");
                    App.create_connection_thread(); //create another connection
                    try {
                        Thread.sleep(1000); // Sleep for one second (1000 milliseconds)
                        out.writeObject(msg_2_send);
                    } catch (InterruptedException err) {
                        System.err.println("Thread interrupted while sleeping");
                    } catch (IOException err) {
                        System.out.println("No se pudo enviar el mensaje tras resetear conexión");
                    }
                }
                acuses--;
            }
            if (!substractionQueue.isEmpty() && acuses >= min_acuses) {
                MsgStruct msg_2_send = substractionQueue.poll();
                System.out.println("Message dequeued"+msg_2_send.toString());
                try {
                    out.writeObject(msg_2_send);
                } catch (IOException e) {
                    System.out.println("Can't send message to MOM.");
                    App.create_connection_thread(); //create another connection
                    try {
                        Thread.sleep(1000); // Sleep for one second (1000 milliseconds)
                        out.writeObject(msg_2_send);
                    } catch (InterruptedException err) {
                        System.err.println("Thread interrupted while sleeping");
                    } catch (IOException err) {
                        System.out.println("No se pudo enviar el mensaje tras resetear conexión");
                    }
                }
                acuses--;
            }
            if (!multiplicationQueue.isEmpty() && acuses >= min_acuses) {
                MsgStruct msg_2_send = multiplicationQueue.poll();
                System.out.println("Message dequeued"+msg_2_send.toString());
                try {
                    out.writeObject(msg_2_send);
                } catch (IOException e) {
                    System.out.println("Can't send message to MOM.");
                    App.create_connection_thread(); //create another connection
                    try {
                        Thread.sleep(1000); // Sleep for one second (1000 milliseconds)
                        out.writeObject(msg_2_send);
                    } catch (InterruptedException err) {
                        System.err.println("Thread interrupted while sleeping");
                    } catch (IOException err) {
                        System.out.println("No se pudo enviar el mensaje tras resetear conexión");
                    }
                }
                acuses--;
            }
            if (!divisionQueue.isEmpty() && acuses >= min_acuses) {
                MsgStruct msg_2_send = divisionQueue.poll();
                System.out.println("Message dequeued"+msg_2_send.toString());
                try {
                    out.writeObject(msg_2_send);
                } catch (IOException e) {
                    System.out.println("Can't send message to MOM.");
                    App.create_connection_thread(); //create another connection
                    try {
                        Thread.sleep(1000); // Sleep for one second (1000 milliseconds)
                        out.writeObject(msg_2_send);
                    } catch (InterruptedException err) {
                        System.err.println("Thread interrupted while sleeping");
                    } catch (IOException err) {
                        System.out.println("No se pudo enviar el mensaje tras resetear conexión");
                    }
                }
                acuses--;
            }
        } else {
            System.out.println("Not enough acuses to send messages");
        }
    }

    public void increaseAcuses() {
        acuses++;
        checkAndSendPendingMessages();
    }


}
