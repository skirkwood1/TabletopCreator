package UI;

import java.awt.*;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

public class ComponentTree extends JPanel {
    private JTree tree;


    public ComponentTree(){
        DefaultMutableTreeNode top = new DefaultMutableTreeNode("Game");
        DefaultMutableTreeNode decks = new DefaultMutableTreeNode("Decks");
        top.add(decks);
        top.add(new DefaultMutableTreeNode("Pieces"));
        top.add(new DefaultMutableTreeNode("Dice"));

        decks.add(new DefaultMutableTreeNode("Deck 1"));

        tree = new JTree(top);
        tree.setPreferredSize(new Dimension(1280,0));
        tree.setEditable(true);

        JScrollPane view = new JScrollPane(tree);

        setLayout(new BorderLayout());
        add(view, BorderLayout.WEST);
    }
}
