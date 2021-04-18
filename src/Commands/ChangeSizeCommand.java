package Commands;

import Models.Game;

/* Change the size of the board to the new desired width and height.
* Spaces that already had a color/texture/piece applied will remain in the new board
* Saves the old width and height for undo
* */
public class ChangeSizeCommand implements GameCommand {
    private int desiredWidth,desiredHeight;
    private int oldWidth,oldHeight;

    private Game game;

    private int[] oldMargins;
    private int[] margins;

    public ChangeSizeCommand(Game game, int desiredWidth, int desiredHeight){
        //this.memento = new GameMemento();
        this.game = game;

        this.desiredWidth = desiredWidth;
        this.desiredHeight = desiredHeight;

        this.oldMargins = game.getBoard().getMargins();
        this.margins = null;
    }

    public ChangeSizeCommand(Game game, int desiredWidth, int desiredHeight, int[] margins){
        this.game = game;
        this.desiredWidth = desiredWidth;
        this.desiredHeight = desiredHeight;

        this.oldMargins = game.getBoard().getMargins();
        this.margins = margins;
    }

    public void execute(){
        this.oldWidth = game.getBoard().getSize()[0];
        this.oldHeight = game.getBoard().getSize()[1];
        //this.memento.setState(this.game);

        game.getBoard().setSize(desiredWidth,desiredHeight);

        if(this.margins != null){
            game.getBoard().setMargins(this.margins);
        }
    }

    public void unExecute(){
        //game = memento.getState();

        game.getBoard().setSize(oldWidth,oldHeight);
        game.getBoard().setMargins(oldMargins);

        System.out.println(game);
    }

    public String toString(){
        return String.format("Changed board size and margins from: \n\r\t" +
                "Width: %d, Height: %d, Top: %d, Bottom: %d, Left: %d, Right: %d to \n\r\t" +
                "Width: %d, Height: %d, Top: %d, Bottom: %d, Left: %d, Right: %d",
                oldWidth,oldHeight,oldMargins[0],oldMargins[1],oldMargins[2],oldMargins[3],
                desiredWidth,desiredHeight,margins[0],margins[1],margins[2],margins[3]);
    }
}
