package it.polimi.ingsw.PSP32.client;

import it.polimi.ingsw.PSP32.model.Message;

public class ClientHeartbeat implements Runnable {

    final private ServerAdapter serverAdapter;
    final private ServerAdapterGui serverAdapterGui;

    public ClientHeartbeat(ServerAdapter serverAdapter)
    {
        this.serverAdapter=serverAdapter;
        this.serverAdapterGui=null;
    }
    public ClientHeartbeat(ServerAdapterGui serverAdapterGui)
    {
        this.serverAdapterGui=serverAdapterGui;
        this.serverAdapter=null;
    }

    @Override
    public void run() {
        Message heartbeatMessage = new Message(null, null,"Heartbeat", null);

        while(true)
        {
            try{
                if(serverAdapterGui==null) { serverAdapter.requestSendObject(heartbeatMessage); }
                else {serverAdapterGui.requestSendObject(heartbeatMessage); }
                Thread.sleep(10000);
            }
            catch(InterruptedException e){
            }
        }
    }
}

