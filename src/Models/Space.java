package Models;

import java.awt.*;
import java.io.Serializable;

public class Space implements Serializable {

    public enum Shape{
        SQUARE,HEX
    }

    //private boolean empty = true;
    //private Shape shape = Shape.SQUARE;
    private Color color;

    public Space(){
        this.color = Color.GRAY;
    }

    public Space(Color color){
        this.color = color;
        //this.empty = false;
    }

    public Color getColor(){
        return color;
    }

    public String toString(){
        return "R: " + color.getRed() + "\t G: " + color.getGreen() + "\tB: " + color.getBlue() + "\t| ";
    }
}
