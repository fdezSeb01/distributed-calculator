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
    private static ObjectOutputStream out;
    private static short op,op1,op2;
    private static PrimaryController instance;

    @FXML
    private void initialize(){
        instance=this;
    }

    static {op = op1 = op2 = NONE;}

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
        if(op1 == NONE){

            op1 = n;
            resultado.setText(""+n);
            return;
        }
        if(op ==NONE){
            System.out.println("Operand Missing");
            return;
        }
        if (op2== NONE){
            op2=n;
            resultado.setText(resultado.getText()+n);
            return;
        }
        System.out.println("Only two operands per operation");
    }

    private void setOp(short n, String symbol){
        if(op1==NONE){
            System.out.println("First operand missing");
            return;
        }
        if(op == NONE){
            op=n;
            resultado.setText(resultado.getText()+symbol);
            return;
        }
        System.out.println("Only one operation allowed");
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
        op = op1 = op2 = NONE;

    }
    
    @FXML
    private void handleBtnEqualsClick() throws IOException {
        //send operation to MOM (later)
        if(op == NONE || op1==NONE || op2==NONE){
            System.out.println("Missing operands or operation");
            return;
        }
        MsgStruct msg = new MsgStruct(op,new Message((short)2, op1, op2));
        sendMessage2Server(msg);
        resultado.setText("");
        op = op1 = op2 = NONE;
    }

    private void sendMessage2Server(MsgStruct msg) throws IOException {
        try {
            out.writeObject(msg);
            //out.flush();
        } catch (IOException e) {
            System.out.println("Can't send message to server.");
        }
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
        out = o;
    }

    public static void GotResult(OperationResult opRes){
        if(instance != null){
            javafx.application.Platform.runLater(() -> instance.printResult(opRes.getLog(),opRes.getRes()));
        }
    }

    private void printResult(String log, double res){
        resultado.setText(String.valueOf(res));
        logsTextArea.appendText(log+"\n");
    }
}
