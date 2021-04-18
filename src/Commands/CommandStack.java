package Commands;

import Models.Game;
import UI.CommandLog;

import java.util.Stack;

public class CommandStack {
    private int pointer = -1;
    private Stack<GameCommand> commandStack = new Stack<>();

    CommandLog log;

    public void setLog(CommandLog log){
        this.log = log;
    }

    // Adds a command to the stack at the pointer and executes it.
    // Removes all commands past the pointer
    public void insertCommand(GameCommand gc){
        deleteElementsAfterPointer(pointer);
        gc.execute();
        commandStack.push(gc);
        log.appendText(gc.toString());
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

    // Gets the command at the pointer and calls unExecute(),
    // Moves the pointer to the previous command
    public void undo()
    {
        if(pointer >= 0){
            GameCommand command = commandStack.get(pointer);
            command.unExecute();
            pointer--;

            log.appendText(String.format("Undid command: \"%s\"",command.toString()));
        }
    }

    // If the pointer is not at the top of the stack,
    // increment the pointer and execute that command
    public void redo()
    {
        if(pointer == commandStack.size() - 1)
            return;
        pointer++;
        GameCommand command = commandStack.get(pointer);
        command.execute();
        log.appendText(String.format("Redid command: \"%s\"",command.toString()));
    }

    public void clear(){
        commandStack.clear();
        pointer = -1;
    }
}