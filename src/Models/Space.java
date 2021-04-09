package Models;

import java.awt.*;
import java.awt.image.BufferedImage;
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

    private boolean useTexture;
    private Texture texture;

    public Space(){
        this.color = Color.GRAY;
        this.occupied = false;

        this.useTexture = false;
    }

    public Space(Color color){
        this.color = color;
        this.occupied = false;

        this.useTexture = false;
        //this.empty = false;
    }

    public Space(Texture image){
        this.texture = image;
        this.occupied = false;

        this.useTexture = true;
    }

    public Color getColor(){
        return color;
    }

    public Texture getTexture(){
        return texture;
    }

    public boolean isUsingTexture(){
        return useTexture;
    }

    public String toString(){
        if(useTexture){
            return "Texture: \t" + texture.getName() + "\t\t| ";
        }else{
            return "R: " + color.getRed() + "\t G: " + color.getGreen() + "\tB: " + color.getBlue() + "\t| ";
        }
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
        this.texture = null;
        this.useTexture = false;
    }

    public void setTexture(Texture image){
        this.texture = image;
        this.useTexture = true;
    }
}
