package Commands;

import Models.Game;

import java.awt.*;
import java.awt.image.BufferedImage;

public class MultipleSpacesCommand extends GameCommand {
    int start_x, start_y, end_x, end_y;

    Color[][] oldColors;
    BufferedImage[][] oldTextures;
    boolean[][] usedTexture;

    Color newColor;

    public MultipleSpacesCommand(Game game,int start_x,int start_y,int end_x, int end_y){
        this.game = game;
        this.start_x = start_x;
        this.start_y = start_y;
        this.end_x = end_x;
        this.end_y = end_y;

        this.oldColors = new Color[end_x - start_x + 1][end_y - start_y + 1];
        this.oldTextures = new BufferedImage[end_x - start_x + 1][end_y - start_y + 1];
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
                if(game.getBoard().useTexture()){
                    game.getBoard().setSquare(i,j,this.game.getBoard().getTextureImage());
                }else{
                    game.getBoard().setSquare(i,j,this.game.getBoard().getColor());
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
}
