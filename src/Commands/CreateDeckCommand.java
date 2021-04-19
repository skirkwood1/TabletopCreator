package Commands;

import Models.Deck;
import Models.Game;

public class CreateDeckCommand implements GameCommand {

    private Game game;
    private Deck deck;

    public CreateDeckCommand(Game game, Deck deck){
        this.game = game;
        this.deck = deck;
    }

    @Override
    public void execute() {
        game.createDeck(deck);
    }

    @Override
    public void unExecute() {
        game.getDecks().remove(deck);

    }

    public String toString(){
        return String.format("Created deck named \"%s\" with %d cards",deck.getName(),deck.getCards().size());
    }
}
