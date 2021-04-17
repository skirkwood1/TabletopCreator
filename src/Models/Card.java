package Models;

import java.awt.image.BufferedImage;
import java.io.*;

public class Card implements Serializable, GameComponent,CardInterface {

    private static final long serialVersionUID = 469671549044569183L;

    private String name,text;
    private ComponentImage image;

    public Card(String name, String text, ComponentImage image){
        this.name = name;
        this.text = text;
        this.image = image;
    }

    public Card copy(){
        return new Card(this.getName(),this.getText(),this.getComponentImage());
    }

    public String getName(){
        return this.name;
    }

    public String getText(){
        return this.text;
    }

    public ComponentImage getComponentImage(){
        return this.image;
    }

    public BufferedImage getImage(){
        return this.image.getImage();
    }

    public String toString(){
        return getName();
    }

}
