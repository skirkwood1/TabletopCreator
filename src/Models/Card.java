package Models;

import java.awt.image.BufferedImage;
import java.io.*;

public class Card extends CardInterface implements Serializable, GameComponent {

    private static final long serialVersionUID = 469671549044569183L;

    private String name,text;
    private ComponentImage image;
    private ComponentImage cardBack;

    private boolean flipped;

    public Card(String name, String text, ComponentImage image){
        this.name = name;
        this.text = text;
        this.image = image;
        this.flipped = false;
    }

    public Card(String name, String text, ComponentImage image, ComponentImage cardBack){
        this.name = name;
        this.text = text;
        this.image = image;
        this.cardBack = cardBack;
        this.flipped = false;
    }

    public Card copy(){
        return new Card(this.getName(),this.getText(),this.getComponentImage(),this.cardBack);
    }

    public String getName(){
        return this.name;
    }

    public String getText(){
        return this.text;
    }

    public ComponentImage getComponentImage(){
        if(flipped) return this.cardBack;
        return this.image;
    }

    public BufferedImage getImage(){
        if(flipped) return this.cardBack.getImage();
        return this.image.getImage();
    }

    public String toString(){
        return getName();
    }

    @Override
    public void flip() {
        this.flipped = !flipped;
    }

    @Override
    public boolean isFlipped() {
        return flipped;
    }
}
