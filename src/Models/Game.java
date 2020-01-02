package Models;

import java.io.Serializable;
import java.util.ArrayList;

public class Game implements Serializable {
    private ArrayList<Card> deck;
    private ArrayList<Dice> diceCollection;

    public Game(){
        this.deck = new ArrayList<>();
        this.diceCollection = new ArrayList<>();
    }

    public Card addCard(String name, String text, String filename){
        Card card = new Card(name,text,filename);
        deck.add(card);
        return card;
    }

    public ArrayList<Card> getDeck(){
        return deck;
    }

    public Card getCard(String name){
        for(Card card: deck){
            if(card.getName().equals(name)){
                return card;
            }
        }
        return null;
    }

    public String toString(){
        String finalString = "";
        int numDice = 0;
        for(Card card: deck){
            finalString += card.getName() + "   ";
            finalString += card.getText() + "   ";
            finalString += card.getImage().getAbsolutePath();
            finalString += "\r\n";
        }
        for(Dice die: diceCollection){
            numDice += 1;
        }
        finalString = finalString + numDice;

        return finalString;
    }
}
