package UI;

import javax.swing.*;
import Models.*;


public class Program {

    public static void main(String[] args){

        SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                new Frame();
            }
        });
    }
}
