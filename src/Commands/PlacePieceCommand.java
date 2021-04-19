package Commands;

import Models.Game;
import Models.Piece;
import Models.Space;

/* Place a piece at a square
* Undo removes the piece from the square
* */
public class PlacePieceCommand implements GameCommand {

    private final int x,y;
    private final Piece piece;
    private final Game game;

    public PlacePieceCommand(Game game, int x, int y, Piece piece){
        this.game = game;
        this.x = x;
        this.y = y;
        this.piece = piece;
    }

    public void execute(){
        game.getBoard().getSpace(x,y).addPiece(piece);
    }

    public void unExecute(){
        game.getBoard().getSpace(x,y).removePiece();
    }

    public String toString(){
        return String.format("Placed piece at (%d,%d)",x,y);
    }
}
