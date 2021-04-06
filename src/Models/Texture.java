package Models;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class Texture implements Serializable {

    private String name;

    private transient BufferedImage texture;

    public Texture(String name, BufferedImage texture){
        this.texture = texture;
        this.name = name;
    }

    public Texture(String name, String filename){
        try{
            this.texture = ImageIO.read(new File(filename));}
        catch(IOException e){

        }
        this.name = name;
    }

    public BufferedImage getTexture(){
        return this.texture;
    }

    public String getName(){
        return this.name;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        ImageIO.write(texture, "png", out); // png is lossless
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        texture = ImageIO.read(in);
    }
}
