package Commands;

import Models.Game;
import UI.BoardPaneObjects.DrawerInterface;

public class PlaceDrawerCommand implements GameCommand {
    private Game game;
    private DrawerInterface drawer;

    public PlaceDrawerCommand(Game game, DrawerInterface drawer){
        this.game = game;
        this.drawer = drawer;
    }

    @Override
    public void execute() {
        game.placeComponent(drawer);
    }

    @Override
    public void unExecute() {
        game.removePlacedComponent(drawer);
    }

    @Override
    public String toString() {
        return String.format("Placed component %s at point (%d,%d)",
                drawer.getComponent(),drawer.getPoint().x,drawer.getPoint().y);
    }
}
