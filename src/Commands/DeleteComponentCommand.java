package Commands;

import Models.Game;
import Models.GameComponent;

public class DeleteComponentCommand implements GameCommand {

    private Game game;
    private GameComponent component;

    private int index;

    public DeleteComponentCommand(Game game, GameComponent component){
        this.game = game;
        this.component = component;
        this.index = game.getComponentIndex(component);
    }

    @Override
    public void execute() {
        game.removeComponent(component);
    }

    @Override
    public void unExecute() {
        if(index >= 0){
            game.addComponent(component,index);
        }else{
            game.addComponent(component);
        }

    }

    public String toString(){
        return "Deleted component named " + component.getName();
    }
}
