package Commands;

import Models.Card;
import Models.Deck;

import java.util.ArrayList;

import static java.util.stream.Collectors.joining;

public class DeleteFromDeckCommand implements GameCommand {

    private Deck deck;
    private ArrayList<Card> cards;

    public DeleteFromDeckCommand(Deck deck, String cardName){
        this.deck = deck;
        Card card;
//        for(Card c: deck.getCards()){
//            if(c.getName())
//        }
    }

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

    public String toString(){
        ArrayList<String> cardNames = new ArrayList<>();
        for(Card card:cards){
            cardNames.add(card.getName());
        }
        String joined = cardNames.stream()
                .collect(joining(", ", "", ""));
        return String.format("Remove the following cards from deck %s: %s",deck.getName(),joined);
    }
}
