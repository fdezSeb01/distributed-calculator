package com.upcompdistr.calculadora.Models;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Streams{
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public Streams(ObjectInputStream in, ObjectOutputStream out){
        this.in=in;
        this.out=out;
    }

    public ObjectInputStream getIn() {
        return in;
    }

    public ObjectOutputStream getOut() {
        return out;
    }
}