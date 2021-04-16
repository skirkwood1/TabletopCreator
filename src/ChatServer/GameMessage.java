package ChatServer;

import Models.Game;

import java.io.Serializable;
import java.util.ArrayList;

public class GameMessage implements Serializable,Message {

    private static final long serialVersionUID = -1362608971386820237L;

    private Game game;
    private String message;
    private ArrayList<String> clients;

    public GameMessage(){
        this.game = null;
        this.message = "";
        this.clients = new ArrayList<>();
    }

    public GameMessage(String message,ArrayList<ServerThread> clients){
        this.game = null;
        this.message = message;
        this.clients = new ArrayList<>();
        for(ServerThread client: clients){
            this.clients.add(client.getUsername());
        }
    }

    public void setGame(Game game){
        this.game = game;
    }

    public void setMessage(String message){
        this.message = message;
    }

    public String getMessage(){
        return this.message;
    }

    public Game getGame(){
        return this.game;
    }

    public ArrayList<String> getClients(){
        return this.clients;
    }
}