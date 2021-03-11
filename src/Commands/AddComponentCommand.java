package Commands;

import Models.Card;
import Models.Component;
import Models.Game;
import Models.Piece;

public class AddComponentCommand extends GameCommand {
    private Component component;

    public AddComponentCommand(Game game, Component component){
        this.memento = new GameMemento();
        this.game = game;
        this.component = component;
    }

    public void execute(){
        this.memento.setState(this.game);

        if(this.component instanceof Piece){
            game.addPiece((Piece)component);
        }
        else if(this.component instanceof Card){
            game.addCard((Card)component);
        }
    }

    public void unExecute(){
        game = memento.getState();
    }
}
