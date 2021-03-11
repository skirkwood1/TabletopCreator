package Commands;

import java.util.Stack;

public class CommandStack {
    private static int pointer = -1;
    private static Stack<GameCommand> commandStack = new Stack<>();

    public static void insertCommand(GameCommand gc){
        deleteElementsAfterPointer(pointer);
        gc.execute();
        commandStack.push(gc);
        pointer++;

    }

    private static void deleteElementsAfterPointer(int undoRedoPointer)
    {
        if(commandStack.size()<1)return;
        for(int i = commandStack.size()-1; i > undoRedoPointer; i--)
        {
            commandStack.remove(i);
        }
    }

    public static void undo()
    {
        GameCommand command = commandStack.get(pointer);
        command.unExecute();
        pointer--;
    }

    public static void redo()
    {
        if(pointer == commandStack.size() - 1)
            return;
        pointer++;
        GameCommand command = commandStack.get(pointer);
        command.execute();
    }


}
