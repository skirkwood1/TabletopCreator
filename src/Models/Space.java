package Models;

import java.awt.*;
import java.io.Serializable;

public class Space implements Serializable {

    public enum Shape{
        SQUARE,HEX
    }

    //private boolean empty = true;
    //private Shape shape = Shape.SQUARE;
    private Color color;
    private boolean occupied;
    private Piece piece = null;

    public Space(){
        this.color = Color.GRAY;
        this.occupied = false;
    }

    public Space(Color color){
        this.color = color;
        this.occupied = false;
        //this.empty = false;
    }

    public Color getColor(){
        return color;
    }

    public String toString(){
        return "R: " + color.getRed() + "\t G: " + color.getGreen() + "\tB: " + color.getBlue() + "\t| ";
    }

    public void addPiece(Piece piece){
        this.piece = piece;
        this.occupied = true;
    }

    public void removePiece(){
        this.piece = null;
        this.occupied = false;
    }

    public Piece getPiece(){
        return this.piece;
    }

    public boolean isOccupied(){
        return this.occupied;
    }

    public void setColor(Color color){
        this.color = color;
    }
}
