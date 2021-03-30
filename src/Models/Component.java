package Models;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class Component implements Serializable {

    private String name;
    private String text;
    //private File image;
    private transient BufferedImage picture;


    public Component(String name, String text, String filename){
        this.name = name;
        //this.image = new File(filename);
        this.text = text;

        try{
            this.picture = ImageIO.read(new File(filename));}
        catch(IOException e){

        }
    }

    public String getName(){
        return name;
    }

    public String getText(){
        return text;
    }

//    public File getImage(){
//        return image;
//    }

    public BufferedImage getPicture(){
        return picture;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        ImageIO.write(picture, "png", out); // png is lossless
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        picture = ImageIO.read(in);

    }

    public String toString(){
        return this.getName();
    }

}
