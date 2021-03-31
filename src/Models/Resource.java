package Models;

public class Resource {
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