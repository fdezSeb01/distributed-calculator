package com.upcompdistr.calculadora.Models;

public class MOM_connection {
    private boolean connected;
    private int port;
    public MOM_connection(boolean connected, int port){
        this.connected=connected;
        this.port = port;
    }
    public boolean isConnected() {
        return connected;
    }
    public void setConnected(boolean connected) {
        this.connected = connected;
    }
    public int getPort() {
        return port;
    }
    public void setPort(int port) {
        this.port = port;
    }
    
}
