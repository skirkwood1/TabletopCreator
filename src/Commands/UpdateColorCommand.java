package Commands;

import Models.Game;
import UI.Toolbar;

import java.awt.*;

public class UpdateColorCommand extends GameCommand {
    private Color color,oldColor;

    private Toolbar toolbar;

    public UpdateColorCommand(Game game, Color color, Toolbar toolbar){
        this.memento = new GameMemento();
        this.game = game;

        this.color = color;
        this.oldColor = new Color(game.getBoard().getColor().getRGB());

        this.toolbar = toolbar;
    }

    public void execute(){
        memento.setState(this.game);

        game.getBoard().setColor(this.color);
        toolbar.updateColorLabel();
    }

    public void unExecute(){
        //game = memento.getState();

        game.getBoard().setColor(this.oldColor);
        toolbar.updateColorLabel();
    }
}
