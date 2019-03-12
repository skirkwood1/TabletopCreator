package UI;

import Models.Dice;

public class GUI {
    public static void main(String[] args){
        Dice hex = new Dice(6);
        Dice oct = new Dice(8);
        for(int i = 0; i<50; i++){
            System.out.println(Dice.rollCombo(hex,oct));
        }

    }
}
