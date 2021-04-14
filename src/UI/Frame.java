package UI;

import ChatServer.ClientWindow;
import Commands.*;
import Models.*;
import Observers.ColorLabelObserver;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.HashMap;

import static UI.StateListener.ButtonOutput.*;

public class Frame extends JFrame {

    private ComponentTree componentTree;
    private Toolbar toolbar;
    private JTextField textPanel,cmd;
    private TextPanel cmdOutput;
    private CenterPane centerPane;
    private ComponentCreationDialog cardCreationDialog;
    private ComponentCreationDialog pieceCreationDialog;
    private ComponentCreationDialog textureCreationDialog;
    private ComponentCreationDialog componentCreationDialog;
    private JSplitPane buttonPane,cmdPane,mainPane;

    private ResizeBoardPane resizePane;

    private JFileChooser fileChooser = new JFileChooser();
    private ColorChooseDialog colorDialog = new ColorChooseDialog();

    private Game game = new Game(10,10);
    private CommandStack commandStack = new CommandStack();

    private HashMap<KeyStroke, Action> actionMap = new HashMap<KeyStroke, Action>();

    public Frame() {
        super("Tabletop Creator v0.02");

        //GridBagConstraints gbc = new GridBagConstraints();
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
        centerPane = new CenterPane(game,commandStack);
        ColorLabelObserver colorLabelObserver = new ColorLabelObserver(toolbar);
        centerPane.addObserver(colorLabelObserver);
        centerPane.setOpaque(false);
        toolbar.setBorder(BorderFactory.createRaisedBevelBorder());

        componentCreationDialog = new ComponentCreationDialog();
        cardCreationDialog = new ComponentCreationDialog();
        pieceCreationDialog = new ComponentCreationDialog();
        textureCreationDialog = new ComponentCreationDialog();

        this.resizePane = new ResizeBoardPane(game);

        add(toolbar, BorderLayout.NORTH);
        add(centerPane, BorderLayout.CENTER);
        pack();

        toolbar.setStateListener(state -> {
            int userSelection;
            switch(state){
                case SAVE:
                    fileChooser.setDialogTitle("Save file");
                    userSelection = fileChooser.showSaveDialog(this);
                    if(userSelection == JFileChooser.APPROVE_OPTION){
                        File fileToSave = fileChooser.getSelectedFile();
                        System.out.println("Save as file:" + fileToSave.getAbsolutePath());
                        saveGame(game, fileToSave);
                    }
                    break;
                case OPEN:
                    fileChooser.setDialogTitle("Open file");
                    userSelection = fileChooser.showOpenDialog(this);
                    if(userSelection == JFileChooser.APPROVE_OPTION){
                        File fileToOpen = fileChooser.getSelectedFile();
                        System.out.println("Open file:" + fileToOpen.getAbsolutePath());
                        Game newGame = openGame(fileToOpen);
                        OpenGameCommand ogc = new OpenGameCommand(this,this.game,newGame);
                        commandStack.insertCommand(ogc);
                        centerPane.refreshComponentTree(game);
                        centerPane.updateBoard();
                    }
                    break;
                case CHANGE_SIZE:
                    resizePane.display();
                    ChangeSizeCommand csc = new ChangeSizeCommand(game,resizePane.getDesiredWidth(),resizePane.getDesiredHeight(),resizePane.getMargins());
                    commandStack.insertCommand(csc);
                    System.out.println(game.getBoard());
                    centerPane.updateBoard();
                    break;
                case COLOR_CHOOSE:
                    colorDialog.display();
                    Color color = colorDialog.getColor();
                    UpdateColorCommand ucc = new UpdateColorCommand(game,color);
                    commandStack.insertCommand(ucc);
                    centerPane.updateObservers();
                    break;
                case MESSAGE:
                    try {
                        new ClientWindow("localhost").start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case ADD_CARD:
                    addComponentDialog(ADD_CARD);
                    break;
                case ADD_PIECE:
                    addComponentDialog(ADD_PIECE);
                    break;
                case ADD_TEXTURE:
                    addComponentDialog(ADD_TEXTURE);
                    break;
                case UNDO:
                    undo();
                    break;
                case REDO:
                    redo();
                    break;
                case PLACE:
                    centerPane.boardPane.setPlacementType(toolbar.getPlacementType());
                    centerPane.updateBoard();
                    break;
                case CREATE_DECK:
                    addDeckDialog();
                    centerPane.refreshComponentTree(game);
                    break;
                case ADD_DECK:
                    DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)centerPane.componentTree.getTree().getLastSelectedPathComponent();
                    if(selectedNode != null){
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
                    }else{
                        addDeckDialog();
                    }
                    centerPane.refreshComponentTree(game);
                    break;
                case ADD_RULE:
                    break;
                default:
                    break;

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

    public void setFileChooserUI(Component[] comps){
        for(Component comp: comps) {
            if(comp instanceof Container) setFileChooserUI(((Container)comp).getComponents());
            try{
                comp.setFont(new Font("Segoe UI",Font.PLAIN,12));
            } catch(Exception e){
                e.printStackTrace();
            }//do nothing
            try{
                comp.setBackground(Color.WHITE);
            }catch(Exception e){
                e.printStackTrace();
            }
            if(comp instanceof JButton){
                JButton jb = (JButton)comp;
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

    private void addComponentDialog(StateListener.ButtonOutput state){
        int n = componentCreationDialog.display();
        if(n == JOptionPane.YES_OPTION){
            String name = componentCreationDialog.getComponentName();
            String text = componentCreationDialog.getComponentText();
            String fileSelected = componentCreationDialog.getFileSelect();

            Models.Component component = null;

            switch(state){
                case ADD_CARD:
                    component = new Card(name,text,fileSelected);
                    break;
                case ADD_PIECE:
                    component = new Piece(name,text,fileSelected);
                    break;
                case ADD_TEXTURE:
                    component = new Texture(name,text,fileSelected);
                    break;
                default:
                    break;
            }

            AddComponentCommand acc = new AddComponentCommand(game,component);
            commandStack.insertCommand(acc);

            centerPane.updateComponentTree(component);

            componentCreationDialog.clear();
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
