package UI;

import javax.swing.*;
import Models.Dice;

public class GUI {
    public static void main(String[] args){
        SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                new Frame();


            }
        });
    }
}
