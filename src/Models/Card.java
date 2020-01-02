package Models;

import java.io.File;
import java.util.ArrayList;

public class Card {
    private String name;
    private String text;
    private File image;

    public static ArrayList<Card> deck = new ArrayList<>();

    public Card(String name, String text, String filename){
        this.name = name;
        this.image = new File(filename);
        this.text = text;
        deck.add(this);
    }

    public String getName(){
        return name;
    }

    public String getText(){
        return text;
    }

    public File getImage(){
        return image;
    }

    public static Card getCard(String name){
        for(Card card: deck){
            if(card.getName().equals(name)){
                return card;
            }
        }
        return null;
    }
}
