package ChatServer;

import ChatServer.Messages.GameMessage;
import ChatServer.Messages.Message;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;

public class ServerThread implements Runnable {

        private Socket socket;
        private ArrayList<ServerThread> clients;
        private ServerThread self;
        private HashMap<Message.MessageType, ChatCommand> commandMap;

        private volatile ObjectOutputStream oos;
        private ObjectInputStream ois;

        private String username = "";

        public ServerThread(Socket socket, ArrayList<ServerThread> clients) {
            this.socket = socket;
            this.clients = clients;
            this.commandMap = new HashMap<>();
            this.self = this;

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
                        commandMap.get(msg.getType()).execute(msg);

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

        public void initCommands(){
            commandMap.put(Message.MessageType.QUIT, msg -> {
                msg.setMessage(username);
                try{
                    socket.close();
                    clients.remove(this);
                    msg.setClients(clients);
                    for(ServerThread client: clients){
                        if(!client.isClosed()){
                            client.getObjectOutputStream().writeObject(
                                    new GameMessage(username + " left the server.",clients));
                        }
                    }
                }catch(IOException i){
                    i.printStackTrace();
                }
            });

            commandMap.put(Message.MessageType.GAME, msg -> {
                msg.setMessage(username + ": " + msg.getMessage());
                for(ServerThread client: clients){
                    if(!client.isClosed()){
                        try{
                            client.getObjectOutputStream().writeObject(msg);
                        }catch(IOException ie){
                            ie.printStackTrace();
                        }
                    }
                }
            });


        }


}