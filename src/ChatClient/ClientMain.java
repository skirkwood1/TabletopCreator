package ChatClient;

import javax.swing.*;
import java.io.IOException;

public class ClientMain {
    public static void main(String[] args){

        SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                ClientWindow win = new ClientWindow("localhost");
                try{
                    win.startConnection();
                }catch(Exception e){}
            }
        });
    }
}