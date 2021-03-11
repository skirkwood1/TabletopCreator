package Commands;

import Models.Game;

import java.awt.*;

public class UpdateColorCommand extends GameCommand {
    Color color;

    public UpdateColorCommand(Game game, Color color){
        this.memento = new GameMemento();
        this.game = game;

        this.color = color;
    }

    public void execute(){
        memento.setState(this.game);

        this.game.getBoard().setColor(this.color);
    }

    public void unExecute(){
        this.game = memento.getState();
    }
}
