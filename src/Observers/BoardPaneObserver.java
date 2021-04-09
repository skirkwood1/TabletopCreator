package Observers;

import UI.BoardPane;
import UI.CenterPane;

public class BoardPaneObserver extends Observer {
    CenterPane centerPane;

    public BoardPaneObserver(CenterPane centerPane){
        this.centerPane = centerPane;
    }

    public void update(){
        centerPane.updateBoard();
    }
}
