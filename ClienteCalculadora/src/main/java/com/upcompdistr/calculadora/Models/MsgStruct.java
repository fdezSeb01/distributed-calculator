package com.upcompdistr.calculadora.Models;

import java.io.Serializable;

public class MsgStruct implements Serializable{
    private short type; //1. add, 2. substract, 3.multiply, 4.divide
    private Message message;
    public MsgStruct(short type, Message message) {
        this.type = type;
        this.message = message;
    }
    public short getType() {
        return type;
    }
    public Message getMessage() {
        return message;
    }
    @Override
    public String toString() {
        return "MsgStruct [type=" + type + ", message=" + message.toString() + "]";
    }
}

