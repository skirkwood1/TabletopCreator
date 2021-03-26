package Commands;

import Models.Game;
import Models.Piece;
import Models.Space;

import java.awt.*;

public class DeleteSpaceCommand extends GameCommand{

    private Space space;
    private int x,y;

    private Piece oldPiece;
    private Color oldColor;

    public DeleteSpaceCommand(Game game,int x, int y, Space space){
        this.game = game;
        this.x = x;
        this.y = y;
        this.space = space;

        this.oldPiece = game.getBoard().getSpace(x,y).getPiece();
        this.oldColor = game.getBoard().getSpace(x,y).getColor();
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
            game.getBoard().setSquare(x,y,oldColor);
        }
    }
}
