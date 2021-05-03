package Commands;

import Models.CardInterface;
import Models.Game;

import java.awt.*;

public class CardMoveCommand implements GameCommand{
    private final Game game;
    private final Point startPoint,endPoint;
    private final CardInterface card;

    public CardMoveCommand(Game game, CardInterface card, Point startPoint, Point endPoint){
        this.game = game;
        this.card = card;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
    }

    @Override
    public void execute() {
        game.getPlacedComponents().put(card,endPoint);
    }

    @Override
    public void unExecute() {
        game.getPlacedComponents().put(card,startPoint);

    }

    public String toString(){
        return String.format("Moved card %s from (%d,%d) to (%d,%d)",
                card,(int)startPoint.getX(),(int)startPoint.getY(),(int)endPoint.getX(),(int)endPoint.getY());
    }
}
