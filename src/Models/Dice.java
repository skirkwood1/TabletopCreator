package Models;

import java.util.ArrayList;
import java.util.Random;

public class Dice {

    private int sides;

    public Dice(int sides){
        this.sides = sides;
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
