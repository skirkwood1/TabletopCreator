package ChatServer;

import java.awt.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class ServerProgram {
    //ServerSocket serverSocket;

    private final int port = 8888;

    public ServerProgram(){
        ArrayList<ServerThread> clients;
        clients = new ArrayList<>();

        try(ServerSocket serverSocket = new ServerSocket(port)){
            while(true){
                Socket socket = serverSocket.accept();
                System.out.println("Connected to client");

                ServerThread client = new ServerThread(socket,clients);
                Thread t = new Thread(client);
                t.start();

                clients.add(client);
            }

        }catch(IOException e){

        }
    }

    public static void main(String[] args){
        EventQueue.invokeLater(new Runnable(){
            public void run(){
                new ServerProgram();
            }
        });
    }
}