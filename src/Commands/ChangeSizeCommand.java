package Commands;

import Models.Game;

public class ChangeSizeCommand extends GameCommand {
    int desiredWidth,desiredHeight;

    public ChangeSizeCommand(Game game, int desiredWidth, int desiredHeight){
        this.game = game;
        this.desiredWidth = desiredWidth;
        this.desiredHeight = desiredHeight;
    }

    public void execute(){
        this.memento.setState(this.game);

        game.getBoard().setSize(desiredWidth,desiredHeight);
    }

    public void unExecute(){
        this.game = memento.getState();
    }
}
