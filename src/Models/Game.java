package Models;

import UI.BoardPaneObjects.DrawerInterface;

import javax.imageio.ImageIO;
import java.io.Serializable;
import java.util.ArrayList;

public class Game implements Serializable {

    private static final long serialVersionUID = -458626325330702518L;
    //Lists of components that have been imported into the project

    private String name;

    private ArrayList<Card> cards;
    private ArrayList<Dice> diceCollection;
    private ArrayList<Piece> pieces;
    private ArrayList<Deck> decks;
    private ArrayList<Texture> textures;
    private ArrayList<ResourceSheet> resourceSheets;
    private ArrayList<Player> players;

    private Board board;

    private GameComponent selectedComponent = null;
    private CardInterface selectedCard = null;
    private ResourceSheet selectedResourceSheet = null;

    private ArrayList<DrawerInterface> placedComponents;

    public Game(){
        this.cards = new ArrayList<>();
        this.diceCollection = new ArrayList<>();
        this.pieces = new ArrayList<>();
        this.board = new Board(10,10);
        this.decks = new ArrayList<>();
        this.textures = new ArrayList<>();
        this.placedComponents = new ArrayList<>();
        this.resourceSheets = new ArrayList<>();
        this.players = new ArrayList<>();
    }

    public Game(Game game){
        this.cards = game.getCards();
        this.diceCollection = game.getDice();
        this.pieces = game.pieces;
        this.textures = new ArrayList<>();
        this.board = game.getBoard();
        this.placedComponents = new ArrayList<>();
        this.resourceSheets = new ArrayList<>();
        this.players = new ArrayList<>();
    }

    public Game(int x,int y){
        this.cards = new ArrayList<>();
        this.diceCollection = new ArrayList<>();
        this.pieces = new ArrayList<>();
        this.board = new Board(x,y);
        this.decks = new ArrayList<>();
        this.textures = new ArrayList<>();
        this.placedComponents = new ArrayList<>();
        this.resourceSheets = new ArrayList<>();
        this.players = new ArrayList<>();
    }

    public Game(ArrayList<Card> cards, ArrayList<Dice> diceCollection, ArrayList<Piece> pieces, Board board){
        this.cards = cards;
        this.diceCollection = diceCollection;
        this.pieces = pieces;
        this.board = board;
        this.textures = new ArrayList<>();
        this.placedComponents = new ArrayList<>();
        this.resourceSheets = new ArrayList<>();
        this.players = new ArrayList<>();
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

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

//    public void placeCard(Card card,Point point){
//        this.placedComponents.put(card.copy(),point);
//    }

    public ArrayList<DrawerInterface> getPlacedComponents(){
        return this.placedComponents;
    }

    public void removePlacedComponent(DrawerInterface drawer){
        this.placedComponents.remove(drawer);
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

    public void removeComponent(GameComponent component){
        if(component instanceof Texture){
            textures.remove(component);
        }else if(component instanceof  Piece){
            pieces.remove(component);
        }else if(component instanceof Card){
            cards.remove(component);
        }else if(component instanceof Deck){
            decks.remove(component);
        }else if(component instanceof ResourceSheet){
            resourceSheets.remove(component);
        }else if(component instanceof Player){
            players.remove(component);
        }
    }

    public void addComponent(GameComponent component){
        if(component instanceof Texture){
            textures.add((Texture)component);
        }else if(component instanceof  Piece){
            pieces.add((Piece)component);
        }else if(component instanceof Card){
            cards.add((Card)component);
        }else if(component instanceof Deck){
            decks.add((Deck)component);
        }else if(component instanceof ResourceSheet){
            resourceSheets.add((ResourceSheet)component);
        }else if(component instanceof Player){
            players.add((Player)component);
        }
    }

    public void addComponent(GameComponent component, int index){
        if(component instanceof Texture){
            textures.add(index,(Texture)component);
        }else if(component instanceof  Piece){
            pieces.add(index,(Piece)component);
        }else if(component instanceof Card){
            cards.add(index,(Card)component);
        }else if(component instanceof Deck){
            decks.add(index,(Deck)component);
        }else if(component instanceof ResourceSheet){
            resourceSheets.add(index,(ResourceSheet)component);
        }else if(component instanceof Player){
            players.add(index, (Player)component);
        }
    }

    public int getComponentIndex(GameComponent component){
        if(component instanceof Texture){
            return textures.indexOf(component);
        }else if(component instanceof  Piece){
            return pieces.indexOf(component);
        }else if(component instanceof Card){
            return cards.indexOf(component);
        }else if(component instanceof Deck){
            return decks.indexOf(component);
        }else if(component instanceof ResourceSheet){
            return resourceSheets.indexOf(component);
        }else if(component instanceof Player){
            return players.indexOf(component);
        }
        return -1;
    }

    public ArrayList<ResourceSheet> getResources(){
        return this.resourceSheets;
    }

    public ResourceSheet getResource(String name){
        for(ResourceSheet resourceSheet : this.resourceSheets){
            if(resourceSheet.getName().equals(name)){
                return resourceSheet;
            }
        }

        return null;
    }

    public void placeComponent(DrawerInterface drawer){
        this.placedComponents.add(drawer);
    }

    public void addPlayer(Player player){
        this.players.add(player);
    }

    public ArrayList<Player> getPlayers(){
        return this.players;
    }

    public Player getPlayer(String name){
        for(Player player: this.players){
            if(player.getName().equals(name)){
                return player;
            }
        }
        return null;
    }

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
}
