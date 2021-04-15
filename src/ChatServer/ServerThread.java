package ChatServer;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;

public class ServerThread implements Runnable {


        private Socket socket;
        private ArrayList<ServerThread> clients;

        private volatile PrintWriter printWriter;
        private volatile ObjectOutputStream oos;

        private ObjectInputStream ois;

        private String username;

        public ServerThread(Socket socket, ArrayList<ServerThread> clients) {
            this.socket = socket;
            this.clients = clients;
        }

        public void run() {
            try {
                //InputStream input = socket.getInputStream();
                this.oos = new ObjectOutputStream(socket.getOutputStream());
                this.ois = new ObjectInputStream(socket.getInputStream());

                System.out.println("Created ObjectInputStream");
                //Scanner in = new Scanner(new InputStreamReader(input));
                //OutputStream output = socket.getOutputStream();
                //this.printWriter = new PrintWriter(output,true);

                System.out.println("Created ObjectOutputStream");

                oos.writeObject(new GameMessage("Please enter a username:",this.clients));
                //printWriter.println("Please enter a username:");

                //this.username = in.nextLine();
                try{
                    GameMessage gm = (GameMessage)ois.readObject();
                    this.username = gm.getMessage();
                    System.out.println(username);
                    oos.writeObject(new GameMessage("Username set to: " + username,this.clients));
                }catch(ClassNotFoundException ce){

                }
                //printWriter.println("Username set to: " + username);
                //this.out = new PrintWriter(output, true);

                while(!socket.isClosed()){
                    String s;
//                    if(in.hasNextLine()){
//                        s = in.nextLine();
//                    }

                    try{
                        GameMessage gm = (GameMessage)ois.readObject();
                        s = gm.getMessage();
                        gm.setMessage(this.username + ": " + s);

                        //System.out.println(s);

                        for(ServerThread client: this.clients){
                            if(!client.isClosed()){
                                client.getObjectOutputStream().writeObject(gm);
                            }
                        }

                    }catch(ClassNotFoundException ce){

                    }

//                    if(!s.equals("")){
//                        for(ServerThread client: this.clients){
//                            if(!client.isClosed()){
//                                String message = this.username + ": " + s;
//                                client.getWriter().println(message);
//                            }
//                        }
//                    }
                }
                clients.remove(this);

            } catch (IOException ex) {
                System.out.println("Server exception: " + ex.getMessage());
                ex.printStackTrace();
            }
        }

        public PrintWriter getWriter(){
            return this.printWriter;
        }

        public ObjectOutputStream getObjectOutputStream(){
            return this.oos;
        }

        public boolean isClosed(){
            return this.socket.isClosed();
        }

        public String getUsername(){return this.username;}


}