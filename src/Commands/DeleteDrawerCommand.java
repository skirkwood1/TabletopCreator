package Commands;

import Models.Game;
import UI.BoardPaneObjects.ResourceDrawer;

public class DeleteDrawerCommand implements GameCommand {
    private Game game;
    private ResourceDrawer drawer;

    public DeleteDrawerCommand(Game game, ResourceDrawer drawer){
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
}
