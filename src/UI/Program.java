package UI;

import javax.swing.*;
import Models.*;


public class Program {

    public static void main(String[] args){
        new Card("poopy","src/Images/icons8-save-100.png","stinky");
        new Card("stinky","src/Images/folder-open-outline-filled.png","poopy");

        Card card = Card.getCard("stinky");
        System.out.println(card.getName());
        SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                new Frame();
            }
        });

        Dice die = new Dice(6);
    }
}
