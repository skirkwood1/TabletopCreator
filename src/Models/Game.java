package Models;

import java.io.Serializable;
import java.util.ArrayList;

public class Game implements Serializable {

    private ArrayList<Card> deck;
    private ArrayList<Dice> diceCollection;
    private ArrayList<Piece> pieces;

    private Board board;

    private Component selectedComponent;

    public Game(){
        this.deck = new ArrayList<>();
        this.diceCollection = new ArrayList<>();
        this.pieces = new ArrayList<>();
        this.board = new Board(10,10);
    }

    public Game(Game game){
        this.deck = game.getDeck();
        this.diceCollection = game.getDice();
        this.pieces = game.pieces;

        this.board = game.getBoard();
    }

    public Game(ArrayList<Card> deck, ArrayList<Dice> diceCollection, ArrayList<Piece> pieces, Board board){
        this.deck = deck;
        this.diceCollection = diceCollection;
        this.pieces = pieces;

        this.board = board;
    }

    public Card addCard(String name, String text, String filename){
        Card card = new Card(name,text,filename);
        deck.add(card);
        return card;
    }

    public Card addCard(Card card){
        deck.add(card);
        return card;
    }

    public Piece addPiece(String name, String text, String filename){
        Piece piece = new Piece(name,text,filename);
        pieces.add(piece);
        return piece;
    }

    public Piece addPiece(Piece piece){
        pieces.add(piece);
        return piece;
    }

    public ArrayList<Card> getDeck(){
        return deck;
    }

    public ArrayList<Piece> getPieces(){
        return pieces;
    }

    public Card getCard(String name){
        for(Card card: deck){
            if(card.getName().equals(name)){
                return card;
            }
        }
        return null;
    }

    public Piece getPiece(String name){
        for(Piece piece: pieces){
            if(piece.getName().equals(name)){
                return piece;
            }
        }
        return null;
    }

    public Board getBoard(){
        return this.board;
    }

    public ArrayList<Dice> getDice(){ return this.diceCollection;}

    public String toString(){
        String finalString = "";
        int numDice = 0;
        for(Card card: deck){
            finalString += card.getName() + "   ";
            finalString += card.getText() + "   ";
            //finalString += card.getImage().getAbsolutePath();
            finalString += "\r\n";
        }
        for(Dice die: diceCollection){
            numDice += 1;
        }
        finalString = finalString + numDice + "\n\r";

        finalString += board.toString();

        return finalString;
    }

    public Component getSelectedComponent(){
        return selectedComponent;
    }

    public void setSelectedComponent(Component piece){
        this.selectedComponent = piece;
    }

    public void removeCard(Card card){
        deck.remove(card);
    }

    public void removePiece(Piece piece){
        deck.remove(piece);
    }
}
