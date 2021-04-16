package Models;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class Piece implements Serializable,Component {
    private static final long serialVersionUID = -497535162892637357L;

    private String name,text;

    private BufferedImage image;

    public Piece(String name, String text, BufferedImage image){
        this.name = name;
        this.text = text;
        this.image = image;
    }

    public Piece(String name, String text, String filename){
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

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        image = ImageIO.read(in);
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        ImageIO.write(image, "png", out); // png is lossless
    }
}