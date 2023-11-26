package com.upcompdistr.calculadora;

import java.io.IOException;
import java.io.ObjectOutputStream;

import com.upcompdistr.calculadora.Models.Message;
import com.upcompdistr.calculadora.Models.MsgStruct;
import com.upcompdistr.calculadora.Models.OperationResult;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class PrimaryController {
    private static final short NONE = 32767;
    private static short op=NONE;
    private static PrimaryController instance;
    private static String ops_commas="";
    private static String tempNum="";

    public static QueueHandlerThread queueHandlerThread;

    static{
        queueHandlerThread = new QueueHandlerThread();
        queueHandlerThread.setDaemon(true);
        queueHandlerThread.start();
    }

    @FXML
    private void initialize(){
        instance=this;
    }

    static {op =NONE;}

    @FXML
    private TextField resultado;

    @FXML
    private TextArea logsTextArea;

    @FXML
    private Button btn1;

    @FXML
    private Button btn2;

    @FXML
    private Button btn3;

    @FXML
    private Button btn4;

    @FXML
    private Button btn5;

    @FXML
    private Button btn6;

    @FXML
    private Button btn7;

    @FXML
    private Button btn8;

    @FXML
    private Button btn9;

    @FXML
    private Button btn0;

    @FXML
    private Button btnDot;

    @FXML
    private Button btnEquals;

    @FXML
    private Button btnPlus;

    @FXML
    private Button btnMinus;

    @FXML
    private Button btnMultiply;

    @FXML
    private Button btnDivide;

    private void setOperand(short n){
        if(op==NONE && tempNum.isEmpty()){
            resultado.setText("");
        }
        tempNum+=n;
        resultado.setText(resultado.getText()+n);
    }

    private void setOp(short n, String symbol){
        if(tempNum.isEmpty()){
            System.out.println("Operand missing");
            return;
        }
        if(op==NONE){
            op = n;
        } else if (n != op){
            System.out.println("Only one operand allowed");
            return;
        }
        if(resultado.getText().isEmpty()){
            System.out.println("Operand missing");
            return;
        }
        ops_commas+=tempNum+",";
        tempNum="";
        resultado.setText(resultado.getText()+symbol);
    }

    @FXML
    private void handleBtn1Click() {
        setOperand((short)1);
    }
    
    @FXML
    private void handleBtn2Click() {
        setOperand((short)2);
    }
    
    @FXML
    private void handleBtn3Click() {
        setOperand((short)3);
    }
    
    @FXML
    private void handleBtn4Click() {
        setOperand((short)4);
    }
    
    @FXML
    private void handleBtn5Click() {
        setOperand((short)5);
    }
    
    @FXML
    private void handleBtn6Click() {
        setOperand((short)6);
    }
    
    @FXML
    private void handleBtn7Click() {
        setOperand((short)7);
    }
    
    @FXML
    private void handleBtn8Click() {
        setOperand((short)8);
    }
    
    @FXML
    private void handleBtn9Click() {
        setOperand((short)9);
    }
    
    @FXML
    private void handleBtn0Click() {
        setOperand((short)0);
    }
    
    @FXML
    private void handleBtnDotClick() {
        //resultado.setText(resultado.getText()+".");
        resultado.setText("");
        tempNum="";
        op=NONE;
        ops_commas="";
    }
    
    @FXML
    private void handleBtnEqualsClick() throws IOException {
        //send operation to MOM (later)
        if(resultado.getText().isEmpty()){
            System.out.println("Missing operands or operation");
            return;
        }
        ops_commas+=tempNum;
        MsgStruct msg = new MsgStruct(op,new Message(ops_commas.split(",")));
        sendMessage2Server(msg);
        resultado.setText("");
        op=NONE;
        tempNum="";
        ops_commas="";
    }

    private void sendMessage2Server(MsgStruct msg) throws IOException {
        queueHandlerThread.addMsgToQueue(msg);
            //out.writeObject(msg);
            //out.flush();
    }

    
    @FXML
    private void handleBtnPlusClick() {
        setOp((short)1,"+");
    }
    
    @FXML
    private void handleBtnMinusClick() {
        setOp((short)2,"-");
    }
    
    @FXML
    private void handleBtnMultiplyClick() {
        setOp((short)3,"*");
    }
    
    @FXML
    private void handleBtnDivideClick() {
        setOp((short)4,"/");
    }

    public static void setOut(ObjectOutputStream o){
        queueHandlerThread.setOut(o);
    }

    public static void GotResult(OperationResult opRes){
        if(instance != null){
            javafx.application.Platform.runLater(() -> instance.printResult(opRes.getLog(),opRes.getRes()));
        }
    }

    private void printResult(String log, double res){
        resultado.setText(String.valueOf(res));
        logsTextArea.appendText(log+res+"\n");
    }
}
