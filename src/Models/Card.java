package Models;

import java.io.File;
import java.io.Serializable;

public class Card implements Serializable {
    private String name;
    private String text;
    private File image;

    public Card(String name, String text, String filename){
        this.name = name;
        this.image = new File(filename);
        this.text = text;
    }

    public String getName(){
        return name;
    }

    public String getText(){
        return text;
    }

    public File getImage(){
        return image;
    }


}
