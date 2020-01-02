package Models;

import java.io.File;
import java.util.ArrayList;

public class Card {
    private String text;
    private File image;

    public static ArrayList<Card> deck = new ArrayList<>();

    public Card(String filename, String text){
        this.image = new File(filename);
        this.text = text;
        deck.add(this);
    }

}
