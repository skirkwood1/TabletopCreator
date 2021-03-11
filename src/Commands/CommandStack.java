package Commands;

import Models.Game;

import java.util.Stack;

public class CommandStack {
    private int pointer = -1;
    private Stack<GameCommand> commandStack = new Stack<>();

    public void insertCommand(GameCommand gc){
        deleteElementsAfterPointer(pointer);
        gc.execute();
        commandStack.push(gc);
        pointer++;
    }

    private void deleteElementsAfterPointer(int pointer)
    {
        if(commandStack.size()<1)return;
        for(int i = commandStack.size()-1; i > pointer; i--)
        {
            commandStack.remove(i);
        }
    }

    public void undo()
    {
        GameCommand command = commandStack.get(pointer);
        command.unExecute();
        pointer--;

        System.out.println("Undid command " + command.toString() + "\n\r Pointer set to + " + pointer);
    }

    public void redo()
    {
        if(pointer == commandStack.size() - 1)
            return;
        pointer++;
        GameCommand command = commandStack.get(pointer);
        command.execute();


    }

    public void clear(){
        commandStack.clear();
        pointer = -1;
    }


}
