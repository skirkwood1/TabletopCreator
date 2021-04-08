package ChatServer;

import UI.TextPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class ServerWindow {
    //ServerSocket serverSocket;

    HashMap<String,String> commandMap;

    ArrayList<ServerThread> clients;

    private final int port = 8888;

    public ServerWindow(){
        this.clients = new ArrayList<>();

        try(ServerSocket serverSocket = new ServerSocket(port)){
            while(true){
                Socket socket = serverSocket.accept();
                System.out.println("Connected to client");

                ServerThread client = new ServerThread(socket,this.clients);
                Thread t = new Thread(client);
                t.start();

                clients.add(client);
            }

        }catch(IOException e){

        }
    }

    public void parseCommand(String[] command){

    }

    public static void main(String[] args){

        EventQueue.invokeLater(new Runnable(){
            public void run(){
                new ServerWindow();
            }
        });
    }
}