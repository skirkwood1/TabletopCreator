package Commands;

import Models.Game;

public interface GameCommand {

    public abstract void execute();

    public abstract void unExecute();

    public String toString();
}
