package Models;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class Dice implements Serializable {

    private static final long serialVersionUID = 4915119969397691077L;
    private int sides;

    public static ArrayList<Dice> dieList = new ArrayList<>();

    public Dice(int sides){
        this.sides = sides;
        dieList.add(this);
    }

    public int roll(){
        Random i = new Random();
        return i.nextInt(sides) + 1;
    }

    public static int rollCombo(Dice... dice){
        int total = 0;
        for(Dice die: dice){
            total += die.roll();
        }
        return total;
    }


}
