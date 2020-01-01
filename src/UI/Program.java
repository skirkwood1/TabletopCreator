package UI;

import javax.swing.*;
import Models.Dice;


public class Program {

    public static void main(String[] args){
        SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                new Frame();
            }
        });

        Dice die = new Dice(6);
    }
}
