package Models;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Texture {
    private BufferedImage texture;
    private String name;

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
}
