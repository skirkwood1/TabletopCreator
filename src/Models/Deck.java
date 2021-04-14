package Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

/*Represents an individual list of cards. The project has a set of cards,
* but they can be separated into decks which can contain multiple copies
* of each card for use in separate parts of the game
* */
public class Deck implements Serializable {
    private String name;
    private ArrayList<Card> cards;

    public Deck(String name){
        this.name = name;
        this.cards = new ArrayList<>();
    }

    public Deck(String name,Card ... cards){
        this.name = name;
        this.cards = new ArrayList<>();

        Collections.addAll(this.cards, cards);
    }

    public Deck(String name,ArrayList<Card> cards){
        this.name = name;
        this.cards = cards;
    }

    public void shuffle(){
        Collections.shuffle(cards);
    }

    public Card drawCard(){
        Card card = this.cards.get(0);
        this.cards.remove(0);
        return card;
    }

    public void addCard(Card card){
        this.cards.add(card.copy());
    }

    public void addCard(Card card,int n){
        for(int i = 0; i < n; i++){
            this.cards.add(card.copy());
        }
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }
    public ArrayList<Card> getCards(){
        return this.cards;
    }
}
