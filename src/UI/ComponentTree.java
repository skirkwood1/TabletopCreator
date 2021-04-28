package UI;

import Commands.CommandStack;
import Commands.DeleteComponentCommand;
import Commands.DeleteFromDeckCommand;
import Commands.DetextureSpacesCommand;
import Models.*;
import Models.GameComponent;
import UI.UIHelpers.Icons.TreeCollapsedIcon;
import UI.UIHelpers.Icons.TreeExpandedIcon;
import UI.UIHelpers.Icons.TreeIcon;
import UI.UIHelpers.Icons.TreeLeafIcon;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.plaf.basic.BasicTreeUI;
import javax.swing.tree.*;

public class ComponentTree extends JPanel implements Observable {
    private final JTree tree;

    private Game game;

    DefaultMutableTreeNode top = new DefaultMutableTreeNode("Project");
    DefaultMutableTreeNode cards = new DefaultMutableTreeNode("Cards");
    DefaultMutableTreeNode pieces = new DefaultMutableTreeNode("Pieces");
    DefaultMutableTreeNode textures = new DefaultMutableTreeNode("Textures");
    DefaultMutableTreeNode rules = new DefaultMutableTreeNode("Rules");
    DefaultMutableTreeNode decks = new DefaultMutableTreeNode("Decks");
    DefaultMutableTreeNode players = new DefaultMutableTreeNode("Players");
    DefaultMutableTreeNode resources = new DefaultMutableTreeNode("Resources");


    private JPopupMenu rightClickMenu;
    private JMenuItem delete,add;


    public ComponentTree(Game game, CommandStack commandStack){
        //super();

        this.game = game;
        //this.observers = new ArrayList<>();

        UIManager.put("Tree.font",new Font("Segoe UI",Font.PLAIN,14));
        //UIManager.put("Tree.border",BorderFactory.createEmptyBorder(2,2,2,2));
        UIManager.put("Tree.paintLines", Boolean.FALSE);

        Icon empty = new TreeIcon();
        UIManager.put("Tree.collapsedIcon", empty);
        UIManager.put("Tree.expandedIcon", empty);

        top.add(cards);
        top.add(pieces);
        top.add(textures);
        top.add(rules);
        top.add(decks);
        top.add(players);
        top.add(resources);
        //top.add(new DefaultMutableTreeNode("Dice"));

        tree = new JTree(top);
        tree.setShowsRootHandles(false);
        tree.setBorder(BorderFactory.createEmptyBorder(2,5,2,2));
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
                GameComponent component = null;

                ArrayList<Space> texturedSpaces = new ArrayList<>();
                if(selectedNode.getParent().equals(cards)){
                    component = game.getCard(selectedNode.toString());
                }else if(selectedNode.getParent().equals(pieces)){
                    component = game.getPiece(selectedNode.toString());
                }else if(selectedNode.getParent().equals(textures)){
                    component = game.getTexture(selectedNode.toString());
                    game.getBoard().setColor(game.getBoard().getColor());
                    for(Space[] row:game.getBoard().getSpaces()){
                        for(Space space:row){
                            if(space.isUsingTexture()){
                                if(space.getTexture().equals(component)){
                                    texturedSpaces.add(space);
                                }
                            }
                        }
                    }
                }else if(selectedNode.getParent().equals(resources)){

                }
                else if(selectedNode.getParent().equals(decks)){
                    component = game.getDeck(selectedNode.toString());
                }else if(selectedNode.getParent().getParent().equals(decks)){
                    component = game.getCard(selectedNode.toString());
                    ArrayList<Card> cardList = new ArrayList<>();
                    cardList.add((Card)component);
                    DeleteFromDeckCommand ddc = new DeleteFromDeckCommand(game.getDeck(selectedNode.getParent().toString()),cardList);
                    commandStack.insertCommand(ddc);
                }

                if(component != null && !selectedNode.getParent().getParent().equals(decks)){
                    DeleteComponentCommand dcc = new DeleteComponentCommand(game,component);
                    commandStack.insertCommand(dcc);

                    DetextureSpacesCommand dsc = new DetextureSpacesCommand(game,texturedSpaces);
                    commandStack.insertCommand(dsc);
                }

                updateObservers();
                refreshTree(game);
            }
        });

        rightClickMenu.add(delete);
        //rightClickMenu.add(add);

        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
        renderer.setClosedIcon(new TreeCollapsedIcon());
        renderer.setOpenIcon(new TreeExpandedIcon());
        renderer.setLeafIcon(new TreeLeafIcon());
        tree.setCellRenderer(renderer);

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

    public void updateTree(GameComponent component){
        DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(component.getName());
        tree.scrollPathToVisible(new TreePath(node.getPath()));
        if(component instanceof Piece){
            model.insertNodeInto(node,pieces,pieces.getChildCount());
        }
        else if(component instanceof Card){
            model.insertNodeInto(node, cards, cards.getChildCount());
        }
        else if(component instanceof Texture){
            model.insertNodeInto(node, textures, textures.getChildCount());
        }
    }

    public void addResource(Resource resource){
        DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(resource.getName());
        tree.scrollPathToVisible(new TreePath(node.getPath()));
        model.insertNodeInto(node,resources,resources.getChildCount());
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
        resources.removeAllChildren();

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

        for(Resource resource: this.game.getResources()){
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(resource.getName());
            model.insertNodeInto(node,resources,resources.getChildCount());
        }

        model.reload();
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

