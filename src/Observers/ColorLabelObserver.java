package Observers;

import UI.Toolbar;

public class ColorLabelObserver implements Observer {
    Toolbar toolbar;

    public ColorLabelObserver(Toolbar toolbar){
        this.toolbar = toolbar;
    }

    public void update(){
        this.toolbar.updateColorLabel();
    }
}
