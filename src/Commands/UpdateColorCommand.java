package Commands;

import Models.Game;
import Models.Texture;
import UI.Toolbar;

import java.awt.*;

public class UpdateColorCommand extends GameCommand {
    private Color color,oldColor;

    private Texture texture,oldTexture;
    private boolean usedTexture,useTexture;

    private Toolbar toolbar;

    public UpdateColorCommand(Game game, Color color, Toolbar toolbar){
        this.memento = new GameMemento();
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

        this.toolbar = toolbar;
    }

    public UpdateColorCommand(Game game, Texture texture, Toolbar toolbar){
        this.memento = new GameMemento();
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

        this.toolbar = toolbar;
    }

    public void execute(){
        if(useTexture){
            game.getBoard().setTexture(this.texture);
        }
        else{
            game.getBoard().setColor(this.color);
        }
        toolbar.updateColorLabel();
    }

    public void unExecute(){
        if(usedTexture){
            game.getBoard().setTexture(this.oldTexture);
        }
        else{
            game.getBoard().setColor(this.oldColor);
        }


        toolbar.updateColorLabel();
    }
}
