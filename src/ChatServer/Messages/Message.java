package ChatServer.Messages;

import ChatServer.ServerThread;

import java.net.Socket;
import java.util.ArrayList;

public interface Message {
    String getMessage();
    void setMessage(String msg);

    enum MessageType {QUIT,GAME,SERVERLEAVE,SERVERJOIN}

    MessageType getType();
    ArrayList<String> getClients();
    void setClients(ArrayList<ServerThread> clients);
}
