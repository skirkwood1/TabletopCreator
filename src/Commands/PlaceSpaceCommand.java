package Commands;

import Models.Game;

import java.awt.*;

public class PlaceSpaceCommand extends GameCommand {

    private int x,y;
    private Color oldColor;

    public PlaceSpaceCommand(Game game, int x, int y){
        //this.memento = new GameMemento();
        this.game = game;

        Color color = game.getBoard().getSpace(x,y).getColor();
        this.oldColor = new Color(color.getRGB());

        this.x = x;
        this.y = y;
    }

    public void execute(){
        //this.memento.setState(this.game);

        game.getBoard().setSquare(x,y,this.game.getBoard().getColor());
    }

    public void unExecute(){
        //game = memento.getState();

        game.getBoard().setSquare(x,y,oldColor);
    }
}
