package Commands;

import Models.Game;
import UI.BoardPaneObjects.DrawerInterface;

import java.awt.*;

public class MoveDrawerCommand implements GameCommand {
    private Game game;
    private DrawerInterface drawer;
    private Point oldPoint,newPoint;

    public MoveDrawerCommand(Game game, DrawerInterface drawer, Point oldPoint, Point newPoint){
        this.game = game;
        this.drawer = drawer;
        this.oldPoint = oldPoint;
        this.newPoint = newPoint;
    }

    @Override
    public void execute() {
        drawer.move(newPoint);
    }

    @Override
    public void unExecute() {
        drawer.move(oldPoint);
    }

    public String toString(){
        return String.format("Moved component %s from point (%d,%d) to point (%d,%d)",
                drawer.getComponent(),oldPoint.x,oldPoint.y,newPoint.x,newPoint.y);
    }
}
