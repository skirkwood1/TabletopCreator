package Models;

import java.io.Serializable;

public class Resource implements Serializable {
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

    public String getName(){
        return name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String toString(){
        return String.format("Resource Name: %s \n\r Starting value: %d",name,value);
    }
}
