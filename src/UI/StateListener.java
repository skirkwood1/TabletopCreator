package UI;

public interface StateListener {
    //void textEmitted(String text);

    void stateEmitted(ButtonOutput state);

    enum ButtonOutput{
        SAVE,OPEN,COLOR_CHOOSE,ADD_PIECE,ADD_CARD,
        ADD_RULE,ADD_TEXTURE,CREATE_DECK,ADD_DECK,
        MESSAGE,CHANGE_SIZE,UNDO,REDO,PLACE,
        ADD_RESOURCE,MOVE,ADD_PLAYER
    }
}
