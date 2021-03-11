package Commands;

import Models.Game;

public class GameMemento {
    Game game;

    public Game getState(){
        return game;
    }

    public void setState(Game game){
        this.game = game;
    }
}
