package com.upcompdistr.calculadora.Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Message implements Serializable {
    private short size;
    private List<Short> ops;

    public Message(short size, short op1, short op2) {
        this.size = size;
        this.ops = new ArrayList<>();
        this.ops.add(op1);
        this.ops.add(op2);
    }

    public short getSize() {
        return size;
    }

    public List<Short> getOps() {
        return ops;
    }

    @Override
    public String toString() {
        return "Message [size=" + size + ", ops=" + ops + "]";
    } 
}

