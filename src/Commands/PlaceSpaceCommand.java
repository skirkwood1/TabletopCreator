package Commands;

import Models.Game;

import java.awt.*;
import java.awt.image.BufferedImage;

public class PlaceSpaceCommand extends GameCommand {

    private int x,y;
    private Color oldColor;
    private BufferedImage oldTexture;

    public PlaceSpaceCommand(Game game, int x, int y){
        //this.memento = new GameMemento();
        this.game = game;

        Color color = game.getBoard().getSpace(x,y).getColor();
        this.oldColor = new Color(color.getRGB());

        this.oldTexture = game.getBoard().getSpace(x,y).getTexture();

        this.x = x;
        this.y = y;
    }

    public void execute(){
        //this.memento.setState(this.game);
        if(game.getBoard().useTexture()){
            game.getBoard().setSquare(x,y,game.getBoard().getTexture());
        }else{
            game.getBoard().setSquare(x,y,this.game.getBoard().getColor());
        }
    }

    public void unExecute(){
        //game = memento.getState();

        if(this.oldTexture == null){
            game.getBoard().setSquare(x,y,oldColor);
        }else{
            game.getBoard().setSquare(x,y,oldTexture);
        }
    }
}
