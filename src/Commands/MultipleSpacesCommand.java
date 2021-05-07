package Commands;

import Models.Game;
import Models.Texture;

import java.awt.*;
import java.awt.image.BufferedImage;

/* Assigns color/texture to multiple new squares at a time to the board
* Saves arrays for each square containing their textures and/or colors
* */
public class MultipleSpacesCommand implements GameCommand {
    int start_x, start_y, end_x, end_y;

    private Game game;

    private final Color[][] oldColors;
    private final Texture[][] oldTextures;
    private final boolean[][] usedTexture;

    private final boolean useTexture;
    private Color newColor;
    private Texture newTexture;

    public MultipleSpacesCommand(Game game,int start_x,int start_y,int end_x, int end_y){
        this.game = game;
        this.start_x = start_x;
        this.start_y = start_y;
        this.end_x = end_x;
        this.end_y = end_y;

        if(game.getBoard().useTexture()){
            this.useTexture = true;
            this.newTexture = game.getBoard().getTexture();
        }else{
            this.useTexture = false;
            this.newColor = game.getBoard().getColor();
        }

        this.oldColors = new Color[end_x - start_x + 1][end_y - start_y + 1];
        this.oldTextures = new Texture[end_x - start_x + 1][end_y - start_y + 1];
        this.usedTexture = new boolean[end_x - start_x + 1][end_y - start_y + 1];

        for(int i = 0; i <= end_x - start_x; i++){
            for(int j = 0; j <= end_y - start_y; j++){
                oldColors[i][j] = game.getBoard().getSpace(i + start_x,j + start_y).getColor();
                usedTexture[i][j] = game.getBoard().getSpace(i + start_x,j + start_y).isUsingTexture();

                if(usedTexture[i][j]){
                    oldTextures[i][j] = game.getBoard().getSpace(i + start_x,j + start_y).getTexture();
                }
            }
        }
    }

    @Override
    public void execute() {
        for(int i = start_x; i <= end_x; i++){
            for(int j = start_y; j <= end_y; j++){
                if(useTexture){
                    game.getBoard().setSquare(i,j,newTexture);
                }else{
                    game.getBoard().setSquare(i,j,newColor);
                }
            }
        }
    }

    @Override
    public void unExecute() {
        for(int i = start_x; i <= end_x; i++){
            for(int j = start_y; j <= end_y; j++){
                if(usedTexture[i-start_x][j-start_y]){
                    game.getBoard().setSquare(i,j,oldTextures[i - start_x][j - start_y]);
                }
                else{
                    game.getBoard().setSquare(i,j,oldColors[i - start_x][j - start_y]);
                }
            }
        }
    }

    public String toString(){
        if(useTexture){
            return String.format("Placed spaces of texture %s from (%d,%d) to (%d,%d)",
                    newTexture,start_x,start_y,end_x,end_y);
        }else{
            return String.format("Placed spaces of color [%d,%d,%d] from (%d,%d) to (%d,%d)",
                    newColor.getRed(),newColor.getGreen(),newColor.getBlue(),
                    start_x,start_y,end_x,end_y);
        }
    }
}
