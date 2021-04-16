package Models;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class Texture implements Serializable, Component {
    private static final long serialVersionUID = 5757975696680667974L;

    private String name,text;

    private transient BufferedImage image;

    public Texture(String name, String description, BufferedImage texture){
        this.name = name;
        this.text = text;
        this.image = image;
    }

    public Texture(String name, String description, String filename){
        this.name = name;
        //this.image = new File(filename);
        this.text = text;

        try{
            this.image = ImageIO.read(new File(filename));}
        catch(IOException e){

        }
    }

    public String getName(){
        return this.name;
    }

    public String getText(){
        return this.text;
    }

    public BufferedImage getImage(){
        return this.image;
    }

    public String toString(){
        return getName();
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        ImageIO.write(image, "png", out); // png is lossless
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        image = ImageIO.read(in);
    }
}
