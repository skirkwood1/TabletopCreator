package Commands;

import Models.Game;

public abstract class GameCommand {
    Game game;
    GameMemento memento;

    public abstract void execute();

    public abstract void unExecute();
}
