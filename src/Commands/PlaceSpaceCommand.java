package Commands;

import Models.Game;

public class PlaceSpaceCommand extends GameCommand {

    int x,y;

    public PlaceSpaceCommand(Game game, int x, int y){
        this.memento = new GameMemento();
        this.game = game;

        this.x = x;
        this.y = y;
    }

    public void execute(){
        this.memento.setState(this.game);

        this.game.getBoard().setSquare(x,y,this.game.getBoard().getColor());
    }

    public void unExecute(){
        this.game = memento.getState();
    }
}
