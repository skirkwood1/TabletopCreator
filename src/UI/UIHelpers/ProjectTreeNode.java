package UI.UIHelpers;

import Models.GameComponent;

import javax.swing.tree.DefaultMutableTreeNode;

public abstract class ProjectTreeNode extends DefaultMutableTreeNode {

    ProjectTreeNode(String name){
        super(name);
    }

    public abstract GameComponent getComponent();
}
