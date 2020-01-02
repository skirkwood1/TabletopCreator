package UI;

import Models.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

public class ComponentTree extends JPanel {
    private JTree tree;

    private Game game;

    DefaultMutableTreeNode top = new DefaultMutableTreeNode("Game");
    DefaultMutableTreeNode deck = new DefaultMutableTreeNode("Deck");

    public ComponentTree(Game game){
        super();

        this.game = game;

        top.add(deck);
        top.add(new DefaultMutableTreeNode("Pieces"));
        top.add(new DefaultMutableTreeNode("Dice"));

        tree = new JTree(top);
        tree.setPreferredSize(new Dimension(1280,0));
        tree.setEditable(true);

        JScrollPane view = new JScrollPane(tree);

        setLayout(new BorderLayout());
        add(view, BorderLayout.WEST);
    }

    public void updateTree(Card card){
        DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(card.getName());
        tree.scrollPathToVisible(new TreePath(node.getPath()));
        model.insertNodeInto(node,deck,deck.getChildCount());
    }

    public void collapseTree(){
        if(tree == null){
            throw new NullPointerException("tree == null");
        }
        TreeSelectionModel m = tree.getSelectionModel();
        if (m != null) {
            m.clearSelection();
        }

        for(int i = tree.getRowCount() -1; i > 0; i--){
            tree.collapseRow(i);
        }
    }

    public void refreshTree(Game game){
        this.game = game;
        DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
        deck.removeAllChildren();
        //model.reload();

        for(Card card: this.game.getDeck()){
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(card.getName());
            model.insertNodeInto(node,deck,deck.getChildCount());
        }
    }

//    public void valueChanged(TreeSelectionEvent e) {
//        //JTree tree = (JTree)e.getSource();
//        if (e.getNewLeadSelectionPath() != null) {
//            DefaultMutableTreeNode selected = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
//            String selectedName = selected.toString();
//            System.out.println(selectedName);
//            if (selected.isLeaf()) {
//                selectedCard = Card.getCard(selectedName);
//            }
//        }
//    }

//    public Card getSelectedCard () {
//        return selectedCard;
//    }

    public JTree getTree () {
        return tree;
    }
//
    }

