package ChatServer;

import UI.TextPanel;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class ServerWindow {
    //ServerSocket serverSocket;

    HashMap<String,String> commandMap;

    private final int port = 8888;

    public ServerWindow(){

        try(ServerSocket serverSocket = new ServerSocket(port)){
            while(true){
                Socket socket = serverSocket.accept();

                Thread t = new Thread(new ServerThread(socket));
                t.start();
            }

        }catch(IOException e){

        }
    }

    public void parseCommand(String[] command){

    }
}