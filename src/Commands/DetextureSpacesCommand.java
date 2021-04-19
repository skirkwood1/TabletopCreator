package Commands;

import Models.Game;
import Models.Space;
import Models.Texture;

import java.awt.*;
import java.util.ArrayList;

public class DetextureSpacesCommand implements GameCommand {

    private Game game;
    private ArrayList<Space> spaces;
    private ArrayList<Texture> textures;
    private ArrayList<Color> colors;

    public DetextureSpacesCommand(Game game, ArrayList<Space> spaces){
        this.game = game;
        this.spaces = spaces;
        this.textures = new ArrayList<>();
        this.colors = new ArrayList<>();

        for(Space space:spaces){
            textures.add(space.getTexture());
            colors.add(space.getColor());
        }
    }

    @Override
    public void execute() {
        for(int i = 0; i < spaces.size(); i++){
            spaces.get(i).setColor(colors.get(i));
        }
    }

    @Override
    public void unExecute() {
        for(int i = 0; i < spaces.size(); i++){
            spaces.get(i).setTexture(textures.get(i));
        }
    }

    public String toString(){
        return "Recolored tiles of deleted texture " + textures.get(0);
    }
}
