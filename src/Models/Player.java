package Models;

import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;

public class Player implements Serializable, GameComponent {
    private static final long serialVersionUID = 6607801587212017657L;

    private String name;
    private ArrayList<Card> hand;
    private ArrayList<Piece> controlledPieces;
    private ArrayList<Resource> resources;

    public Player(String name){
        this.name = name;
        this.hand = new ArrayList<>();
        this.controlledPieces = new ArrayList<>();
        this.resources = new ArrayList<>();
    }

    public Player(String name, ArrayList<Resource> resources){
        this.name = name;
        this.hand = new ArrayList<>();
        this.controlledPieces = new ArrayList<>();
        this.resources = resources;
    }

    public Player(String name,ArrayList<Card> hand,ArrayList<Piece> controlledPieces,ArrayList<Resource> resources){
        this.name = name;
        this.hand = hand;
        this.controlledPieces = controlledPieces;
        this.resources = resources;
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
        return "Player: " + this.name + "\n\r # of Resources: " + resources.size();
    }

    @Override
    public BufferedImage getImage() {
        return null;
    }

    public int getResourceValue(String name){
        for(Resource resource:resources){
            if(resource.getName().equals(name)){
                return resource.getValue();
            }
        }
        return 0;
    }

    public void setResource(String name, int value){
        for(Resource resource: this.resources){
            if(resource.getName().equals(name)){
                resource.setValue(value);
            }
        }
    }

    public void addResource(Resource resource){
        this.resources.add(resource);
    }

    public ArrayList<Resource> getResources(){
        return this.resources;
    }

    public ArrayList<Piece> getPieces(){
        return this.controlledPieces;
    }

    public ArrayList<Card> getCards(){
        return this.hand;
    }

    public Player copy(){
        ArrayList<Resource> copiedResources = new ArrayList<>();
        for(Resource resource: this.resources){
            copiedResources.add(resource.copy());
        }
        return new Player(this.name,this.hand,this.controlledPieces,copiedResources);
    }
}
