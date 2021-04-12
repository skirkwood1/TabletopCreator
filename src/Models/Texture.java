package Models;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class Texture extends Component implements Serializable {

//    private String name;
//
//    private transient BufferedImage picture;

    public Texture(String name, String description, BufferedImage texture){
        super(name,description,texture);
    }

    public Texture(String name, String description, String filename){
        super(name,description,filename);
    }

    //public String getName(){
    //    return this.name;
    //}

//    private void writeObject(ObjectOutputStream out) throws IOException {
//        out.defaultWriteObject();
//        ImageIO.write(picture, "png", out); // png is lossless
//    }

//    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
//        in.defaultReadObject();
//        picture = ImageIO.read(in);
//    }
}
