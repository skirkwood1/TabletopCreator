package Commands;

import Models.Game;
import Models.GameComponent;

public class RenameComponentCommand implements GameCommand {

    private final GameComponent component;
    private final String oldName;
    private final String newName;

    public RenameComponentCommand(GameComponent component, String name){
        this.component = component;
        this.oldName = component.getName();
        this.newName = name;
    }

    @Override
    public void execute() {
        component.setName(newName);
    }

    @Override
    public void unExecute() {
        component.setName(oldName);
    }

    public String toString(){
        return "Renamed component '" + oldName + "' to '" + newName + "'";
    }
}
