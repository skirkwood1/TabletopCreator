package Models;

import java.util.ArrayList;
import java.util.Collections;

public class Deck {
    private String name;
    private ArrayList<Card> cards;

    public Deck(String name){
        this.name = name;
        this.cards = new ArrayList<>();
    }

    public Deck(String name,Card ... cards){
        this.name = name;
        this.cards = new ArrayList<>();

        for(Card card:cards){
            this.cards.add(card);
        }
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
        this.cards.add(card);
    }

    public void addCard(Card card,int n){
        for(int i = 0; i < n; i++){
            this.cards.add(card);
        }
    }

    public String getName(){
        return this.name;
    }
    public ArrayList<Card> getCards(){
        return this.cards;
    }
}
