package Models;

import java.io.Serializable;

public class Resource implements Serializable {
    private static final long serialVersionUID = 8607053040307709847L;
    private String name;
    private int value;

    public Resource(String name){
        this.name = name;
        this.value = 0;
    }

    public Resource(String name,int value){
        this.name = name;
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
}
