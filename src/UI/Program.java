package UI;

import javax.swing.*;
import Models.*;


public class Program {

    public static void main(String[] args){
        new Card("Pizza","It's-a very good-a","src/Images/icons8-save-100.png");
        new Card("Burger","An American classic","src/Images/folder-open-outline-filled.png");

        SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                new Frame();
            }
        });

        Dice die = new Dice(6);
    }
}
