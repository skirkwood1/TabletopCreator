package Models;

import java.awt.image.BufferedImage;
import java.io.Serializable;

public class Resource implements Serializable,GameComponent {
    private static final long serialVersionUID = 8607053040307709847L;
    private String name;
    private int value;
    private final int startValue;

    public Resource(String name){
        this.name = name;
        this.startValue = 0;
        this.value = 0;
    }

    public Resource(String name,int value){
        this.name = name;
        this.startValue = value;
        this.value = value;
    }

    public Resource(String name,int value,int startValue){
        this.name = name;
        this.startValue = startValue;
        this.value = value;
    }

    public String getName(){
        return name;
    }

    public int getValue() {
        return value;
    }

    public String getText(){
        return "Starting value: " + startValue;
    }

    public BufferedImage getImage(){
        return null;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void incrementValue(){
        this.value++;
    }

    public void decrementValue(){
        this.value--;
    }

    public Resource copy(){
        return new Resource(this.name,this.value,this.startValue);
    }

    public String toString(){
        return String.format("Resource: %s; Start Value: %d",name,value);
    }
}
