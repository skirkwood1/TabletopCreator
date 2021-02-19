package Models;

import java.io.File;

public class Component {

    private String name;
    private String text;
    private File image;

    public Component(String name, String text, String filename){
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
