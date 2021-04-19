package Commands;

import Models.Card;
import Models.Deck;

import java.util.ArrayList;

public class DeleteFromDeckCommand implements GameCommand {

    private Deck deck;
    private ArrayList<Card> cards;

    public DeleteFromDeckCommand(Deck deck, ArrayList<Card> cards){
        this.deck = deck;
        this.cards = cards;
    }

    @Override
    public void execute() {
        deck.getCards().removeAll(cards);
    }

    @Override
    public void unExecute() {
        deck.getCards().addAll(cards);
    }
}
