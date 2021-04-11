package Commands;

import Models.Game;
import Models.Piece;

/* Move piece to a new location
* Save old space piece was at for undo
* TODO: Handle if new space is already occupied
* */
public class PieceMoveCommand extends GameCommand {
    private final int start_x,start_y,end_x,end_y;

    private final Piece piece;

    public PieceMoveCommand(Game game, int start_x, int start_y, int end_x, int end_y, Piece piece){
        this.game = game;

        this.start_x = start_x;
        this.start_y = start_y;
        this.end_x = end_x;
        this.end_y = end_y;

        this.piece = piece;

    }

    public void execute(){
        game.getBoard().getSpace(start_x, start_y).removePiece();
        game.getBoard().getSpace(end_x, end_y).addPiece(piece);
    }

    public void unExecute(){
        game.getBoard().getSpace(end_x, end_y).removePiece();
        game.getBoard().getSpace(start_x, start_y).addPiece(piece);
    }
}
