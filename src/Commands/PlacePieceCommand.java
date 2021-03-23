package Commands;

import Models.Game;
import Models.Piece;
import Models.Space;

public class PlacePieceCommand extends GameCommand {

    private int x,y;
    private Piece piece;

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
}
