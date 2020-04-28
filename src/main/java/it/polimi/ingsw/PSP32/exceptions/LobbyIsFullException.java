package it.polimi.ingsw.PSP32.exceptions;

import it.polimi.ingsw.PSP32.client.ServerAdapter;

public class LobbyIsFullException extends Throwable {

        public LobbyIsFullException(ServerAdapter serverAdapter)

    {
        serverAdapter.stop();
        return;
    }
}
