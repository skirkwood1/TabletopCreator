package Models;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

/*Represents an individual list of cards. The project has a set of cards,
* but they can be separated into decks which can contain multiple copies
* of each card for use in separate parts of the game
* */
public class Deck extends CardInterface implements Serializable, GameComponent {
    private static final long serialVersionUID = -1499205425401974194L;
    private String name;
    private ArrayList<Card> cards;

    private ComponentImage cardBack;
    private boolean flipped;

    public Deck(String name){
        this.name = name;
        this.cards = new ArrayList<>();
        this.flipped = true;
        try{
            this.cardBack = new ComponentImage(ImageIO.read(getClass().getClassLoader().getResource("cardback.png")));
        }catch(IOException i){

        }
    }

    public Deck(String name,Card ... cards){
        this.name = name;
        this.cards = new ArrayList<>();
        this.flipped = true;

        Collections.addAll(this.cards, cards);
    }

    public Deck(String name,ArrayList<Card> cards){
        this.name = name;
        this.cards = cards;
        this.flipped = true;

        try{
            this.cardBack = new ComponentImage(ImageIO.read(getClass().getClassLoader().getResource("cardback.png")));
        }catch(IOException i){

        }
    }

    public Deck(String name,ArrayList<Card> cards, ComponentImage cardBack){
        this.name = name;
        this.cards = cards;
        this.cardBack = cardBack;
        this.flipped = true;
    }

    public void shuffle(){
        Collections.shuffle(cards);
    }

    public Card drawCard(){
        Card card = this.cards.get(0);
        this.cards.remove(0);
        return card;
    }

    public Deck copy(){
        ArrayList<Card> cardCopies = new ArrayList<>();

        for(Card card: this.cards){
            cardCopies.add(card.copy());
        }
        return new Deck(this.name,cardCopies,cardBack);
    }

    public void addCard(Card card){
        this.cards.add(card);
    }

    public void addCard(Card card,int n){
        for(int i = 0; i < n; i++){
            this.cards.add(card);
        }
    }

    public void setCards(ArrayList<Card> cards){
        this.cards = cards;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public String getText(){
        return "";
    }

    public String toString(){
        return getName();
    }

    public ArrayList<Card> getCards(){
        return this.cards;
    }

    public BufferedImage getImage(){
        if(flipped){
            return this.cardBack.getImage();
        }else{
            return this.cards.get(0).getImage();
        }
    }

    public void flip(){
        this.flipped = !flipped;
    }

    @Override
    public boolean isFlipped() {
        return flipped;
    }
}
