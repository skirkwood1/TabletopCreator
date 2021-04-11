package Commands;

import Models.Game;
import Models.Texture;

import java.awt.*;
import java.awt.image.BufferedImage;

/*Put a color/texture at a square
* Saves the old color/texture
* */
public class PlaceSpaceCommand extends GameCommand {

    private final int x,y;

    private final boolean useTexture;
    private final Color newColor;
    private final Texture newTexture;

    private final boolean usedTexture;
    private final Color oldColor;
    private final Texture oldTexture;

    public PlaceSpaceCommand(Game game, int x, int y){
        this.game = game;

        this.useTexture = game.getBoard().useTexture();
        this.newColor = game.getBoard().getColor();
        this.newTexture = game.getBoard().getTexture();

        this.usedTexture = game.getBoard().getSpace(x,y).isUsingTexture();
        this.oldColor = game.getBoard().getSpace(x,y).getColor();
        this.oldTexture = game.getBoard().getSpace(x,y).getTexture();

        this.x = x;
        this.y = y;
    }

    public void execute(){
        //this.memento.setState(this.game);
        if(useTexture){
            game.getBoard().setSquare(x,y,newTexture);
        }else{
            game.getBoard().setSquare(x,y,newColor);
        }
    }

    public void unExecute(){
        if(usedTexture){
            game.getBoard().setSquare(x,y,oldTexture);
        }else{
            game.getBoard().setSquare(x,y,oldColor);
        }
    }
}
