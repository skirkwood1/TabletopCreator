package Observers;

import UI.BoardPane;
import UI.CenterPane;

public class PlacementTypeObserver implements Observer {
    private CenterPane centerPane;

    public PlacementTypeObserver(CenterPane centerPane){
        this.centerPane = centerPane;
    }

    public void update(){
        if(centerPane.willPlace()){
            centerPane.getBoardPane().setPlacementType(centerPane.getPlacementType());
        }else{
            centerPane.getBoardPane().setPlacementType(BoardPane.PlacementType.NONE);
        }
    }
}
