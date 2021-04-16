package ChatServer;

import Commands.GameCommand;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;

public class ServerThread implements Runnable {


        private Socket socket;
        private ArrayList<ServerThread> clients;

        private volatile ObjectOutputStream oos;

        private ObjectInputStream ois;

        private String username;

        public ServerThread(Socket socket, ArrayList<ServerThread> clients) {
            this.socket = socket;
            this.clients = clients;
        }

        public void run() {
            try {
                this.oos = new ObjectOutputStream(socket.getOutputStream());
                this.ois = new ObjectInputStream(socket.getInputStream());

                System.out.println("Created ObjectInputStream");

                System.out.println("Created ObjectOutputStream");

                oos.writeObject(new GameMessage("Please enter a username:",this.clients));

                try{
                    Message gm = (Message)ois.readObject();
                    this.username = gm.getMessage();
                    oos.writeObject(new GameMessage("Username set to: " + username,this.clients));
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
                }catch(ClassNotFoundException ce){

                }

                while(!socket.isClosed()){
                    String s;

                    try{
                        Message msg = (Message)ois.readObject();
                        if(msg instanceof QuitMessage){
                            System.out.println(username + " quit");
                            socket.close();
                        }else{
                            GameMessage gm = (GameMessage)msg;
                            s = gm.getMessage();
                            gm.setMessage(this.username + ": " + s);

                            for(ServerThread client: this.clients){
                                if(!client.isClosed()){
                                    try{
                                        client.getObjectOutputStream().writeObject(gm);
                                    }catch(SocketException se){
                                        client.socket.close();
                                    }
                                }
                            }
                        }

                    }catch(ClassNotFoundException ce){
                        System.err.println("Class not found");
                    }
                }

                clients.remove(this);

                for(ServerThread client: this.clients){
                    if(!client.isClosed()){
                        client.getObjectOutputStream().writeObject(
                                new GameMessage(username + " left the server.",this.clients));
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


}