package UI;

import Models.*;
import Models.Component;
import Observers.ColorLabelObserver;
import Observers.Observer;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

public class ComponentTree extends JPanel {
    private JTree tree;

    private Game game;

    DefaultMutableTreeNode top = new DefaultMutableTreeNode("Project");
    DefaultMutableTreeNode cards = new DefaultMutableTreeNode("Cards");
    DefaultMutableTreeNode pieces = new DefaultMutableTreeNode("Pieces");
    DefaultMutableTreeNode textures = new DefaultMutableTreeNode("Textures");
    DefaultMutableTreeNode rules = new DefaultMutableTreeNode("Rules");
    DefaultMutableTreeNode decks = new DefaultMutableTreeNode("Decks");
    DefaultMutableTreeNode players = new DefaultMutableTreeNode("Players");

    JPopupMenu rightClickMenu;
    JMenuItem delete,add;

    ArrayList<Observer> observers;

    public ComponentTree(Game game){
        super();

        this.game = game;
        this.observers = new ArrayList<>();

        UIManager.put("Tree.font",new Font("Segoe UI",Font.PLAIN,12));
        UIManager.put("Tree.border",BorderFactory.createEmptyBorder(2,2,2,2));

        top.add(cards);
        top.add(pieces);
        top.add(textures);
        top.add(rules);
        top.add(decks);
        top.add(players);
        //top.add(new DefaultMutableTreeNode("Dice"));

        tree = new JTree(top);
        tree.setPreferredSize(new Dimension(1280,0));
        tree.setEditable(true);

        JScrollPane view = new JScrollPane(tree);

        setLayout(new BorderLayout());
        add(view, BorderLayout.WEST);

        this.rightClickMenu = new JPopupMenu();
        this.delete = new JMenuItem("Delete");
        this.add = new JMenuItem("Add");

        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();

                if(selectedNode.getParent().equals(cards)){
                    game.getCards().remove(game.getCard(selectedNode.toString()));
                    refreshTree(game);
                }else if(selectedNode.getParent().equals(pieces)){
                    game.getPieces().remove(game.getPiece(selectedNode.toString()));
                    refreshTree(game);
                }else if(selectedNode.getParent().equals(textures)){
                    Texture texture = game.getTexture(selectedNode.toString());
                    game.getTextures().remove(game.getTexture(selectedNode.toString()));
                    game.getBoard().setColor(game.getBoard().getColor());
                    for(Space[] row:game.getBoard().getSpaces()){
                        for(Space space:row){
                            if(space.isUsingTexture()){
                                if(space.getTexture().equals(texture)){
                                    space.setColor(space.getColor());
                                }
                            }
                        }
                    }
                    refreshTree(game);
                }else if(selectedNode.getParent().equals(pieces)){
                    game.getPieces().remove(game.getPiece(selectedNode.toString()));
                    refreshTree(game);
                }else if(selectedNode.getParent().equals(pieces)){
                    game.getPieces().remove(game.getPiece(selectedNode.toString()));
                    refreshTree(game);
                }

                for(Observer observer:observers){
                    observer.update();
                }
            }
        });

        rightClickMenu.add(delete);
        rightClickMenu.add(add);

        tree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(SwingUtilities.isRightMouseButton(e)){
                    int selRow = tree.getRowForLocation(e.getX(), e.getY());
                    TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
                    tree.setSelectionPath(selPath);
                    if (selRow>-1){
                        tree.setSelectionRow(selRow);
                    }

                    rightClickMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
    }

//    public void updateTree(Card card){
//        DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
//        DefaultMutableTreeNode node = new DefaultMutableTreeNode(card.getName());
//        tree.scrollPathToVisible(new TreePath(node.getPath()));
//        model.insertNodeInto(node,cards,cards.getChildCount());
//    }

    public void updateTree(Component component){
        DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(component.getName());
        tree.scrollPathToVisible(new TreePath(node.getPath()));
        if(component instanceof Piece){
            model.insertNodeInto(node,pieces,pieces.getChildCount());
        }
        else if(component instanceof Card){
            model.insertNodeInto(node, cards, cards.getChildCount());
        }
    }

    public void addDeck(Deck deck){
        DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(deck.getName());
        tree.scrollPathToVisible(new TreePath(node.getPath()));
        for(Card card: deck.getCards()){
            node.add(new DefaultMutableTreeNode(card.getName()));
        }
//        this.decks.add(node);
        model.insertNodeInto(node,decks,decks.getChildCount());
//        refreshTree(game);

    }

    public void updateTree(Texture texture){
        DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(texture.getName());
        tree.scrollPathToVisible(new TreePath(node.getPath()));
        model.insertNodeInto(node, textures, textures.getChildCount());
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
        cards.removeAllChildren();
        pieces.removeAllChildren();
        textures.removeAllChildren();
        decks.removeAllChildren();

        for(Card card: this.game.getCards()){
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(card.getName());
            model.insertNodeInto(node, cards, cards.getChildCount());
        }

        for(Piece piece: this.game.getPieces()){
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(piece.getName());
            model.insertNodeInto(node,pieces,pieces.getChildCount());
        }

        for(Texture texture: this.game.getTextures()){
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(texture.getName());
            model.insertNodeInto(node,textures,textures.getChildCount());
        }

        for(Deck deck: this.game.getDecks()){
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(deck.getName());
            for(Card card: deck.getCards()){
                node.add(new DefaultMutableTreeNode(card.getName()));
            }
            model.insertNodeInto(node,decks,decks.getChildCount());
        }

        model.reload();
    }

    public void addObserver(Observer obs){
        observers.add(obs);
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

