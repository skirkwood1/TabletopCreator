package UI.UIHelpers;

import Models.GameComponent;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

public class ComponentTreeNode extends ProjectTreeNode {
    private static final long serialVersionUID = -1462728068661712428L;
    private GameComponent component;
    public ComponentTreeNode(GameComponent component){
        super(component.getName());
        this.component = component;
    }

    public GameComponent getComponent(){
        return this.component;
    }


}
