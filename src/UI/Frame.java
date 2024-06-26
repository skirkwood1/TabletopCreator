package UI;

import ChatServer.ClientWindow;
import ChatServer.GameListener;
import Commands.*;
import Models.*;
import Observers.ColorLabelObserver;
import Observers.PlacementTypeObserver;
import UI.UIHelpers.FileChooserCreator;
import UI.UIHelpers.FileViewerUI;
import UI.UIHelpers.Icons.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

import static UI.StateListener.ButtonOutput.*;

public class Frame extends JFrame implements Observable {

    private ComponentTree componentTree;
    private Toolbar toolbar;
    private JTextField textPanel,cmd;
    private CommandLog cmdOutput;
    private CenterPane centerPane;
    private ComponentCreationDialog cardCreationDialog;
    private ComponentCreationDialog pieceCreationDialog;
    private ComponentCreationDialog textureCreationDialog;
    private ComponentCreationDialog componentCreationDialog;
    private ResourceCreationDialog resourceCreationDialog;
    private JSplitPane buttonPane,cmdPane,mainPane;

    private ResizeBoardPane resizePane;

    private JFileChooser fileChooser;
    private ColorChooseDialog colorDialog = new ColorChooseDialog();

    private Game game = new Game(10,10);
    private CommandStack commandStack = new CommandStack();

    private ClientWindow clientWindow;
    private GameListener gameListener;

    private HashMap<KeyStroke, Action> actionMap = new HashMap<>();

    public Frame() {
        super("Tabletop Creator v0.1");

        ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("ProgramIcon.png"));
        this.setIconImage(icon.getImage());

        //GridBagConstraints gbc = new GridBagConstraints();
        //setLayout(new GridBagLayout());
        setLayout(new BorderLayout());

//        try{
//            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//        }
//        catch(Exception e){}

        UIManager.put("Separator.foreground",Color.BLACK);
        UIManager.put("Separator.background",Color.BLACK);

        UIManager.put("FileChooser.upFolderIcon", new UpIcon());
        UIManager.put("FileChooser.homeFolderIcon", new HomeIcon());
        UIManager.put("FileChooser.listViewIcon", new FileGridViewIcon());
        UIManager.put("FileChooser.detailsViewIcon", new FileDetailViewIcon());
        UIManager.put("FileChooser.newFolderIcon", new FolderIcon());

        this.fileChooser = new JFileChooser();
        fileChooser.setBackground(Color.WHITE);
        fileChooser.setOpaque(true);
        fileChooser.setBorder(BorderFactory.createEmptyBorder(5,10,5,10));
        fileChooser.setFileView(new FileViewerUI());
        Component[] comp =  fileChooser.getComponents();
        FileChooserCreator.setFileChooserElements(comp);

        centerPane = new CenterPane(game,commandStack);
        PlacementTypeObserver pto = new PlacementTypeObserver(centerPane);
        observers.add(pto);
        toolbar = new Toolbar(game,observers);

        ColorLabelObserver colorLabelObserver = new ColorLabelObserver(toolbar);
        centerPane.addObserver(colorLabelObserver);
        centerPane.setOpaque(false);
        toolbar.setBorder(BorderFactory.createRaisedBevelBorder());

        componentCreationDialog = new ComponentCreationDialog();
        cardCreationDialog = new ComponentCreationDialog();
        pieceCreationDialog = new ComponentCreationDialog();
        textureCreationDialog = new ComponentCreationDialog();

        resourceCreationDialog = new ResourceCreationDialog();

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
                        game.setName(fileToSave.getName());
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
                        OpenGameCommand ogc = new OpenGameCommand(this,newGame);
                        commandStack.insertCommand(ogc);
                        centerPane.updateObservers();
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
                    centerPane.setPlacementType(BoardPane.PlacementType.SPACE);
                    System.out.println(centerPane.getPlacementType());
                    UpdateColorCommand ucc = new UpdateColorCommand(game,color);
                    commandStack.insertCommand(ucc);
                    updateObservers();
                    break;
                case MESSAGE:
                    try {
                        this.clientWindow = new ClientWindow("localhost");

                        clientWindow.setGameListener(game -> {
                            OpenGameCommand ogc = new OpenGameCommand(this,game);
                            commandStack.insertCommand(ogc);
                        });

                        clientWindow.start();
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
                case ADD_PLAYER:
                    addPlayerDialog();
                    centerPane.refreshComponentTree(game);
                    centerPane.updateBoard();
                    break;
                case UNDO:
                    undo();
                    break;
                case REDO:
                    redo();
                    break;
                case PLACE:
                    centerPane.setPlace(true);
                    //centerPane.boardPane.setPlacementType(toolbar.getPlacementType());
                    centerPane.updateBoard();
                    break;
                case MOVE:
                    centerPane.setPlace(false);
                    centerPane.boardPane.setPlacementType(BoardPane.PlacementType.NONE);
                    updateObservers();
                    centerPane.updateBoard();
                    break;
                case CREATE_DECK:
                    addDeckDialog();
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
                    //centerPane.refreshComponentTree(game);
                    break;
                case ADD_RULE:
                    break;

                case ADD_RESOURCE:
                    addResourceDialog();
                    centerPane.refreshComponentTree(game);
                    centerPane.updateBoard();
                    break;
                default:
                    break;

            }
        });

        setSize(1280,720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                if(clientWindow != null){
                    clientWindow.closeFrame();
                }
            }
        });

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

    public void setGame(Game game){
        this.game = game;
        this.toolbar.setGame(game);
        this.centerPane.refreshComponentTree(game);
        this.updateBoard();
    }

    public Game getGame(){
        return this.game;
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

            ComponentImage ci = new ComponentImage(fileSelected);

            GameComponent component = null;
            ResourceSheet resourceSheet = null;

            switch(state){
                case ADD_CARD:
                    try{
                        ComponentImage cardBack = new ComponentImage(ImageIO.read(getClass().getClassLoader().getResource("cardback.png")));
                        component = new Card(name,text,ci,cardBack);
                    }catch(IOException ie){
                        component = new Card(name,text,ci);
                    }

                    break;
                case ADD_PIECE:
                    component = new Piece(name,text,ci);
                    break;
                case ADD_TEXTURE:
                    component = new Texture(name,text,ci);
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

    private void addPlayerDialog(){
        PlayerCreationDialog playerCreationDialog = new PlayerCreationDialog(game);
        int n = playerCreationDialog.display();

        if(n == JOptionPane.YES_OPTION){
            ArrayList<ResourceSheet> resourceSheets = new ArrayList<>();
            for(ResourceSheet resourceSheet : playerCreationDialog.getSelection()){
                resourceSheets.add(resourceSheet);
            }
            Player player = new Player(playerCreationDialog.getPlayerName(), resourceSheets);

            commandStack.insertCommand(new AddComponentCommand(game,player));
            //game.addPlayer(player);
            //game.createDeck(deck);
            centerPane.updateComponentTree(player);
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
            CreateDeckCommand cdc = new CreateDeckCommand(game,deck);
            commandStack.insertCommand(cdc);
            //game.createDeck(deck);
            centerPane.updateComponentTree(deck);
        }
    }

    private void addDeckDialog(Deck deck){
        DeckCreationDialog deckCreationDialog = new DeckCreationDialog(game);
        int n = deckCreationDialog.displayNoName();

        ArrayList<Card> cards = new ArrayList<>();
        if(n == JOptionPane.YES_OPTION){
            for(Card card: deckCreationDialog.getSelection()){
                    cards.add(card);
                    //deck.addCard(card);
            }
            AddToDeckCommand adc = new AddToDeckCommand(deck,cards,deckCreationDialog.getNumCopies());
            commandStack.insertCommand(adc);
            centerPane.updateComponentTree(deck);
        }
    }

    private void addResourceDialog(){
        int n = resourceCreationDialog.display();
        if(n == JOptionPane.YES_OPTION){
            ResourceSheet resourceSheet = new ResourceSheet(
                    resourceCreationDialog.getTitleField(),
                    resourceCreationDialog.getValMap());

            AddComponentCommand acc = new AddComponentCommand(game, resourceSheet);
            commandStack.insertCommand(acc);
            centerPane.updateComponentTree(resourceSheet);
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
                centerPane.boardPane.deleteSelection();
                //centerPane.boardPane.deleteSelectedSpaces();
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
