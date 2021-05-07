package Commands;

import Models.*;

/* Adds a component (piece or card) to the project.
* Undo removes the component.
* */
public class AddComponentCommand implements GameCommand {
    private final GameComponent component;

    private final Game game;

    public AddComponentCommand(Game game, GameComponent component){
        this.game = game;
        this.component = component;
    }

    public void execute(){
        game.addComponent(component);
    }

    public void unExecute(){
        game.removeComponent(component);
    }

    public String toString(){
        return "Imported component named " + component + " to project.";
    }
}
