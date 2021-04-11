package Commands;

import Models.Game;
import Models.Piece;
import Models.Space;
import Models.Texture;

import java.awt.*;

/* Remove the piece or the color from a space
* Saves the piece/color for undo
* */
public class DeleteSpaceCommand extends GameCommand{

    private Space space;
    private int x,y;

    private Piece oldPiece;

    private boolean usedTexture;
    private Color oldColor;
    private Texture oldTexture;

    public DeleteSpaceCommand(Game game,int x, int y, Space space){
        this.game = game;
        this.x = x;
        this.y = y;
        this.space = space;

        this.oldPiece = game.getBoard().getSpace(x,y).getPiece();
        if(game.getBoard().getSpace(x,y).isUsingTexture()){
            this.usedTexture = true;
            this.oldTexture = game.getBoard().getSpace(x,y).getTexture();
        }else{
            this.usedTexture = false;
            this.oldColor = game.getBoard().getSpace(x,y).getColor();
        }
    }

    @Override
    public void execute() {
        if(space.isOccupied()){
            game.getBoard().getSpace(x,y).removePiece();
        }else{
            game.getBoard().setSquare(x,y,game.getBoard().getDefaultColor());
        }
    }

    @Override
    public void unExecute() {
        if(oldPiece != null){
            game.getBoard().getSpace(x,y).addPiece(oldPiece);
        }else{
            if(usedTexture){
                game.getBoard().setSquare(x,y,oldTexture);
            }else{
                game.getBoard().setSquare(x,y,oldColor);
            }
        }
    }
}
