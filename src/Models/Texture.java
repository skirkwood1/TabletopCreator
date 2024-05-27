package Models;

import java.awt.image.BufferedImage;
import java.io.*;

public class Texture implements Serializable, GameComponent {
    private static final long serialVersionUID = 5757975696680667974L;

    private String name,text;

    private ComponentImage image;

    public Texture(String name, String description, ComponentImage texture){
        this.name = name;
        this.text = text;
        this.image = texture;
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
