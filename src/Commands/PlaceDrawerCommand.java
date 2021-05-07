package Commands;

import Models.CardInterface;
import Models.Game;
import Models.Player;
import Models.Resource;
import UI.BoardPaneObjects.DrawerInterface;
import UI.BoardPaneObjects.ResourceDrawer;

import java.util.ArrayList;

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
