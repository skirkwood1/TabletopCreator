package Commands;

import Models.*;

/* Adds a component (piece or card) to the project.
* Undo removes the component.
* */
public class AddComponentCommand extends GameCommand {
    private final Component component;

    public AddComponentCommand(Game game, Component component){
        this.game = game;
        this.component = component;
    }

    public void execute(){
        if(this.component instanceof Piece){
            game.addPiece((Piece)component);
        }
        else if(this.component instanceof Card){
            game.addCard((Card)component);
        }
        else if(this.component instanceof Texture){
            game.addTexture((Texture)component);
        }
    }

    public void unExecute(){
        if(this.component instanceof Piece){
            game.removePiece((Piece)component);
        }
        else if(this.component instanceof Card){
            game.removeCard((Card)component);
        }
        else if(this.component instanceof Texture){
            game.removeTexture((Texture)component);
        }
    }
}
