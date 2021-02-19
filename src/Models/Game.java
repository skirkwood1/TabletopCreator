package Models;

import java.io.Serializable;
import java.util.ArrayList;

public class Game implements Serializable {

    private ArrayList<Card> deck;
    private ArrayList<Dice> diceCollection;
    private ArrayList<Piece> pieces;

    private Board board;

    public Game(){
        this.deck = new ArrayList<>();
        this.diceCollection = new ArrayList<>();
        this.pieces = new ArrayList<>();
        this.board = new Board(10,10);
    }

    public Card addCard(String name, String text, String filename){
        Card card = new Card(name,text,filename);
        deck.add(card);
        return card;
    }

    public Piece addPiece(String name, String text, String filename){
        Piece piece = new Piece(name,text,filename);
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

    public String toString(){
        String finalString = "";
        int numDice = 0;
        for(Card card: deck){
            finalString += card.getName() + "   ";
            finalString += card.getText() + "   ";
            finalString += card.getImage().getAbsolutePath();
            finalString += "\r\n";
        }
        for(Dice die: diceCollection){
            numDice += 1;
        }
        finalString = finalString + numDice + "\n\r";

        finalString += board.toString();

        return finalString;
    }
}
