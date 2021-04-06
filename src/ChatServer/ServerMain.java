package ChatServer;

import javax.swing.*;

public class ServerMain {
    public static void main(String[] args){

        SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                new ServerWindow();
            }
        });
    }
}
