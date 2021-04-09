package ChatServer;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;

public class ServerThread implements Runnable {


        private Socket socket;
        private ArrayList<ServerThread> clients;

        private volatile PrintWriter printWriter;

        private String username;

        public ServerThread(Socket socket, ArrayList<ServerThread> clients) {
            this.socket = socket;
            this.clients = clients;
        }

        public void run() {
            try {
                InputStream input = socket.getInputStream();
                Scanner in = new Scanner(new InputStreamReader(input));
                OutputStream output = socket.getOutputStream();
                this.printWriter = new PrintWriter(output,true);

                printWriter.println("Please enter a username:");

                this.username = in.nextLine();
                printWriter.println("Username set to: " + username);
                //this.out = new PrintWriter(output, true);

                while(!socket.isClosed()){
                    String s = "";
                    if(in.hasNextLine()){
                        s = in.nextLine();
                    }

                    if(!s.equals("")){
                        for(ServerThread client: this.clients){
                            if(!client.isClosed()){
                                String message = this.username + ": " + s;
                                client.getWriter().println(message);
                            }
                        }
                    }
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

        public boolean isClosed(){
            return this.socket.isClosed();
        }


}