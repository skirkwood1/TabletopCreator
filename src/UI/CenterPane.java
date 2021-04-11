package UI;

import Commands.CommandStack;
import Commands.UpdateColorCommand;
import Models.Deck;
import Models.Game;
import Models.Component;
import Models.Texture;
import Observers.BoardPaneObserver;
import Observers.ColorLabelObserver;
import Observers.Observer;
import UI.Listeners.BoardPaneViewScroll;
import UI.Listeners.ImageViewScroll;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import static javax.swing.BorderFactory.createEmptyBorder;


public class CenterPane extends JPanel {
    ComponentTree componentTree;
    BoardPane boardPane;
    JScrollPane boardScreen;
    private final TextPanel cardText;
    private final JLabel componentImage;

    private final JTextField cmd;
    private final TextPanel cmdOutput;

    //private double zoom = 1.0;
    private double imageZoom = 1.0;

    private HashMap<String,Action> commandMap;
    private ArrayList<Observer> observers;

    private Game game;
    private CommandStack commandStack;

    BoardPaneViewScroll bpvs;

    TreeSelectionListener tsl = e -> {
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)componentTree.getTree().getLastSelectedPathComponent();
        if(selectedNode != null){
            String name = selectedNode.toString();
            Component component = null;
            Texture texture = null;
//                if(selectedNode.isLeaf()){
//                    card = game.getCard(cardName);
//                }

            if(selectedNode.isLeaf()){
                if(selectedNode.getParent().equals(componentTree.top)){

                }
                else if(selectedNode.getParent().equals(componentTree.pieces)){
                    component = game.getPiece(name);
                }
                else if(selectedNode.getParent().equals(componentTree.cards)){
                    component = game.getCard(name);
                }
                else if(selectedNode.getParent().equals(componentTree.textures)){
                    texture = game.getTexture(name);
                }
                else if(selectedNode.getParent().getParent().equals(componentTree.decks)){
                    component = game.getCard(name);
                }

            }

            if(component != null){
                imageZoom = 1.0;
                displayImage(component.getPicture());
                setCardText(component.getText());
                game.setSelectedComponent(component);
            }else if(texture != null){
                displayImage(texture.getTexture());
                //game.getBoard().setTexture(texture);
                UpdateColorCommand ucc = new UpdateColorCommand(game,texture);
                commandStack.insertCommand(ucc);

                updateObservers();

            }
        }
    };

    public CenterPane(Game game,CommandStack commandStack) {
        this.game = game;

        UIManager.put("ScrollPane.border",BorderFactory.createEmptyBorder());
        UIManager.put("SplitPane.border",BorderFactory.createEmptyBorder());

        cardText = new TextPanel();
        componentImage = new JLabel();
        //textPanel = new TextPanel();
        boardPane = new BoardPane(game,commandStack);
        boardScreen = new JScrollPane();
        componentTree = new ComponentTree(game);
        JScrollPane imagePane = new JScrollPane(componentImage);

        this.commandStack = commandStack;
        this.observers = new ArrayList<>();

        observers.add(new BoardPaneObserver(this));

        cardText.setPreferredSize(new Dimension(200,100));
        cardText.setMinimumSize(new Dimension(200,100));
        cardText.setMaximumSize(new Dimension(300,500));
        cardText.setBorder(null);

        imagePane.setPreferredSize(new Dimension(200, 300));
        imagePane.setMinimumSize(new Dimension(200,100));
        imagePane.setMaximumSize(new Dimension(300,500));
        imagePane.setBorder(null);

        componentImage.setHorizontalAlignment(SwingConstants.CENTER);
        componentImage.setVerticalAlignment(SwingConstants.CENTER);

        JSplitPane cardPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, cardText, imagePane);
        cardPane.setMaximumSize(new Dimension (300,600));
        cardPane.setDividerSize(6);

//        Dimension boardDimension = new Dimension(game.getBoard().getSize()[0]*40+40,game.getBoard().getSize()[1]*40+40);
//        boardPane.setPreferredSize(boardDimension);
//        boardPane.setSize(boardDimension);
        boardPane.setMinimumSize(new Dimension(400,300));

        //boardScreen.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        //boardScreen.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        boardScreen.setViewportView(boardPane);
        boardScreen.getViewport().setOpaque(false);
        boardScreen.setMinimumSize(new Dimension(800,400));

        boardScreen.getVerticalScrollBar().setOpaque(false);

        boardScreen.getVerticalScrollBar().setUI(scrollBarUI());

        boardScreen.getHorizontalScrollBar().setOpaque(false);

        boardScreen.getHorizontalScrollBar().setUI(scrollBarUI());

        boardScreen.setAutoscrolls(true);

        //boardScreen.setBackground(Color.LIGHT_GRAY);
        //boardScreen.setBorder(BorderFactory.createEmptyBorder(-2,-2,-2,-2));


        cmd = new JTextField();
        cmd.setSize(new Dimension(800,20));
        cmd.setMinimumSize(new Dimension(800,20));
        cmd.setPreferredSize(new Dimension(800,20));
        //cmd.setBorder(null);

        cmdOutput = new TextPanel();
        cmdOutput.setSize(new Dimension(800,100));
        cmdOutput.setPreferredSize(new Dimension(800,100));
        //cmdOutput.setBorder(BorderFactory.createEmptyBorder(-2,-1,-2,-1));

        cmd.addActionListener(e -> {
            String[] command = cmd.getText().split(" ");
            if(commandMap.containsKey(command[0])){
                parseCommand(command);
                cmd.setText("");
            }else{
                cmdOutput.appendBottomText(cmd.getText());
                cmd.setText("");
            }
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
        //boardAndCmd.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));

        boardAndCmd.addPropertyChangeListener("dividerLocation", e -> {
            int location = ((Integer)e.getNewValue()).intValue();

            if (location < 400)
            {
                JSplitPane splitPane = (JSplitPane)e.getSource();
                splitPane.setDividerLocation( 400 );
            }
        });

        JSplitPane textAndCardPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, boardAndCmd, cardPane);
        textAndCardPane.setResizeWeight(1);
        textAndCardPane.setDividerSize(6);


        componentTree.setMinimumSize(new Dimension(50,0));
        componentTree.setMaximumSize(new Dimension(250,0));
        componentTree.setPreferredSize(new Dimension(150,0));
        componentTree.setSize(new Dimension(150,0));
        componentTree.revalidate();

        //private TextPanel textPanel;
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

        //setLayout(new FlowLayout(FlowLayout.LEFT));
        setLayout(new BorderLayout());

        overallPane.setContinuousLayout(true);

        add(overallPane);

        componentTree.getTree().addTreeSelectionListener(tsl);
        BoardPaneObserver boardPaneObserver = new BoardPaneObserver(this);
        componentTree.addObserver(boardPaneObserver);

        componentImage.setAutoscrolls(true);

        ImageViewScroll ivs = new ImageViewScroll(this.game, imagePane,componentImage);

        componentImage.addMouseListener(ivs);
        componentImage.addMouseMotionListener(ivs);
        componentImage.addMouseWheelListener(ivs);

        this.bpvs = new BoardPaneViewScroll(boardScreen,boardPane,observers);

        boardPane.addMouseListener(bpvs);
        boardPane.addMouseMotionListener(bpvs);
        boardPane.addMouseWheelListener(bpvs);

        updateBoard();

        imagePane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        imagePane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        commandMap = new HashMap<String,Action>();
        actionSetup();

    }


    //void appendText(String text){
    //    textPanel.appendText(text);
    //}

    void setCardText(String text){
        cardText.setText(text);
    }

//    void displayImage(File imageFile){
//        try {
//            Image image = ImageIO.read(imageFile);
//            displayImage(image);
//
//        }catch (IOException ex){
//            ex.printStackTrace();
//        }
//    }

    void displayImage(Image image){
        ImageIcon icon = new ImageIcon(image);
        componentImage.setIcon(icon);
    }

    void updateComponentTree(Component component){
        componentTree.updateTree(component);
    }

    void updateComponentTree(Deck deck){
        componentTree.addDeck(deck);
    }

    void updateComponentTree(Texture texture){
        componentTree.updateTree(texture);
    }

    //void updateComponentTree(Piece piece){
    //    componentTree.updateTree(piece);
    //}

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

    void updateColor(Color c){

        //boardPane.chosenColor = c;
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

    @Override public void setBorder(Border border) {
        // No!
    }

    public BasicScrollBarUI scrollBarUI(){
        return new BasicScrollBarUI() {
            @Override
            protected JButton createDecreaseButton(int orientation) {
                return zeroButton();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return zeroButton();
            }

            @Override
            protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds)
            {
            }

            JButton zeroButton(){
                JButton jbutton = new JButton();
                jbutton.setPreferredSize(new Dimension(0, 0));
                jbutton.setMinimumSize(new Dimension(0, 0));
                jbutton.setMaximumSize(new Dimension(0, 0));
                return jbutton;
            }

            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = Color.LIGHT_GRAY;
            }
        };
    }

    public void addObserver(Observer obs){
        this.observers.add(obs);
        this.componentTree.addObserver(obs);
    }

    public void updateObservers(){
        for(Observer observer:observers){
            observer.update();
        }
    }

    public ComponentTree getComponentTree(){
        return this.componentTree;
    }

}
