package UI;

import Commands.CommandStack;
import Commands.UpdateColorCommand;
import Models.*;
import Observers.BoardPaneObserver;
import Observers.Observer;
import UI.Listeners.BoardPaneViewScroll;
import UI.Listeners.ImageViewScroll;
import UI.UIHelpers.ScrollBarUICreator;

import javax.swing.*;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Random;


public class CenterPane extends JPanel implements Observable {
    ComponentTree componentTree;
    BoardPane boardPane;
    JScrollPane boardScreen;
    private final CommandLog componentText;
    private final JLabel componentImage;

    private final JTextField cmd;
    private final CommandLog cmdOutput;

    //private double zoom = 1.0;

    private HashMap<String,Action> commandMap;
    //private ArrayList<Observer> observers;

    private Game game;
    private CommandStack commandStack;

    BoardPaneViewScroll bpvs;
    ImageViewScroll ivs;

    TreeSelectionListener tsl = e -> {
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)componentTree.getTree().getLastSelectedPathComponent();
        if(selectedNode != null){
            String name = selectedNode.toString();
            GameComponent component = null;
            CardInterface card = null;

            if(selectedNode.getParent() == null){

            }
            else if((selectedNode.getParent() == componentTree.top)){

            }
            else if(selectedNode.getParent().equals(componentTree.pieces)){
                component = game.getPiece(name);
            }
            else if(selectedNode.getParent().equals(componentTree.cards)){
                component = game.getCard(name);
                card = game.getCard(name);
            }
            else if(selectedNode.getParent().equals(componentTree.textures)){
                component = game.getTexture(name);
            }
            else if(selectedNode.getParent().equals(componentTree.decks)){
                component = game.getDeck(name);
                card = game.getDeck(name);
            }
            else if(selectedNode.getParent().getParent().equals(componentTree.decks)){
                component = game.getCard(name);
                card = game.getCard(name);
            }

            if(component != null){
                setComponentPane(component);
                game.setSelectedComponent(component);
                game.setSelectedCard(card);

                if(component instanceof Texture){
                    UpdateColorCommand ucc = new UpdateColorCommand(game,(Texture) component);
                    commandStack.insertCommand(ucc);
                }
                updateObservers();
            }
        }
    };

    public CenterPane(Game game,CommandStack commandStack) {
        this.game = game;

        //setLayout(new FlowLayout(FlowLayout.LEFT));
        setLayout(new BorderLayout());

        UIManager.put("ScrollPane.border",BorderFactory.createEmptyBorder());
        UIManager.put("SplitPane.border",BorderFactory.createEmptyBorder());

        componentText = new CommandLog();
        componentImage = new JLabel();
        boardPane = new BoardPane(game,commandStack);
        boardScreen = new JScrollPane();
        componentTree = new ComponentTree(game,commandStack);
        JScrollPane imagePane = new JScrollPane(componentImage);

        this.commandStack = commandStack;
        //this.observers = new ArrayList<>();

        addObserver(new BoardPaneObserver(this));

        componentText.setPreferredSize(new Dimension(200,100));
        componentText.setMinimumSize(new Dimension(200,100));
        componentText.setMaximumSize(new Dimension(300,500));
        componentText.setBorder(null);

        imagePane.setPreferredSize(new Dimension(200, 300));
        imagePane.setMinimumSize(new Dimension(200,100));
        imagePane.setMaximumSize(new Dimension(300,500));
        imagePane.setBorder(null);

        componentImage.setHorizontalAlignment(SwingConstants.CENTER);
        componentImage.setVerticalAlignment(SwingConstants.CENTER);

        JSplitPane cardPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, componentText, imagePane);
        cardPane.setMaximumSize(new Dimension (300,600));
        cardPane.setDividerSize(6);

        boardPane.setMinimumSize(new Dimension(400,300));

        boardScreen.setViewportView(boardPane);
        boardScreen.getViewport().setOpaque(false);
        boardScreen.setMinimumSize(new Dimension(800,400));
        boardScreen.getVerticalScrollBar().setOpaque(false);
        boardScreen.getVerticalScrollBar().setUI(ScrollBarUICreator.scrollBarUI());
        boardScreen.getHorizontalScrollBar().setOpaque(false);
        boardScreen.getHorizontalScrollBar().setUI(ScrollBarUICreator.scrollBarUI());
        boardScreen.setAutoscrolls(true);

        //boardScreen.setBackground(Color.LIGHT_GRAY);
        //boardScreen.setBorder(BorderFactory.createEmptyBorder(-2,-2,-2,-2));

        cmd = new JTextField();
        cmd.setSize(new Dimension(800,20));
        cmd.setMinimumSize(new Dimension(800,20));
        cmd.setPreferredSize(new Dimension(800,20));
        //cmd.setBorder(null);

        cmdOutput = new CommandLog();
        cmdOutput.setSize(new Dimension(800,100));
        cmdOutput.setPreferredSize(new Dimension(800,100));

        commandStack.setLog(cmdOutput);
        //cmdOutput.setBorder(BorderFactory.createEmptyBorder(-2,-1,-2,-1));

        cmd.addActionListener(e -> {
            String[] command = cmd.getText().split(" ");
            if(commandMap.containsKey(command[0])){
                parseCommand(command);
            }else{
                cmdOutput.appendBottomText(cmd.getText());
            }
            cmd.setText("");
        });

        JSplitPane cmdPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        cmdPane.setDividerSize(0);
        //cmdPane.setBorder(BorderFactory.createEmptyBorder(-2,-2,-2,-2));

        //buttonPane.add(cardBtn);
        //buttonPane.add(pieceBtn);
        cmdPane.add(cmdOutput);
        cmdPane.add(cmd);
        cmdPane.setMinimumSize(new Dimension(0,80));
        cmdPane.setResizeWeight(1);

        JSplitPane boardAndCmd = new JSplitPane(JSplitPane.VERTICAL_SPLIT, boardScreen, cmdPane);
        boardAndCmd.setDividerSize(6);
        boardAndCmd.setResizeWeight(0.8);
        boardAndCmd.setContinuousLayout(true);
        //boardAndCmd.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));

        boardAndCmd.addPropertyChangeListener("dividerLocation", e -> {
            int location = ((Integer)e.getNewValue()).intValue();
            if (location < 400) {
                JSplitPane splitPane = (JSplitPane)e.getSource();
                splitPane.setDividerLocation( 400 );
            }
        });

        JSplitPane textAndCardPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, boardAndCmd, cardPane);
        textAndCardPane.setResizeWeight(1);
        textAndCardPane.setDividerSize(6);
        textAndCardPane.setContinuousLayout(true);

        componentTree.setMinimumSize(new Dimension(50,0));
        componentTree.setMaximumSize(new Dimension(250,0));
        componentTree.setPreferredSize(new Dimension(150,0));
        componentTree.setSize(new Dimension(150,0));
        componentTree.revalidate();

        //private CommandLog textPanel;
        JSplitPane overallPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, componentTree, textAndCardPane);

        overallPane.setResizeWeight(0);
        overallPane.setPreferredSize(new Dimension(1200,600));
        overallPane.setDividerSize(6);
        //overallPane.setDividerLocation(200);

        overallPane.addPropertyChangeListener("dividerLocation", e -> {
            int location = ((Integer)e.getNewValue()).intValue();
            if (location > 400)
            {
                JSplitPane splitPane = (JSplitPane)e.getSource();
                splitPane.setDividerLocation( 400 );
            }
        });

        overallPane.setContinuousLayout(true);
        add(overallPane);

        componentTree.getTree().addTreeSelectionListener(tsl);
        BoardPaneObserver boardPaneObserver = new BoardPaneObserver(this);
        componentTree.addObserver(boardPaneObserver);

        componentImage.setAutoscrolls(true);

        this.ivs = new ImageViewScroll(this.game, imagePane,componentImage);

        componentImage.addMouseListener(ivs);
        componentImage.addMouseMotionListener(ivs);
        componentImage.addMouseWheelListener(ivs);

        this.bpvs = new BoardPaneViewScroll(boardScreen,boardPane,observers);

        boardPane.addMouseListener(bpvs);
        boardPane.addMouseMotionListener(bpvs);
        boardPane.addMouseWheelListener(bpvs);

        JPopupMenu popupMenu = boardPane.getRightClickMenu();

        JMenuItem delete = new JMenuItem("Delete");
        delete.addActionListener(e -> {
            boardPane.deleteSelectedSpaces();
            //boardPane.deleteSelection();
            updateBoard();
        });

        JMenuItem colorSelect = new JMenuItem("Get Color");
        colorSelect.addActionListener(e -> {
            Space space = boardPane.getSelectedSpace();
            if(space == null){
                return;
            }
            else if(space.isUsingTexture()){
                UpdateColorCommand ucc = new UpdateColorCommand(game,space.getTexture());
                commandStack.insertCommand(ucc);
                //game.getBoard().setTexture(space.getTexture());
            }else{
                UpdateColorCommand ucc = new UpdateColorCommand(game,space.getColor());
                commandStack.insertCommand(ucc);
                //game.getBoard().setColor(space.getColor());
            }
            updateBoard();
            updateObservers();
        });

        JMenuItem flip = new JMenuItem("Flip");
        flip.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardInterface cardInterface = boardPane.getSelectedCard();
                if(cardInterface != null){
                    cardInterface.flip();
                }
                updateBoard();
                updateObservers();
            }
        });

        JMenuItem drawCard = new JMenuItem("Draw Card");
        drawCard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardInterface cardInterface = boardPane.getSelectedCard();
                if(cardInterface instanceof Deck){
                    Deck deck = (Deck)cardInterface;
                    Card card = deck.drawCard();
                    boardPane.placeCard(card);
                }
                updateBoard();
            }
        });

        JMenuItem shuffle = new JMenuItem("Shuffle");
        shuffle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardInterface cardInterface = boardPane.getSelectedCard();
                if(cardInterface instanceof Deck){
                    Deck deck = (Deck)cardInterface;
                    deck.shuffle();
                }
                updateBoard();
            }
        });

        MouseAdapter rightClick = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(boardPane.getSelectedCard() != null){
                    setComponentPane((GameComponent)boardPane.getSelectedCard());
                }
                else if(boardPane.getSelectedSpace() != null){
                    if(boardPane.getSelectedSpace().isOccupied()){
                        setComponentPane(boardPane.getSelectedSpace().getPiece());
                    }
                }
                if(SwingUtilities.isRightMouseButton(e)){
                    popupMenu.removeAll();
                    popupMenu.add(delete);
                    popupMenu.add(colorSelect);

                    if(boardPane.getSelectedCard() != null){
                        popupMenu.add(flip);
                        if(boardPane.getSelectedCard() instanceof Deck){
                            popupMenu.add(drawCard);
                            popupMenu.add(shuffle);
                        }
                    }

                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        };

        boardPane.addMouseListener(rightClick);

        updateBoard();

        imagePane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        imagePane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        commandMap = new HashMap<String,Action>();
        actionSetup();

    }

    void setComponentText(String text){
        componentText.setText(text);
    }

    void displayImage(Image image){
        ImageIcon icon = new ImageIcon(image);
        componentImage.setIcon(icon);
    }

    void updateComponentTree(GameComponent component){
        componentTree.updateTree(component);
    }

    void updateComponentTree(Deck deck){
        componentTree.addDeck(deck);
    }

    void updateComponentTree(Texture texture){
        componentTree.updateTree(texture);
    }

    public void collapseComponentTree(){componentTree.collapseTree();}

    void refreshComponentTree(Game game){
        this.game = game;
        componentTree.refreshTree(game);
        //componentTree.collapseTree();
        componentTree.getTree().addTreeSelectionListener(tsl);
    }

    public void updateBoard(){
        boardPane.updateGame(game);

        boardPane.setZoom(bpvs.getZoom());
        boardPane.updateSize();

        this.boardPane.removeAll();
        this.boardPane.revalidate();
        this.boardPane.repaint();

        this.boardScreen.revalidate();
        this.boardScreen.repaint();
    }

    private void actionSetup(){
        commandMap.put("/roll", new AbstractAction("action1") {
            @Override
            public void actionPerformed(ActionEvent e) {
                cmdOutput.appendBottomText("Roll dice");
            }
        });

        commandMap.put("/undo", new AbstractAction("action2"){
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

    private void parseCommand(String[] command){
        if(command[0].equals("/roll")){
            Random r = new Random();
            cmdOutput.appendBottomText("Rolling " + command[1] + " sided die: " + ((r.nextInt(Integer.parseInt(command[1]))) + 1));

        }
    }

//    @Override public void setBorder(Border border) {
//        // No!
//    }

    public void addObserver(Observer obs){
        this.observers.add(obs);
        this.componentTree.addObserver(obs);
    }

    public ComponentTree getComponentTree(){
        return this.componentTree;
    }

    public void setComponentPane(GameComponent component){
        ivs.setImageZoom(1.0);
        displayImage(component.getImage());
        setComponentText(component.getText());
    }

    public CommandLog getCommandLog(){
        return this.cmdOutput;
    }

}
