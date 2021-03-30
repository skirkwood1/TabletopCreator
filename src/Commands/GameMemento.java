package Commands;

import Models.*;

import java.util.ArrayList;

class GameMemento {
    private ArrayList<Card> deck;
    private ArrayList<Dice> diceCollection;
    private ArrayList<Piece> pieces;

    private int height;
    private int width;
    private Space[][] spaces;

    Game getState(){
        Board board = new Board(height,width,spaces);
        Game game = new Game(deck,diceCollection,pieces,board);

        return game;
    }

    void setState(Game game){
        this.deck = game.getCards();
        this.diceCollection = game.getDice();
        this.pieces = game.getPieces();

        this.width = game.getBoard().getSize()[0];
        this.height = game.getBoard().getSize()[1];
        this.spaces = game.getBoard().getSpaces();

    }
}
