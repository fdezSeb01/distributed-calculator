package com.upcompdistr.calculadora;

public class QueueHandlerThread extends Thread{

    //TODO: aqui va a estar la cola de mesnajes recibidos por el MOM
    //pueden ser msjs de operacion o resultado
    //cada elemento de la cola ademas tiene el out al que se debe mandar ese mensjae para poder hacerlo
    //ademas del out de el cliente que mando el request para mandarle un acuse de recibido

    //TODO: tambien va a estar un metodo que acepta requests de añadir mensajes a la cola
    //y otro metodo que cuando hay un nueva adicion busca completar este mensaje ya sea mandandolo al cliente o server
    
}
