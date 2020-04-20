package it.polimi.ingsw.PSP32.client;

import java.io.Serializable;
import java.util.ArrayList;

public class Message implements Serializable {

  private String methodName;
  private ArrayList<Object> parameters;


  public String getMethodName() {
    return methodName;
  }


  public ArrayList<Object> getParameters() {
    return parameters;
  }

  public Message(String methodName, ArrayList<Object> parameters){
    this.methodName = methodName;
    this.parameters = parameters;
  }
  public Message(String methodName){
    this.methodName = methodName;
    this.parameters = null;
  }

}
