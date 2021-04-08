package ChatServer;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

public class ServerThread implements Runnable {


        private Socket socket;
        private ArrayList<ServerThread> clients;

        private OutputStream output;

        private String username;

        public ServerThread(Socket socket, ArrayList<ServerThread> clients) {
            this.socket = socket;
            this.clients = clients;
        }

        public void run() {
            try {
                InputStream input = socket.getInputStream();
                Scanner in = new Scanner(new InputStreamReader(input));
                this.output = socket.getOutputStream();

                output.write(("Please enter a username: \n").getBytes());

                this.username = in.nextLine();
                output.write(("Username set to: " + username + "\n").getBytes());
                //this.out = new PrintWriter(output, true);

                while(!socket.isClosed()){
                    String s = in.nextLine();

                    if(!s.equals("")){
                        for(ServerThread client: this.clients){
                            String message = this.username + ": " + s + "\n";
                            client.getWriter().write(message.getBytes());
                        }
                    }
                }

                socket.close();
            } catch (IOException ex) {
                System.out.println("Server exception: " + ex.getMessage());
                ex.printStackTrace();
            }
        }

        public OutputStream getWriter(){
            return this.output;
        }


}