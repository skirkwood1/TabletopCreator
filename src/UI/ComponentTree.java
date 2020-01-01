package UI;

import java.awt.*;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

public class ComponentTree extends JPanel {
    private JTree tree;


    public ComponentTree(){
        DefaultMutableTreeNode top = new DefaultMutableTreeNode("Top");
        top.add(new DefaultMutableTreeNode("1"));
        top.add(new DefaultMutableTreeNode("2"));
        top.add(new DefaultMutableTreeNode("3"));
        tree = new JTree(top);
        tree.setPreferredSize(new Dimension(150,0));
        tree.setEditable(true);

        JScrollPane view = new JScrollPane(tree);

        setLayout(new BorderLayout());
        add(view, BorderLayout.WEST);
    }
}
