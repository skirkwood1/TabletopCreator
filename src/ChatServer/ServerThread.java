package ChatServer;

import ChatServer.Messages.GameMessage;
import ChatServer.Messages.Message;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class ServerThread implements Runnable {

    public enum CommandType{
        WHISPER,JOIN,LEAVE,REGULAR
    }

    private Socket socket;
    private ArrayList<ServerThread> clients;
    private HashMap<Message.MessageType, MessageTypeExecutor> messageMap;
    private HashMap<String, CommandType> commandMap;

    private volatile ObjectOutputStream oos;
    private ObjectInputStream ois;

    private String username = "";

    public ServerThread(Socket socket, ArrayList<ServerThread> clients) {
        this.socket = socket;
        this.clients = clients;
        this.messageMap = new HashMap<>();
        this.commandMap = new HashMap<>();
        initMessages();
        initCommands();
    }

    public void run() {
        try {
            this.oos = new ObjectOutputStream(socket.getOutputStream());
            this.ois = new ObjectInputStream(socket.getInputStream());

            oos.writeObject(new GameMessage("Please enter a username:",this.clients));

            try{
                Message msg = (Message)ois.readObject();
                this.username = msg.getMessage();
            }catch(ClassNotFoundException ce){ }

            if(!username.equals("")){
                for(ServerThread client: this.clients){
                    if(!client.isClosed()){
                        try{
                            client.getObjectOutputStream().writeObject(
                                    new GameMessage(username + " joined the server.",this.clients));
                        }catch(SocketException se){
                            client.socket.close();
                        }
                    }
                }
            }

            while(!socket.isClosed()){
                try{
                    Message msg = (Message)ois.readObject();
                    messageMap.get(msg.getType()).execute(msg);

                }catch(ClassNotFoundException ce){
                    System.err.println("Class not found");
                }
            }

            socket.getOutputStream().close();
            socket.getInputStream().close();
            oos.close();
            ois.close();
            socket.close();
        } catch (IOException ex) {

            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public ObjectOutputStream getObjectOutputStream(){
        return this.oos;
    }

    public boolean isClosed(){
        return this.socket.isClosed();
    }

    public String getUsername(){return this.username;}

    public void initMessages(){
        messageMap.put(Message.MessageType.QUIT, msg -> {
            msg.setMessage(username);
            try{
                socket.close();
                clients.remove(this);
                msg.setClients(clients);
                GameMessage leaveMessage = new GameMessage(username + " left the server.",clients);
//                for(ServerThread client: clients){
//                    if(!client.isClosed()){
//                        client.getObjectOutputStream().writeObject(
//                                new GameMessage(username + " left the server.",clients));
//                    }
//                }
                parseMessage(leaveMessage);
            }catch(IOException i){
                i.printStackTrace();
            }
        });

        messageMap.put(Message.MessageType.GAME, msg -> {
            parseMessage(msg);
//            for(ServerThread client: clients){
//                if(!client.isClosed()){
//                    try{
//                        client.getObjectOutputStream().writeObject(msg);
//                    }catch(IOException ie){
//                        ie.printStackTrace();
//                    }
//                }
//            }
        });


    }

    public void initCommands(){
        commandMap.put("/w", CommandType.WHISPER);
        commandMap.put("/join", CommandType.JOIN);
        commandMap.put("/leave", CommandType.LEAVE);
    }

    public void parseMessage(Message msg){
        String text = msg.getMessage();
        String[] textSplit = text.split(" ");
        ArrayList<String> parts = new ArrayList<>();
        Collections.addAll(parts,textSplit);


        CommandType commandType = commandMap.get(parts.get(0));
        if(commandType == null){
            commandType = CommandType.REGULAR;
        }

        ArrayList<ServerThread> recipients = new ArrayList<>();

        System.out.println(commandType);

        switch(commandType){
            case WHISPER:
                recipients.add(this);
                String recipientName = parts.get(1);
                parts.remove(0);
                parts.remove(0);
                for(ServerThread client: this.clients){
                    if(client.getUsername().equals(recipientName)){
                        recipients.add(client);
                    }
                }

                StringBuffer sb = new StringBuffer();
                for(String s: parts){
                    sb.append(s);
                    sb.append(" ");
                }
                System.out.println(sb);
                msg.setMessage(sb.toString());
                break;
            case JOIN:
                break;
            case LEAVE:
                break;
            default:
                recipients = this.clients;
                break;
        }
        msg.setMessage(username + ": " + msg.getMessage());
        distributeMessage(recipients,msg);

    }

    public void distributeMessage(ArrayList<ServerThread> clients, Message message){
        for(ServerThread client: clients){
            if(!client.isClosed()){
                try{
                    client.getObjectOutputStream().writeObject(message);
                }catch(IOException ie){
                    ie.printStackTrace();
                }
            }
        }
    }

}