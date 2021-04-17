package ChatServer.Messages;

import ChatServer.Messages.Message;
import ChatServer.ServerThread;

import java.io.Serializable;
import java.util.ArrayList;

import static ChatServer.Messages.Message.MessageType.QUIT;

public class QuitMessage implements Message, Serializable {
    private MessageType type = QUIT;

    private String message;

    private ArrayList<String> clients = new ArrayList<>();

    public String getMessage(){
        return this.message;
    }

    public MessageType getType(){
        return this.type;
    }

    public void setMessage(String msg){
        this.message = msg + " left the server.";
    }

    public void setClients(ArrayList<ServerThread> clients){
        for(ServerThread client:clients){
            this.clients.add(client.getUsername());
        }
    }

    public ArrayList<String> getClients(){
        return this.clients;
    }
}