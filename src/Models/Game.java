package Models;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class Game implements Serializable {

    private static final long serialVersionUID = -458626325330702518L;
    //Lists of components that have been imported into the project

    private String name;

    private ArrayList<Card> cards;
    private ArrayList<Dice> diceCollection;
    private ArrayList<Piece> pieces;
    private ArrayList<Deck> decks;
    private ArrayList<Texture> textures;

    private Board board;

    private GameComponent selectedComponent = null;
    private CardInterface selectedCard = null;

    private LinkedHashMap<CardInterface, Point> placedCards;

    public Game(){
        this.cards = new ArrayList<>();
        this.diceCollection = new ArrayList<>();
        this.pieces = new ArrayList<>();
        this.board = new Board(10,10);
        this.decks = new ArrayList<>();
        this.textures = new ArrayList<>();
        this.placedCards = new LinkedHashMap<>();
    }

    public Game(Game game){
        this.cards = game.getCards();
        this.diceCollection = game.getDice();
        this.pieces = game.pieces;
        this.textures = new ArrayList<>();
        this.board = game.getBoard();
        this.placedCards = new LinkedHashMap<>();
    }

    public Game(int x,int y){
        this.cards = new ArrayList<>();
        this.diceCollection = new ArrayList<>();
        this.pieces = new ArrayList<>();
        this.board = new Board(x,y);
        this.decks = new ArrayList<>();
        this.textures = new ArrayList<>();
        this.placedCards = new LinkedHashMap<>();
    }

    public Game(ArrayList<Card> cards, ArrayList<Dice> diceCollection, ArrayList<Piece> pieces, Board board){
        this.cards = cards;
        this.diceCollection = diceCollection;
        this.pieces = pieces;
        this.board = board;
        this.textures = new ArrayList<>();
        this.placedCards = new LinkedHashMap<>();
    }

    public Card addCard(Card card){
        cards.add(card);
        return card;
    }

    public void addPiece(Piece piece){
        pieces.add(piece);
    }

    public void createDeck(String name,ArrayList<Card> cards){
        try{
            ComponentImage cardBack = new ComponentImage(ImageIO.read(getClass().getClassLoader().getResource("cardback.png")));
            this.decks.add(new Deck(name,cards,cardBack));
        }catch(Exception e){}

    }

    public void createDeck(Deck deck){
        this.decks.add(deck);
    }

    public Deck getDeck(String name){
        for(Deck deck: decks){
            if(deck.getName().equals(name)){
                return deck;
            }
        }
        return null;
    }

    public ArrayList<Deck> getDecks(){
        return this.decks;
    }

    public void addTexture(Texture texture){
        this.textures.add(texture);
    }

    public Texture getTexture(String name){
        for(Texture t: textures){
            if(t.getName().equals(name)){
                return t;
            }
        }
        return null;
    }

    public ArrayList<Texture> getTextures(){
        return this.textures;
    }

    public ArrayList<Card> getCards(){
        return cards;
    }

    public ArrayList<Piece> getPieces(){
        return pieces;
    }

    public Card getCard(String name){
        for(Card card: cards){
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
        for(Card card: cards){
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

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

//    public void placeCard(Card card,Point point){
//        this.placedCards.put(card.copy(),point);
//    }

    public void placeCard(CardInterface cardInterface, Point point){
        this.placedCards.put(cardInterface,point);
    }

    public HashMap<CardInterface,Point> getPlacedCards(){
        return this.placedCards;
    }

    public void removePlacedCard(CardInterface card){
        this.placedCards.remove(card);
    }

    public GameComponent getSelectedComponent(){
        return selectedComponent;
    }

    public void setSelectedComponent(GameComponent component){
        this.selectedComponent = component;
    }

    public CardInterface getSelectedCard(){
        return selectedCard;
    }

    public void setSelectedCard(CardInterface card){
        this.selectedCard = card;
    }

    public void removeCard(Card card){
        cards.remove(card);
    }

    public void removePiece(Piece piece){
        pieces.remove(piece);
    }

    public void removeTexture(Texture texture){textures.remove(texture);}
}
