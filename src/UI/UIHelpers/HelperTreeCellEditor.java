package UI.UIHelpers;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeNode;
import java.awt.event.MouseEvent;
import java.util.EventObject;

public class HelperTreeCellEditor extends DefaultTreeCellEditor {
    public HelperTreeCellEditor(JTree tree, DefaultTreeCellRenderer renderer) {
        super(tree, renderer);
    }

    @Override
    public boolean isCellEditable(EventObject e) {
        return super.isCellEditable(e)
                && ((TreeNode) lastPath.getLastPathComponent()) instanceof ComponentTreeNode;
    }

    @Override
    protected boolean canEditImmediately(EventObject event) {
        if((event instanceof MouseEvent) &&
                SwingUtilities.isLeftMouseButton((MouseEvent)event)) {
            MouseEvent me = (MouseEvent)event;

            return ((me.getClickCount() >= 2) &&
                    inHitRegion(me.getX(), me.getY()));
        }
        return (event == null);
    }
}