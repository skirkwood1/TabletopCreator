package Commands;

import Models.Card;
import Models.Deck;
import Models.Game;

import java.util.ArrayList;

public class AddToDeckCommand implements GameCommand {
    private Deck deck;

    private ArrayList<Card> oldCards;
    private ArrayList<Card> cards;
    int n;

    public AddToDeckCommand(Deck deck, ArrayList<Card> cards, int n){
        this.deck = deck;
        this.oldCards = new ArrayList<>();

        oldCards.addAll(deck.getCards());

        this.cards = cards;
        this.n = n;
    }


    @Override
    public void execute() {
        for(Card card:cards){
            deck.addCard(card,n);
        }
    }

    @Override
    public void unExecute() {
        deck.setCards(oldCards);
    }

    public String toString(){
        return String.format("Added %d copies each of %d different cards to deck %s",n,cards.size(),deck.getName());
    }
}
