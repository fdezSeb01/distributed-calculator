package com.upcompdistr.calculadora.Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Message implements Serializable {
    private short size;
    private List<Short> ops;

    public Message(String[] ops_arr) {
        this.size = (short)ops_arr.length;
        this.ops = new ArrayList<>();
        for(String op : ops_arr){
            this.ops.add(Short.parseShort(op));
        }
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