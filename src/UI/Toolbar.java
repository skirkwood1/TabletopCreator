package UI;

import Models.Game;
import Observers.Observer;
import UI.UIHelpers.AntialiasButton;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.metal.MetalToggleButtonUI;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class Toolbar extends JPanel implements ActionListener,Observable {
    private JPanel buttons;
    private JButton save;
    private JButton open;
    private JButton colorChoose;
    private JButton message;

    private ButtonGroup buttonGroup;
//    private JToggleButton placeSpace;
//    private JToggleButton placePiece;
//    private JToggleButton placeCard;
//    private JToggleButton placeResource;

    private JToggleButton place,move;

    private JLabel colorLabel;

    private JMenuBar menuBar;
    private JMenu file,edit,add,decks;
    private JMenuItem createDeck,addToDeck;

    private StateListener stateListener;

    private Game game;

    private BoardPane.PlacementType placementType;

    private HashMap<JComponent,StateListener.ButtonOutput> actionDescription;

    private MetalToggleButtonUI mtbUI = new MetalToggleButtonUI() {
        @Override
        protected Color getSelectColor() {
            return new Color(170,170,170);
        }
    };
    private Color buttonBG = new Color(210,210,210);
    private ChangeListener changeListener = new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
            AbstractButton button = (AbstractButton) e.getSource();
            ButtonModel model = button.getModel();

            if (model.isRollover()) {
                button.setBackground(Color.LIGHT_GRAY);
            } else {
                button.setBackground(buttonBG);
            }
        }
    };

    private ArrayList<Observer> observers;

    public Toolbar(Game game, ArrayList<Observer> observers){
        this.game = game;
        this.observers = observers;

        this.placementType = BoardPane.PlacementType.NONE;
        this.actionDescription = new HashMap<>();

        UIManager.put("MenuBar.border",BorderFactory.createEmptyBorder());

        UIManager.put("Menu.selectionBackground", Color.LIGHT_GRAY);
        UIManager.put("Menu.selectionForeground", Color.BLACK);
        UIManager.put("Menu.background", Color.WHITE);
        UIManager.put("Menu.foreground", Color.BLACK);
        UIManager.put("Menu.opaque", false);
        UIManager.put("Menu.border",BorderFactory.createEmptyBorder(3,1,3,1));
        UIManager.put("Menu.font",new Font("Segoe UI",Font.PLAIN,14));

        UIManager.put("MenuItem.background",Color.WHITE);
        UIManager.put("MenuItem.selectionBackground",buttonBG);
        UIManager.put("MenuItem.background",Color.WHITE);
        UIManager.put("MenuItem.foreground",Color.BLACK);
        UIManager.put("MenuItem.border",BorderFactory.createEmptyBorder(2,2,2,2));
        UIManager.put("MenuItem.font",new Font("Segoe UI",Font.PLAIN,14));

        UIManager.put("ToolTip.font", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("ToolTip.background", Color.WHITE);
        UIManager.put("ToolTip.foreground", Color.BLACK);
        UIManager.put("ToolTip.border", BorderFactory.createLineBorder(Color.BLACK,1));

        setLayout(new BorderLayout(0,0));
        //setBorder(BorderFactory.createLoweredBevelBorder());
        setOpaque(false);

        BufferedImage saveIcon = null;
        BufferedImage openIcon = null;
        BufferedImage messageIcon = null;
        try {
            saveIcon = ImageIO.read(getClass().getClassLoader().getResource("icons8-save-100.png"));
            openIcon = ImageIO.read(getClass().getClassLoader().getResource("folder-open-outline-filled.png"));
            //messageIcon = createIcon(getClass().getClassLoader().getResource("ChatIconSmall.png"));
            messageIcon = ImageIO.read(getClass().getClassLoader().getResource("SendIcon.png"));
        }catch(IOException ie){

        }
        colorLabel = new JLabel();
        colorLabel.setBackground(Color.RED);
        colorLabel.setOpaque(true);
        colorLabel.setPreferredSize(new Dimension(36,36));

        MouseListener menuListener = new MouseAdapter(){
            @Override
            public void mouseEntered(MouseEvent e) {
                JMenu item = (JMenu) e.getSource(); // is this implementation
                // correct ?
                item.setSelected(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                JMenu item = (JMenu) e.getSource();
                item.setSelected(false);
            }
        };

        save = new AntialiasButton(saveIcon);
        save.setToolTipText("Save");
        initButton(save);
        actionDescription.put(save, StateListener.ButtonOutput.SAVE);

        open = new AntialiasButton(openIcon);
        open.setToolTipText("Open");
        initButton(open);
        actionDescription.put(open, StateListener.ButtonOutput.OPEN);

        JMenuItem changeSize,saveMenu,openMenu,undo,redo,addPiece,addCard,addRule,addTexture,addResource,addPlayer;

        saveMenu = new JMenuItem("Save");
        saveMenu.setIcon(new ImageIcon(saveIcon.getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
        actionDescription.put(saveMenu, StateListener.ButtonOutput.SAVE);

        openMenu = new JMenuItem("Open");
        openMenu.setIcon(new ImageIcon(openIcon.getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
        actionDescription.put(openMenu, StateListener.ButtonOutput.OPEN);

        System.out.println(javax.swing.UIManager.getDefaults().getFont("Label.font"));

        colorChoose = new JButton("⬛");
        colorChoose.setToolTipText("Choose Color");
        initButton(colorChoose);
        colorChoose.setFont(new Font("Dialog",Font.PLAIN,30));
        colorChoose.setForeground(game.getBoard().getColor());
        actionDescription.put(colorChoose, StateListener.ButtonOutput.COLOR_CHOOSE);

        message = new AntialiasButton(messageIcon);
        message.setToolTipText("Chat Room");
        initButton(message);
        actionDescription.put(message, StateListener.ButtonOutput.MESSAGE);

        place = new JToggleButton("Place");
        initToggleButton(place);
        actionDescription.put(place, StateListener.ButtonOutput.PLACE);

        move = new JToggleButton("Move");
        initToggleButton(move);
        actionDescription.put(move, StateListener.ButtonOutput.MOVE);

//        placeSpace = new JToggleButton("Space");
//        initToggleButton(placeSpace);
//        actionDescription.put(placeSpace, StateListener.ButtonOutput.PLACE);
//
//        placePiece = new JToggleButton("Piece");
//        initToggleButton(placePiece);
//        actionDescription.put(placePiece, StateListener.ButtonOutput.PLACE);
//
//        placeCard = new JToggleButton("Card");
//        initToggleButton(placeCard);
//        actionDescription.put(placeCard, StateListener.ButtonOutput.PLACE);
//
//        placeResource = new JToggleButton("Resource");
//        initToggleButton(placeResource);
//        actionDescription.put(placeResource, StateListener.ButtonOutput.PLACE);

        buttons = new JPanel();

        buttons.setLayout(new FlowLayout(FlowLayout.LEFT,2,0));
        //buttons.setBackground(new Color(230,230,230));
        buttons.setBackground(Color.WHITE);
        buttons.setBorder(BorderFactory.createEmptyBorder(2,0,2,0));

//        buttonGroup = new ButtonGroup();
//        buttonGroup.add(placeSpace);
//        buttonGroup.add(placePiece);

        buttons.add(save);
        buttons.add(open);
        buttons.add(colorChoose);
        buttons.add(message);
//        buttons.add(placeSpace);
//        buttons.add(placePiece);
//        buttons.add(placeCard);
//        buttons.add(placeResource);
        buttons.add(move);
        buttons.add(place);

        buttons.add(colorLabel);

        menuBar = new JMenuBar();
        menuBar.setBackground(Color.WHITE);
        menuBar.setBorder(null);

        file = new JMenu("File");
        edit = new JMenu("Edit");
        add = new JMenu("Add");
        decks = new JMenu("Decks");

        file.setRolloverEnabled(true);
        file.addMouseListener(menuListener);

        edit.setRolloverEnabled(true);
        edit.addMouseListener(menuListener);

        add.setRolloverEnabled(true);
        add.addMouseListener(menuListener);

        decks.setRolloverEnabled(true);
        decks.addMouseListener(menuListener);

        changeSize = new JMenuItem("Change Size");
        actionDescription.put(changeSize, StateListener.ButtonOutput.CHANGE_SIZE);

        undo = new JMenuItem("Undo");
        actionDescription.put(undo, StateListener.ButtonOutput.UNDO);

        redo = new JMenuItem("Redo");
        actionDescription.put(redo, StateListener.ButtonOutput.REDO);

        addPiece = new JMenuItem("Piece");
        actionDescription.put(addPiece, StateListener.ButtonOutput.ADD_PIECE);

        addCard = new JMenuItem("Card");
        actionDescription.put(addCard, StateListener.ButtonOutput.ADD_CARD);

        addRule = new JMenuItem("Rule");
        actionDescription.put(addRule, StateListener.ButtonOutput.ADD_RULE);

        addTexture = new JMenuItem("Texture");
        actionDescription.put(addTexture, StateListener.ButtonOutput.ADD_TEXTURE);

        addResource = new JMenuItem("Resource");
        actionDescription.put(addResource, StateListener.ButtonOutput.ADD_RESOURCE);

        addPlayer = new JMenuItem("Player");
        actionDescription.put(addPlayer,StateListener.ButtonOutput.ADD_PLAYER);

        createDeck = new JMenuItem("Create");
        actionDescription.put(createDeck, StateListener.ButtonOutput.CREATE_DECK);

        addToDeck = new JMenuItem("Add");
        actionDescription.put(addToDeck, StateListener.ButtonOutput.ADD_DECK);

        save.addActionListener(this);
        open.addActionListener(this);
        colorChoose.addActionListener(this);
        message.addActionListener(this);
        //placePiece.addActionListener(this);
        //placeSpace.addActionListener(this);
        //placeCard.addActionListener(this);
        //placeResource.addActionListener(this);
        place.addActionListener(this);
        move.addActionListener(this);
        changeSize.addActionListener(this);
        saveMenu.addActionListener(this);
        openMenu.addActionListener(this);
        undo.addActionListener(this);
        redo.addActionListener(this);
        addPiece.addActionListener(this);
        addCard.addActionListener(this);
        addTexture.addActionListener(this);
        addPlayer.addActionListener(this);
        createDeck.addActionListener(this);
        addToDeck.addActionListener(this);
        addResource.addActionListener(this);

        //file.add(cut);
        //file.add(copy);
        file.add(saveMenu);
        file.add(openMenu);

        //edit.add(paste);
        edit.add(changeSize);
        edit.add(undo);
        edit.add(redo);

        add.add(addPiece);
        add.add(addCard);
        //add.add(addRule);
        add.add(addTexture);
        add.add(addResource);
        add.add(addPlayer);

        decks.add(createDeck);
        decks.add(addToDeck);

        menuBar.add(file);
        menuBar.add(edit);
        menuBar.add(add);
        menuBar.add(decks);

        add(menuBar,BorderLayout.NORTH);
        add(buttons,BorderLayout.SOUTH);

        move.setSelected(true);
    }

    public void setStateListener(StateListener listener){
        this.stateListener = listener;
    }

    public void actionPerformed(ActionEvent e) {
        JComponent c = (JComponent)e.getSource();

        //if(c == placeSpace || c == placePiece || c == placeCard || c == placeResource){
//            if(c == placeSpace){
//                placePiece.setSelected(false);
//                placeCard.setSelected(false);
//                placeResource.setSelected(false);
//            }
//            else if(c == placePiece){
//                placeSpace.setSelected(false);
//                placeCard.setSelected(false);
//                placeResource.setSelected(false);
//            }
//            else if(c == placeCard){
//                placeSpace.setSelected(false);
//                placePiece.setSelected(false);
//                placeResource.setSelected(false);
//            }
//            else if(c == placeResource){
//                placeSpace.setSelected(false);
//                placePiece.setSelected(false);
//                placeCard.setSelected(false);
//            }
//
//            if(placePiece.isSelected()){
//                this.placementType = BoardPane.PlacementType.PIECE;
//            }
//            else if(placeSpace.isSelected()){
//                this.placementType = BoardPane.PlacementType.SPACE;
//            }
//            else if(placeCard.isSelected()){
//                this.placementType = BoardPane.PlacementType.CARD;
//            }
//            else if(placeResource.isSelected()){
//                this.placementType = BoardPane.PlacementType.RESOURCE;
//            }
//            else{
//                this.placementType = BoardPane.PlacementType.NONE;
//            }
//        }
//
//        stateListener.stateEmitted(actionDescription.get(c));
        //System.out.println(placementType);

        if(c == place || c == move){
            if(c == place){
                move.setSelected(false);
                place.setSelected(true);
            }else{
                place.setSelected(false);
                move.setSelected(true);
            }
        }

        stateListener.stateEmitted(actionDescription.get(c));
        updateObservers();
    }

    public void updateColorLabel(){
        this.colorLabel.setBackground(game.getBoard().getColor());
        if(game.getBoard().useTexture()){
            Image image = game.getBoard().getTextureImage().getScaledInstance(36, 36, Image.SCALE_SMOOTH);
            this.colorLabel.setIcon(new ImageIcon(image));
        }else{
            this.colorLabel.setIcon(null);
        }
        this.colorChoose.setForeground(game.getBoard().getColor());
    }

    public BoardPane.PlacementType getPlacementType(){
        return this.placementType;
    }

    public void setGame(Game game){
        this.game = game;
    }

    public ImageIcon createIcon(URL iconPath) throws IOException {
        Image img = ImageIO.read(iconPath).getScaledInstance(32, 32, java.awt.Image.SCALE_SMOOTH);
        return new ImageIcon(img);

    }

    public ChangeListener changeListener(JButton button){
        return new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                DefaultButtonModel model = (DefaultButtonModel) e.getSource();

                if (model.isRollover()) {
                    button.setBackground(Color.LIGHT_GRAY);
                } else {
                    button.setBackground(new Color(220,220,220));
                }
            }
        };
    }

    public void initToggleButton(JToggleButton button){
        button.setPreferredSize(new Dimension(48,36));
        button.setMargin(new Insets(0,0,0,0));
        button.setBackground(buttonBG);
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setFocusPainted(false);
        button.setRolloverEnabled(true);
        button.setFont(new Font("Segoe UI",Font.BOLD,13));
        button.addChangeListener(changeListener);
        button.setUI(mtbUI);
    }

    public void initButton(JButton button){
        button.setPreferredSize(new Dimension(36,36));
        button.setMargin(new Insets(0,0,0,0));
        button.setBackground(buttonBG);
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setFocusPainted(false);
        button.addChangeListener(changeListener);
        button.setUI(mtbUI);
    }
}
