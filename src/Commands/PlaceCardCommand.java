package Commands;

import Models.CardInterface;
import Models.Game;

import java.awt.*;

public class PlaceCardCommand implements GameCommand{

    private final Game game;
    private final CardInterface card;
    private final Point point;

    public PlaceCardCommand(Game game, CardInterface card, Point point){
        this.game = game;
        this.card = card.copy();
        this.point = point;
    }

    @Override
    public void execute() {
        game.placeCard(card,point);
    }

    @Override
    public void unExecute() {
        game.removePlacedCard(card);
    }

    public String toString(){
        return String.format("Placed card at (%d,%d)",(int)point.getX(),(int)point.getY());
    }
}
