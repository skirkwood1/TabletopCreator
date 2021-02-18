package Models;

import java.awt.*;
import java.io.Serializable;
import java.util.Random;

public class Board implements Serializable {

    private int height;
    private int width;
    private Space[][] spaces;
    private Random random = new Random();

    public Board(){
        this.width = 4;
        this.height = 4;
        this.spaces = new Space[width][height];

        for (int i = 0; i < width; i++){
            for (int j = 0; j < height; j++){
                spaces[i][j] = new Space();
            }
        }
    }

    public Board(int width, int height){
        this.width = width;
        this.height = height;
        this.spaces = new Space[width][height];

        for (int i = 0; i < width; i++){
            for (int j = 0; j < height; j++){
                Color color = new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255));
                spaces[i][j] = new Space(color);
            }
        }
    }

    public Space[][] getSpaces(){
        return this.spaces;
    }

    public Space getSpace(int x, int y){
        return this.spaces[x][y];
    }

    public int[] getSpaceLocation(int x, int y){
        int[] coordinates = new int[2];
        coordinates[0] = x*40;
        coordinates[1] = y*40;

        return coordinates;
    }

    public int[] getSize(){
        int[] size = new int[2];
        size[0] = width;
        size[1] = height;
        return size;
    }

    public void setSquare(int x, int y, Color color){
        spaces[x][y] = new Space(color);
    }

    public String toString(){
        String str = "";
        for(int i = 0; i < width; i++){
            for(int j = 0; j < width; j++){
                str += spaces[i][j].toString() + " ";
            }
            str += "\n";
        }
        return str;
    }
}
