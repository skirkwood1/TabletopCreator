package Models;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class ComponentImage implements Serializable {
    private BufferedImage image;

    public ComponentImage(BufferedImage image){
        this.image = image;
    }

    public ComponentImage(String filename){
        try{
            this.image = ImageIO.read(new File(filename));}
        catch(IOException e){

        }
    }

    public BufferedImage getImage(){
        return this.image;
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        image = ImageIO.read(in);
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        ImageIO.write(image, "png", out); // png is lossless
    }
}
