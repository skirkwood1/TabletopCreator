package Models;

import java.awt.image.BufferedImage;
import java.io.*;

public class Piece implements Serializable, GameComponent {
    private static final long serialVersionUID = -497535162892637357L;

    private String name,text;

    private ComponentImage image;

    public Piece(String name, String text, ComponentImage image){
        this.name = name;
        this.text = text;
        this.image = image;
    }

    public String getName(){
        return this.name;
    }

    public String getText(){
        return this.text;
    }

    public BufferedImage getImage(){
        return this.image.getImage();
    }

    public ComponentImage getComponentImage(){
        return this.image;
    }

    public String toString(){
        return getName();
    }

    public void setName(String name){
        this.name = name;
    }
}