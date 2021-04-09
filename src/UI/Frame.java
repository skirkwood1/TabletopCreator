package UI;

import ChatServer.ClientWindow;
import Commands.*;
import Models.*;
import Observers.ColorLabelObserver;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import java.awt.*;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.HashMap;

public class Frame extends JFrame {

    private ComponentTree componentTree;
    private Toolbar toolbar;
    private JTextField textPanel,cmd;
    private TextPanel cmdOutput;
    private CenterPane centerPane;
    private ComponentCreationDialog cardCreationDialog;
    private ComponentCreationDialog pieceCreationDialog;
    private ComponentCreationDialog textureCreationDialog;
    private JSplitPane buttonPane,cmdPane,mainPane;

    private ResizeBoardPane resizePane;

    private JFileChooser fileChooser = new JFileChooser();
    private ColorChooseDialog colorDialog = new ColorChooseDialog();

    private Game game = new Game(10,10);
    private CommandStack commandStack = new CommandStack();

    private HashMap<KeyStroke, Action> actionMap = new HashMap<KeyStroke, Action>();

    public Frame() {
        super("Tabletop Creator v0.02");

        GridBagConstraints gbc = new GridBagConstraints();

        //setLayout(new GridBagLayout());
        setLayout(new BorderLayout());

        UIManager.put("Separator.foreground",Color.BLACK);
        UIManager.put("Separator.background",Color.BLACK);

        Component[] comp =  fileChooser.getComponents();

        fileChooser.setBackground(Color.WHITE);
        fileChooser.setOpaque(true);
        fileChooser.setBorder(BorderFactory.createEmptyBorder(5,10,5,10));
        setFileChooserUI(comp);

        toolbar = new Toolbar(game);
        centerPane = new CenterPane(game,toolbar,commandStack);
        ColorLabelObserver colorLabelObserver = new ColorLabelObserver(toolbar);
        centerPane.addObserver(colorLabelObserver);
        centerPane.setOpaque(false);
        toolbar.setBorder(BorderFactory.createRaisedBevelBorder());

//        cmd = new JTextField();
//        cmdOutput = new TextPanel();
//        cmdOutput.setPreferredSize(new Dimension(800,100));
//
//        cmd.addActionListener(e -> {
//            cmdOutput.appendBottomText(cmd.getText());
//            cmd.setText("");
//        });
//
//        cmdPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
//        cmdPane.setDividerSize(0);

        cardCreationDialog = new ComponentCreationDialog();
        pieceCreationDialog = new ComponentCreationDialog();

        textureCreationDialog = new ComponentCreationDialog();

        this.resizePane = new ResizeBoardPane(game);

        //JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        //separator.setPreferredSize(new Dimension(800,50));

        add(toolbar, BorderLayout.NORTH);
        //add(separator, BorderLayout.CENTER);
        add(centerPane, BorderLayout.CENTER);

        //add(cmdPane, BorderLayout.SOUTH);

        pack();

        toolbar.setStringListener(text -> {
            //centerPane.appendText(text);
            if(text.equals("Save\n\r")){
                fileChooser.setDialogTitle("Save file");
                int userSelection = fileChooser.showSaveDialog(this);
                if(userSelection == JFileChooser.APPROVE_OPTION){
                    File fileToSave = fileChooser.getSelectedFile();
                    System.out.println("Save as file:" + fileToSave.getAbsolutePath());
                    saveGame(game, fileToSave);
                }
            }
            else if(text.equals("Open\n\r")){
                fileChooser.setDialogTitle("Open file");
                int userSelection = fileChooser.showOpenDialog(this);
                if(userSelection == JFileChooser.APPROVE_OPTION){
                    File fileToOpen = fileChooser.getSelectedFile();
                    System.out.println("Open file:" + fileToOpen.getAbsolutePath());
                    Game newGame = openGame(fileToOpen);

                    OpenGameCommand ogc = new OpenGameCommand(this,this.game,newGame);
                    commandStack.insertCommand(ogc);

                    centerPane.refreshComponentTree(game);
                    centerPane.updateBoard();
                }
            }
            else if(text.equals("ChangeSize\n\r")){
                resizePane.display();

                ChangeSizeCommand csc = new ChangeSizeCommand(game,resizePane.getDesiredWidth(),resizePane.getDesiredHeight());
                commandStack.insertCommand(csc);

                //game.getBoard().setSize(resizePane.getDesiredWidth(),resizePane.getDesiredHeight());
                System.out.println(game.getBoard());
                centerPane.updateBoard();


            }
            else if(text.equals("ColorChooser\n\r")){
                colorDialog.display();
                Color color = colorDialog.getColor();

                UpdateColorCommand ucc = new UpdateColorCommand(game,color);
                commandStack.insertCommand(ucc);

                centerPane.updateObservers();

                //toolbar.updateColorLabel();
            }

            else if(text.equals("Message\n\r")){
                try {
                    new ClientWindow("localhost").start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            else if(text.equals("AddCard\n\r")){
                addCardDialog();
            }

            else if(text.equals("AddPiece\n\r")){
                addPieceDialog();
            }

            else if(text.equals("AddTexture\n\r")){
                addTextureDialog();
            }

            else if(text.equals("CreateDeck\n\r")){
                addDeckDialog();
                centerPane.refreshComponentTree(game);
            }

            else if(text.equals("AddToDeck\n\r")){
                try{
                    DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)centerPane.componentTree.getTree().getLastSelectedPathComponent();
                    if(selectedNode.isLeaf()){
                        selectedNode = (DefaultMutableTreeNode)selectedNode.getParent();
                    }
                    String selected = selectedNode.toString();
                    Deck deck = game.getDeck(selected);
                    if(deck != null){
                        addDeckDialog(deck);
                    }else{
                        addDeckDialog();
                    }
                }catch(NullPointerException e){
                    addDeckDialog();
                }
                centerPane.refreshComponentTree(game);
            }

            else if(text.equals("Undo\n\r")){
                undo();

            }

            else if(text.equals("Redo\n\r")){
                redo();
            }

            else if(text.equals("Placement\n\r")){
                centerPane.boardPane.setPlacementType(toolbar.getPlacementType());
                centerPane.updateBoard();
            }
        });

        setSize(1280,720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        keyboardSetup();
    }

    private void undo() {
        commandStack.undo();
        centerPane.refreshComponentTree(game);
        centerPane.updateBoard();
        centerPane.updateObservers();
    }

    private void redo() {
        commandStack.redo();
        centerPane.refreshComponentTree(game);
        centerPane.updateBoard();
        centerPane.updateObservers();
    }

    public void setFileChooserUI(Component[] comp){
        for(int x = 0; x < comp.length; x++) {
            if(comp[x] instanceof Container) setFileChooserUI(((Container)comp[x]).getComponents());
            try{
                comp[x].setFont(new Font("Segoe UI",Font.PLAIN,12));
            } catch(Exception e){}//do nothing
            try{
                comp[x].setBackground(Color.WHITE);
            }catch(Exception e){}
            if(comp[x] instanceof JButton){
                JButton jb = (JButton)comp[x];
                jb.setBorder(BorderFactory.createEmptyBorder(3,3,3,3));
                jb.setBackground(new Color(220,220,220));
            }
        }
    }

    public void setGame(Game game){
        this.game = game;
    }

    public void updateBoard(){
        this.centerPane.updateBoard();
    }

    private void saveGame(Game game, File file){
        try{

            FileOutputStream fileOut = new FileOutputStream(file);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(game);
            objectOut.flush();
            objectOut.close();
            System.out.println("File saved");
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }

    private Game openGame(File file){
        //CommandStack.clear();

        try{
            FileInputStream fileIn = new FileInputStream(file);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);
            Game gameIn = (Game)objectIn.readObject();
            System.out.println(gameIn.toString());

            return gameIn;
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    private void addCardDialog(){
        int n = cardCreationDialog.display();
        if(n == JOptionPane.YES_OPTION){
            String cardName = cardCreationDialog.getComponentName();
            String cardText = cardCreationDialog.getComponentText();
            String fileSelected = cardCreationDialog.getFileSelect();

            Card card = new Card(cardName,cardText,fileSelected);

            AddComponentCommand acc = new AddComponentCommand(game,card);
            commandStack.insertCommand(acc);
            //game.addCard(card);

            centerPane.updateComponentTree(card);

            cardCreationDialog.clear();
        }
    }

    private void addPieceDialog(){
        int n = pieceCreationDialog.display();
        if(n == JOptionPane.YES_OPTION){
            String pieceName = pieceCreationDialog.getComponentName();
            String pieceText = pieceCreationDialog.getComponentText();
            String fileSelected = pieceCreationDialog.getFileSelect();

            Piece piece = new Piece(pieceName,pieceText,fileSelected);

            AddComponentCommand acc = new AddComponentCommand(game,piece);
            commandStack.insertCommand(acc);

            //game.addPiece(piece);

            centerPane.updateComponentTree(piece);

            pieceCreationDialog.clear();
        }
    }

    private void addTextureDialog(){
        int n = textureCreationDialog.display();
        if(n == JOptionPane.YES_OPTION){
            String name = textureCreationDialog.getComponentName();
            String fileSelected = textureCreationDialog.getFileSelect();

            Texture texture = new Texture(name,fileSelected);

            game.addTexture(texture);

            //game.addPiece(piece);

            centerPane.updateComponentTree(texture);

            textureCreationDialog.clear();
        }
    }

    private void addDeckDialog(){
        DeckCreationDialog deckCreationDialog = new DeckCreationDialog(game);
        int n = deckCreationDialog.display();

        if(n == JOptionPane.YES_OPTION){
            Deck deck = new Deck(deckCreationDialog.getDeckName());

            for(Card card: deckCreationDialog.getSelection()){
                if(deckCreationDialog.getNumCopies() != null){
                    deck.addCard(card,deckCreationDialog.getNumCopies());
                }else{
                    deck.addCard(card);
                }
            }

            game.createDeck(deck);
            centerPane.updateComponentTree(deck);
        }
    }

    private void addDeckDialog(Deck deck){
        DeckCreationDialog deckCreationDialog = new DeckCreationDialog(game);
        int n = deckCreationDialog.displayNoName();

        if(n == JOptionPane.YES_OPTION){
            for(Card card: deckCreationDialog.getSelection()){
                if(deckCreationDialog.getNumCopies() != null){
                    deck.addCard(card,deckCreationDialog.getNumCopies());
                }else{
                    deck.addCard(card);
                }
            }
            centerPane.updateComponentTree(deck);
        }
    }

    private void keyboardSetup(){
        KeyStroke undo = KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK);
        KeyStroke redo = KeyStroke.getKeyStroke(KeyEvent.VK_Y, KeyEvent.CTRL_DOWN_MASK);
        KeyStroke toggleGrid = KeyStroke.getKeyStroke(KeyEvent.VK_G, KeyEvent.CTRL_DOWN_MASK);

        KeyStroke del = KeyStroke.getKeyStroke("DELETE");

        actionMap.put(undo, new AbstractAction("action1") {
            @Override
            public void actionPerformed(ActionEvent e) {
                undo();
            }
        });

        actionMap.put(redo, new AbstractAction("action2") {
            @Override
            public void actionPerformed(ActionEvent e) {
                redo();
            }
        });

        actionMap.put(del, new AbstractAction("action3") {
            @Override
            public void actionPerformed(ActionEvent e) {
                centerPane.boardPane.deleteSelectedSpace();
                //centerPane.refreshComponentTree(this.game);
                centerPane.updateBoard();
                centerPane.refreshComponentTree(game);
                centerPane.updateObservers();
            }
        });

        actionMap.put(toggleGrid, new AbstractAction("action4") {
            @Override
            public void actionPerformed(ActionEvent e) {
                centerPane.boardPane.toggleGrid();
                //centerPane.refreshComponentTree(this.game);
                centerPane.updateBoard();
                centerPane.refreshComponentTree(game);
            }
        });



        KeyboardFocusManager kfm = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        kfm.addKeyEventDispatcher( new KeyEventDispatcher() {

            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                KeyStroke keyStroke = KeyStroke.getKeyStrokeForEvent(e);
                if ( actionMap.containsKey(keyStroke) ) {
                    final Action a = actionMap.get(keyStroke);
                    final ActionEvent ae = new ActionEvent(e.getSource(), e.getID(), null );
                    SwingUtilities.invokeLater( new Runnable() {
                        @Override
                        public void run() {
                            a.actionPerformed(ae);
                        }
                    } );
                    return true;
                }
                return false;
            }
        });
    }
}
