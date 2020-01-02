package UI;

import Models.Card;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

public class ComponentTree extends JPanel {
    private JTree tree;
    private Card selectedCard = null;

    public ComponentTree(){
        super();
        DefaultMutableTreeNode top = new DefaultMutableTreeNode("Game");
        DefaultMutableTreeNode deck = new DefaultMutableTreeNode("Deck");
        top.add(deck);
        top.add(new DefaultMutableTreeNode("Pieces"));
        top.add(new DefaultMutableTreeNode("Dice"));

        for(Card card: Card.deck) {
            deck.add(new DefaultMutableTreeNode(card.getName()));
        }

        tree = new JTree(top);
        tree.setPreferredSize(new Dimension(1280,0));
        tree.setEditable(true);

        JScrollPane view = new JScrollPane(tree);

        setLayout(new BorderLayout());
        add(view, BorderLayout.WEST);
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

