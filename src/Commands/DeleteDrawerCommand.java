package Commands;

import Models.Game;
import UI.BoardPaneObjects.DrawerInterface;
import UI.BoardPaneObjects.ResourceDrawer;

public class DeleteDrawerCommand implements GameCommand {
    private Game game;
    private DrawerInterface drawer;

    public DeleteDrawerCommand(Game game, DrawerInterface drawer){
        this.game = game;
        this.drawer = drawer;
    }

    @Override
    public void execute() {
        game.removePlacedComponent(drawer);

    }

    @Override
    public void unExecute() {
        game.placeComponent(drawer);
    }

    public String toString(){
        return String.format("Removed component named %s from the board",
                drawer.getComponent().getName());
    }
}
