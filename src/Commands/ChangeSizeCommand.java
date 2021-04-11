package Commands;

import Models.Game;

/* Change the size of the board to the new desired width and height.
* Spaces that already had a color/texture/piece applied will remain in the new board
* Saves the old width and height for undo
* */
public class ChangeSizeCommand extends GameCommand {
    private int desiredWidth,desiredHeight;
    private int oldWidth,oldHeight;

    public ChangeSizeCommand(Game game, int desiredWidth, int desiredHeight){
        //this.memento = new GameMemento();
        this.game = game;

        this.desiredWidth = desiredWidth;
        this.desiredHeight = desiredHeight;
    }

    public void execute(){
        this.oldWidth = game.getBoard().getSize()[0];
        this.oldHeight = game.getBoard().getSize()[1];

        //this.memento.setState(this.game);

        game.getBoard().setSize(desiredWidth,desiredHeight);
    }

    public void unExecute(){
        //game = memento.getState();

        game.getBoard().setSize(oldWidth,oldHeight);

        System.out.println(game);
    }
}
