package Observers;

import UI.BoardPane;
import UI.CenterPane;

public class BoardPaneObserver implements Observer {
    CenterPane centerPane;

    public BoardPaneObserver(CenterPane centerPane){
        this.centerPane = centerPane;
    }

    public void update(){
        centerPane.updateBoard();
    }
}
