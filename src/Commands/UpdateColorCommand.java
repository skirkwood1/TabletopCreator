package Commands;

import Models.Game;
import Models.Texture;

import java.awt.*;

/*Change the board's color to a new color or texture.
* Saves the old color/texture in case of an undo.
* */
public class UpdateColorCommand implements GameCommand {
    private Color color,oldColor;

    private final Game game;
    private Texture texture,oldTexture;
    private final boolean usedTexture,useTexture;

    public UpdateColorCommand(Game game, Color color){
        //this.memento = new GameMemento();
        this.game = game;

        this.color = color;
        this.useTexture = false;

        if(game.getBoard().useTexture()){
            this.oldTexture = game.getBoard().getTexture();
            this.usedTexture = true;
        }
        else{
            this.oldColor = new Color(game.getBoard().getColor().getRGB());
            this.usedTexture = false;
        }
    }

    public UpdateColorCommand(Game game, Texture texture){
        //this.memento = new GameMemento();
        this.game = game;

        this.texture = texture;
        this.useTexture = true;

        if(game.getBoard().useTexture()){
            this.oldTexture = game.getBoard().getTexture();
            this.usedTexture = true;
        }
        else{
            this.oldColor = new Color(game.getBoard().getColor().getRGB());
            this.usedTexture = false;
        }
    }

    public void execute(){
        if(useTexture){
            game.getBoard().setTexture(this.texture);
        }
        else{
            game.getBoard().setColor(this.color);
        }
    }

    public void unExecute(){
        if(usedTexture){
            game.getBoard().setTexture(this.oldTexture);
        }
        else{
            game.getBoard().setColor(this.oldColor);
        }

    }

    public String toString(){
        if(useTexture) {
            return String.format("Changed color to %s",texture);
        }else{
            return String.format("Changed color to [%d,%d,%d]",color.getRed(),color.getGreen(),color.getBlue());
        }
    }
}