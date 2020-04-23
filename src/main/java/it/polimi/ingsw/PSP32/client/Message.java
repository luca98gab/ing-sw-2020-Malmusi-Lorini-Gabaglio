package it.polimi.ingsw.PSP32.client;

import java.io.Serializable;
import java.util.ArrayList;

public class Message implements Serializable {

  private String typeOfMessage;
  private String methodName;
  private ArrayList<Object> parameters;
  private Object result;


  public String getMethodName() {
    return methodName;
  }

  public String getTypeOfMessage() {
    return typeOfMessage;
  }

  public Object getResult() {
    return result;
  }

  public void setResult(Object result) {
    this.result = result;
  }

  public ArrayList<Object> getParameters() {
    return parameters;
  }

  public Message(String methodName, ArrayList<Object> parameters, String typeOfMessage, Object result){
    this.methodName = methodName;
    this.parameters = parameters;
    this.typeOfMessage = typeOfMessage;
    this.result = result;
  }
  public Message(String methodName){
    this.methodName = methodName;
    this.parameters = null;
  }

}
