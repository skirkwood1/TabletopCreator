package Models;

import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;

public class Player implements Serializable, GameComponent {
    private static final long serialVersionUID = 6607801587212017657L;

    private String name;
    private ArrayList<Card> hand;
    private ArrayList<Piece> controlledPieces;
    private ArrayList<ResourceSheet> resourceSheets;

    public Player(String name){
        this.name = name;
        this.hand = new ArrayList<>();
        this.controlledPieces = new ArrayList<>();
        this.resourceSheets = new ArrayList<>();
    }

    public Player(String name, ArrayList<ResourceSheet> resourceSheets){
        this.name = name;
        this.hand = new ArrayList<>();
        this.controlledPieces = new ArrayList<>();
        this.resourceSheets = resourceSheets;
    }

    public Player(String name,ArrayList<Card> hand,ArrayList<Piece> controlledPieces,ArrayList<ResourceSheet> resourceSheets){
        this.name = name;
        this.hand = hand;
        this.controlledPieces = controlledPieces;
        this.resourceSheets = resourceSheets;
    }

    public void addCard(Card card){
        this.hand.add(card);
    }

    public void discard(Card card){
        this.hand.remove(card);
    }

    public void discard(int index){
        this.hand.remove(index);
    }

    public void removePiece(Piece piece){
        this.controlledPieces.remove(piece);
    }

    public void removePiece(int index){
        this.controlledPieces.remove(index);
    }

    public Piece getPiece(int index){
        return this.controlledPieces.get(index);
    }

    public void addPiece(Piece piece){
        this.controlledPieces.add(piece);
    }

    public String getName(){
        return this.name;
    }

    @Override
    public String getText() {
        return "Player: " + this.name + "\n\r # of Resources: " + resourceSheets.size();
    }

    @Override
    public BufferedImage getImage() {
        return null;
    }

    public int getResourceValue(String name, String resName){
        for(ResourceSheet resourceSheet : resourceSheets){
            if(resourceSheet.getName().equals(name)){
                return resourceSheet.getValue(resName);
            }
        }
        return 0;
    }

    public void setResource(String name, String resName, int value){
        for(ResourceSheet resourceSheet : this.resourceSheets){
            if(resourceSheet.getName().equals(name)){
                resourceSheet.setValue(resName, value);
            }
        }
    }

    public void addResource(ResourceSheet resourceSheet){
        this.resourceSheets.add(resourceSheet);
    }

    public ArrayList<ResourceSheet> getResources(){
        return this.resourceSheets;
    }

    public ArrayList<Piece> getPieces(){
        return this.controlledPieces;
    }

    public ArrayList<Card> getCards(){
        return this.hand;
    }

    public Player copy(){
        ArrayList<ResourceSheet> copiedResourceSheets = new ArrayList<>();
        for(ResourceSheet resourceSheet : this.resourceSheets){
            copiedResourceSheets.add(resourceSheet.copy());
        }
        return new Player(this.name,this.hand,this.controlledPieces, copiedResourceSheets);
    }

    public String toString(){
        return String.format("Player: %s",name);
    }

    public void setName(String name){
        this.name = name;
    }
}
