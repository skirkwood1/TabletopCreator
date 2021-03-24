package UI;

import Commands.CommandStack;
import Models.Card;
import Models.Game;
import Models.Piece;
import Models.Component;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

import static javax.swing.BorderFactory.createEmptyBorder;


public class CenterPane extends JPanel {
    private ComponentTree componentTree;
    private TextPanel textPanel;
    private JSplitPane overallPane;
    private JSplitPane textAndCardPane;
    private JSplitPane cardPane;
    private JSplitPane boardAndCmd;
    BoardPane boardPane;
    JScrollPane boardScreen;
    private TextPanel cardText;
    private JLabel componentImage;
    private JScrollPane imagePane;

    private JTextField cmd;
    private TextPanel cmdOutput;
    private JSplitPane cmdPane;

    private double zoom = 1.0;

    private HashMap<String,Action> commandMap;

    MouseAdapter mb = new MouseAdapter() {

        //private Point origin;
        private Point holdPoint;

        private int mouseButton = 0;

        @Override
        public void mousePressed(MouseEvent e) {

            holdPoint = new Point(e.getPoint());
            //origin = new Point(e.getPoint());
            if((e.getModifiersEx() & InputEvent.BUTTON2_DOWN_MASK) != 0){
                mouseButton = 3;
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            mouseButton = 0;
            boardScreen.setCursor(null);

            updateBoard();
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            updateBoard();
            if (mouseButton == 3) {
                boardScreen.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));

                Point dragEventPoint = e.getPoint();
                JViewport viewport = (JViewport) boardPane.getParent();
                Point viewPos = viewport.getViewPosition();
                int maxViewPosX = boardPane.getWidth() - viewport.getWidth();
                int maxViewPosY = boardPane.getHeight() - viewport.getHeight();

                if (boardPane.getWidth() > viewport.getWidth()) {
                    viewPos.x -= dragEventPoint.x - holdPoint.x;

                    if (viewPos.x < 0) {
                        viewPos.x = 0;
                        holdPoint.x = dragEventPoint.x;
                    }

                    if (viewPos.x > maxViewPosX) {
                        viewPos.x = maxViewPosX;
                        holdPoint.x = dragEventPoint.x;
                    }
                }

                if (boardPane.getHeight() > viewport.getHeight()) {
                    viewPos.y -= dragEventPoint.y - holdPoint.y;

                    if (viewPos.y < 0) {
                        viewPos.y = 0;
                        holdPoint.y = dragEventPoint.y;
                    }

                    if (viewPos.y > maxViewPosY) {
                        viewPos.y = maxViewPosY;
                        holdPoint.y = dragEventPoint.y;
                    }
                }

                viewport.setViewPosition(viewPos);
            }
            else if (mouseButton == 1){

            }
        }
        public void mouseMoved(MouseEvent e){
            updateBoard();
        }

    };

    MouseWheelListener mwl = new MouseWheelListener() {
        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {

            zoom -= e.getWheelRotation() * 0.1;
            if(zoom < 0.1){
                zoom = 0.1;
            }
            boardPane.setZoom(zoom);

//            boardPane.setPreferredSize(
//                    new Dimension((int)(boardScreen.getSize().getWidth()*zoom),
//                            (int)(boardScreen.getSize().getHeight()*zoom)));
//
//            boardPane.setSize(boardPane.getPreferredSize());

            boardPane.updateSize();

            boardScreen.setViewportView(boardPane);

            boardPane.removeAll();
            boardPane.revalidate();
            boardPane.repaint();

            boardScreen.revalidate();
            boardScreen.repaint();

        }
    };

    private Game game;
    private CommandStack commandStack;

    public CenterPane(Game game,CommandStack commandStack) {
        this.game = game;

        cardText = new TextPanel();
        componentImage = new JLabel();
        //textPanel = new TextPanel();
        boardPane = new BoardPane(game,commandStack);
        boardScreen = new JScrollPane();
        componentTree = new ComponentTree(game);
        imagePane = new JScrollPane(componentImage);

        this.commandStack = commandStack;

        cardText.setPreferredSize(new Dimension(200,100));
        cardText.setMinimumSize(new Dimension(200,100));
        cardText.setMaximumSize(new Dimension(300,500));

        imagePane.setPreferredSize(new Dimension(200, 300));
        imagePane.setMinimumSize(new Dimension(200,100));
        imagePane.setMaximumSize(new Dimension(300,500));
        componentImage.setHorizontalAlignment(SwingConstants.CENTER);
        componentImage.setVerticalAlignment(SwingConstants.CENTER);

        cardPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, cardText, imagePane);
        cardPane.setMaximumSize(new Dimension (300,600));

//        Dimension boardDimension = new Dimension(game.getBoard().getSize()[0]*40+40,game.getBoard().getSize()[1]*40+40);
//        boardPane.setPreferredSize(boardDimension);
//        boardPane.setSize(boardDimension);
        boardPane.setMinimumSize(new Dimension(400,300));

        //boardScreen.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        //boardScreen.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        boardScreen.setViewportView(boardPane);
        boardScreen.setBackground(Color.LIGHT_GRAY);
        boardScreen.getViewport().setOpaque(true);
        boardScreen.setBorder(createEmptyBorder());

        cmd = new JTextField();
        cmd.setSize(new Dimension(800,20));
        cmd.setPreferredSize(new Dimension(800,20));


        cmdOutput = new TextPanel();
        cmdOutput.setSize(new Dimension(800,40));
        cmdOutput.setPreferredSize(new Dimension(800,40));

        cmd.addActionListener(e -> {
            String[] command = cmd.getText().split(" ");
            if(commandMap.containsKey(command[0])){
                parseCommand(command);
            }else{
            cmdOutput.appendBottomText(cmd.getText());
            cmd.setText("");
            }
        });

        cmdPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        cmdPane.setDividerSize(0);

        //buttonPane.add(cardBtn);
        //buttonPane.add(pieceBtn);
        cmdPane.add(cmdOutput);
        cmdPane.add(cmd);

        cmdPane.setResizeWeight(1);


        boardAndCmd = new JSplitPane(JSplitPane.VERTICAL_SPLIT, boardScreen,cmdPane);

        textAndCardPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, boardAndCmd, cardPane);
        textAndCardPane.setResizeWeight(1);


        componentTree.setMinimumSize(new Dimension(50,0));
        componentTree.setMaximumSize(new Dimension(250,0));
        componentTree.setPreferredSize(new Dimension(150,0));
        componentTree.setSize(new Dimension(150,0));
        componentTree.revalidate();

        overallPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, componentTree, textAndCardPane);

        overallPane.setResizeWeight(0);
        overallPane.setPreferredSize(new Dimension(1200,600));
        overallPane.setDividerLocation(0.25);

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

        componentTree.getTree().addTreeSelectionListener(e -> {

            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)componentTree.getTree().getLastSelectedPathComponent();
            if(selectedNode != null){
                String name = selectedNode.toString();
                Component component = null;
//                if(selectedNode.isLeaf()){
//                    card = game.getCard(cardName);
//                }

                if(selectedNode.isLeaf()){
                    if(selectedNode.getParent().equals(componentTree.pieces)){
                        component = game.getPiece(name);
                    }
                    else{
                        component = game.getCard(name);
                    }
                }

                if(component != null){
                    displayImage(component.getPicture());
                    setCardText(component.getText());
                    game.setSelectedComponent(component);
                }
            }
        });

        componentImage.setAutoscrolls(true);



        MouseAdapter ma = new MouseAdapter() {

            private Point holdPoint;

            @Override
            public void mousePressed(MouseEvent e) {
                holdPoint = new Point(e.getPoint());
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseDragged(MouseEvent e) {
                imagePane.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));

                Point dragEventPoint = e.getPoint();
                JViewport viewport = (JViewport) componentImage.getParent();
                Point viewPos = viewport.getViewPosition();
                int maxViewPosX = componentImage.getWidth() - viewport.getWidth();
                int maxViewPosY = componentImage.getHeight() - viewport.getHeight();

                if (componentImage.getWidth() > viewport.getWidth()) {
                    viewPos.x -= dragEventPoint.x - holdPoint.x;

                    if (viewPos.x < 0) {
                        viewPos.x = 0;
                        holdPoint.x = dragEventPoint.x;
                    }

                    if (viewPos.x > maxViewPosX) {
                        viewPos.x = maxViewPosX;
                        holdPoint.x = dragEventPoint.x;
                    }
                }

                if (componentImage.getHeight() > viewport.getHeight()) {
                    viewPos.y -= dragEventPoint.y - holdPoint.y;

                    if (viewPos.y < 0) {
                        viewPos.y = 0;
                        holdPoint.y = dragEventPoint.y;
                    }

                    if (viewPos.y > maxViewPosY) {
                        viewPos.y = maxViewPosY;
                        holdPoint.y = dragEventPoint.y;
                    }
                }

                viewport.setViewPosition(viewPos);
            }

        };

        componentImage.addMouseListener(ma);
        componentImage.addMouseMotionListener(ma);
        componentImage.addMouseWheelListener(new MouseWheelListener() {
            public void mouseWheelMoved(MouseWheelEvent e) {
                int notches = e.getWheelRotation();
                double temp = zoom - (notches * 0.2);
                // minimum zoom factor is 1.0
                temp = Math.max(temp, 1.0);
                if (temp != zoom) {
                    zoom = temp;
                    //resizeImage();
                }
            }
        });

        boardPane.addMouseListener(mb);
        boardPane.addMouseMotionListener(mb);
        boardPane.addMouseWheelListener(mwl);

        updateBoard();

        imagePane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        imagePane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        commandMap = new HashMap<String,Action>();
        actionSetup();

    }


    void appendText(String text){
        textPanel.appendText(text);
    }

    void setCardText(String text){
        cardText.setText(text);
    }

    void displayImage(File imageFile){
        try {
            Image image = ImageIO.read(imageFile);
            displayImage(image);

        }catch (IOException ex){
            ex.printStackTrace();
        }
    }

    void displayImage(Image image){
//        int baseHeight = ((BufferedImage)image).getHeight();
//        int baseWidth = ((BufferedImage)image).getWidth();

//        int imageSize;
//        float imageScale;
//        if(imagePane.getHeight()>imagePane.getWidth()){
//            imageSize = imagePane.getWidth();
//            imageScale = (float)imageSize/baseWidth;
//        }
//        else{
//            imageSize = imagePane.getHeight();
//            imageScale = (float)imageSize/baseHeight;
//        }
//
//        image = image.getScaledInstance((int)(baseHeight*imageScale),
//                (int)(((BufferedImage)image).getHeight()*imageScale),
//                Image.SCALE_SMOOTH);
        ImageIcon icon = new ImageIcon(image);
        componentImage.setIcon(icon);
    }

    void updateComponentTree(Component component){
        componentTree.updateTree(component);
    }

    //void updateComponentTree(Piece piece){
    //    componentTree.updateTree(piece);
    //}

    public void collapseComponentTree(){componentTree.collapseTree();}

    void refreshComponentTree(Game game){
        this.game = game;
        componentTree.refreshTree(game);
        //componentTree.collapseTree();
        componentTree.getTree().addTreeSelectionListener(e -> {

            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)componentTree.getTree().getLastSelectedPathComponent();
            if(selectedNode != null){
                String name = selectedNode.toString();
                Component component = null;
                if(selectedNode.isLeaf()){
                    if(selectedNode.getParent().equals(componentTree.pieces)){
                        component = game.getPiece(name);
                    }
                    else if (selectedNode.getParent().equals(componentTree.deck)){
                        component = game.getCard(name);
                    }
                }
                if(component != null){
                    displayImage(component.getPicture());
                    setCardText(component.getText());
                }
            }
        });
    }

    void updateBoard(){

        boardPane.setZoom(zoom);
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

}
