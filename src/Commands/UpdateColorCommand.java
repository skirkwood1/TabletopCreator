package Commands;

import Models.Game;
import Models.Texture;
import Observers.ColorLabelObserver;
import Observers.Observer;
import UI.Toolbar;

import java.awt.*;
import java.util.ArrayList;

public class UpdateColorCommand extends GameCommand {
    private Color color,oldColor;

    private Texture texture,oldTexture;
    private boolean usedTexture,useTexture;

    public UpdateColorCommand(Game game, Color color){
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
    }

    public UpdateColorCommand(Game game, Texture texture){
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
}