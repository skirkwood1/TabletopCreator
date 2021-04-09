package Models;

import java.io.Serializable;
import java.util.ArrayList;

public class Player implements Serializable {
    private ArrayList<Card> hand;
    private ArrayList<Piece> controlledPieces;
    private ArrayList<Resource> resources;

    public Player(String ... resources){
        this.hand = new ArrayList<>();
        this.controlledPieces = new ArrayList<>();
        this.resources = new ArrayList<>();

        for(String resource: resources){
            this.resources.add(new Resource(resource));
        }
    }

    public Player(ArrayList<Resource> resources){
        this.hand = new ArrayList<>();
        this.controlledPieces = new ArrayList<>();
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

    public ArrayList<Resource> getResources(){
        return this.resources;
    }

    public ArrayList<Piece> getPieces(){
        return this.controlledPieces;
    }

    public ArrayList<Card> getCards(){
        return this.hand;
    }
}
