package Commands;

import Models.Game;
import UI.Frame;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

/* Replace game with new one
* Saves the old game for undo
* */
public class OpenGameCommand extends GameCommand {

    private final Game oldGame;
    private final Game newGame;

    private final Frame frame;

    public OpenGameCommand(Frame frame, Game oldGame, Game newGame){
        this.frame = frame;

        this.oldGame = oldGame;
        this.newGame = newGame;
    }


    public void execute(){
        frame.setGame(newGame);
        //frame.updateBoard();
    }

    public void unExecute(){
        frame.setGame(oldGame);
        //frame.updateBoard();
    }
}
