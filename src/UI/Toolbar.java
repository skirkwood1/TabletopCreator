package UI;

import Models.Game;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Toolbar extends JPanel implements ActionListener {
    private JPanel buttons;
    private JButton save;
    private JButton open;
    private JButton colorChoose;

    private ButtonGroup buttonGroup;
    private JToggleButton placeSpace;
    private JToggleButton placePiece;

    private JLabel colorLabel;

    private JMenuBar menuBar;
    private JMenu file,edit,add,decks;
    private JMenuItem copy,cut,paste,changeSize,saveMenu,openMenu,undo,redo,addPiece,addCard,addRule,addTexture;
    private JMenuItem createDeck,addToDeck;

    private StringListener stringListener;

    private Game game;

    private BoardPane.PlacementType placementType;


    public Toolbar(Game game){
        this.game = game;

        this.placementType = BoardPane.PlacementType.NONE;

        Color buttonBG = new Color(220,220,220);

        setLayout(new BorderLayout(0,0));
        //setBorder(BorderFactory.createLoweredBevelBorder());
        setOpaque(false);

        ImageIcon saveIcon = new ImageIcon(getClass().getClassLoader().getResource("icons8-save-100.png"));
        Image image = saveIcon.getImage();
        Image newImg = image.getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH);
        saveIcon = new ImageIcon(newImg);

        ImageIcon openIcon = new ImageIcon(getClass().getClassLoader().getResource("folder-open-outline-filled.png"));
        image = openIcon.getImage();
        newImg = image.getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH);
        openIcon = new ImageIcon(newImg);

        ImageIcon colorIcon = new ImageIcon(getClass().getClassLoader().getResource("img_colormap.gif"));
        image = colorIcon.getImage();
        newImg = image.getScaledInstance(25,25, Image.SCALE_SMOOTH);
        colorIcon = new ImageIcon(newImg);

        colorLabel = new JLabel();
        colorLabel.setBackground(Color.RED);
        colorLabel.setOpaque(true);
        colorLabel.setPreferredSize(new Dimension(30,30));

        ChangeListener changeListener = new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                AbstractButton button = (AbstractButton) e.getSource();
                ButtonModel model = button.getModel();

                if (model.isRollover()) {
                    button.setBackground(Color.LIGHT_GRAY);
                } else {
                    button.setBackground(new Color(220,220,220));
                }
            }
        };

        save = new JButton(saveIcon);
        save.setPreferredSize(new Dimension(30,30));
        save.setToolTipText("Save");
        //save.setMargin(new Insets(0,0,0,0));
        save.setBackground(buttonBG);
        save.setBorder(BorderFactory.createEmptyBorder());
        save.setFocusPainted(false);

        save.addChangeListener(changeListener);

        open = new JButton(openIcon);
        open.setPreferredSize(new Dimension(30,30));
        open.setToolTipText("Open");
        open.setMargin(new Insets(0,0,0,0));
        open.setBackground(buttonBG);
        open.setBorder(BorderFactory.createEmptyBorder());
        open.setFocusPainted(false);

        open.addChangeListener(changeListener);

        saveMenu = new JMenuItem("Save");
        saveMenu.setIcon(saveIcon);

        openMenu = new JMenuItem("Open");
        openMenu.setIcon(openIcon);

        colorChoose = new JButton(colorIcon);
        colorChoose.setPreferredSize(new Dimension(30,30));
        colorChoose.setMargin(new Insets(0,0,0,0));
        colorChoose.setToolTipText("Choose Color");
        colorChoose.setBackground(buttonBG);
        colorChoose.setBorder(BorderFactory.createEmptyBorder());
        colorChoose.setFocusPainted(false);

        colorChoose.addChangeListener(changeListener);

        placeSpace = new JToggleButton("Space");
        placeSpace.setPreferredSize(new Dimension(45,30));
        placeSpace.setMargin(new Insets(0,0,0,0));
        placeSpace.setBackground(buttonBG);
        placeSpace.setBorder(BorderFactory.createEmptyBorder());
        placeSpace.setFocusPainted(false);

        placeSpace.addChangeListener(changeListener);

        placePiece = new JToggleButton("Piece");
        placePiece.setPreferredSize(new Dimension(45,30));
        placePiece.setMargin(new Insets(0,0,0,0));
        placePiece.setBackground(buttonBG);
        placePiece.setBorder(BorderFactory.createEmptyBorder());
        placePiece.setFocusPainted(false);

        placePiece.addChangeListener(changeListener);

        save.addActionListener(this);
        open.addActionListener(this);
        colorChoose.addActionListener(this);
        placePiece.addActionListener(this);
        placeSpace.addActionListener(this);

        buttons = new JPanel();

        buttons.setLayout(new FlowLayout(FlowLayout.LEFT,2,2));
        buttons.setBackground(Color.WHITE);

//        buttonGroup = new ButtonGroup();
//        buttonGroup.add(placeSpace);
//        buttonGroup.add(placePiece);

        buttons.add(save);
        buttons.add(open);
        buttons.add(colorChoose);
        buttons.add(colorLabel);
        buttons.add(placeSpace);
        buttons.add(placePiece);

        menuBar = new JMenuBar();
        menuBar.setBackground(Color.WHITE);
        menuBar.setBorder(null);

        file = new JMenu("File");
        edit = new JMenu("Edit");
        add = new JMenu("Add");
        decks = new JMenu("Decks");

        cut = new JMenuItem("Cut");
        copy = new JMenuItem("Copy");
        paste = new JMenuItem("Paste");
        changeSize = new JMenuItem("Change Size");

        undo = new JMenuItem("Undo");
        redo = new JMenuItem("Redo");

        addPiece = new JMenuItem("Piece");
        addCard = new JMenuItem("Card");
        addRule = new JMenuItem("Rule");
        addTexture = new JMenuItem("Texture");

        createDeck = new JMenuItem("Create");
        addToDeck = new JMenuItem("Add");

        changeSize.addActionListener(this);
        saveMenu.addActionListener(this);
        openMenu.addActionListener(this);
        undo.addActionListener(this);
        redo.addActionListener(this);
        addPiece.addActionListener(this);
        addCard.addActionListener(this);
        addTexture.addActionListener(this);
        createDeck.addActionListener(this);
        addToDeck.addActionListener(this);

        file.add(cut);
        file.add(copy);
        file.add(saveMenu);
        file.add(openMenu);

        edit.add(paste);
        edit.add(changeSize);
        edit.add(undo);
        edit.add(redo);

        add.add(addPiece);
        add.add(addCard);
        add.add(addRule);
        add.add(addTexture);

        decks.add(createDeck);
        decks.add(addToDeck);

        menuBar.add(file);
        menuBar.add(edit);
        menuBar.add(add);
        menuBar.add(decks);

        add(menuBar,BorderLayout.NORTH);
        add(buttons,BorderLayout.SOUTH);


    }

    public void setStringListener(StringListener listener){
        this.stringListener = listener;
    }

    public void actionPerformed(ActionEvent e) {
        JComponent c = (JComponent)e.getSource();

        if (c == save || c == saveMenu){
            stringListener.textEmitted("Save\n\r");

        }
        else if (c == open || c == openMenu){
            stringListener.textEmitted("Open\n\r");
        }
        else if (c == changeSize){
            stringListener.textEmitted("ChangeSize\n\r");
            System.out.println("Change Size");
        }

        else if (c == colorChoose){
            stringListener.textEmitted("ColorChooser\n\r");
            System.out.println("Choose Color");

        }

        else if (c == addPiece){
            stringListener.textEmitted("AddPiece\n\r");
            System.out.println("Add Piece");
        }

        else if (c == addCard){
            stringListener.textEmitted("AddCard\n\r");
            System.out.println("Add Card");
        }

        else if (c == addTexture){
            stringListener.textEmitted("AddTexture\n\r");
            System.out.println("Add Texture");
        }

        else if (c == undo){
            stringListener.textEmitted("Undo\n\r");
            //System.out.println("Undo");
        }

        else if (c == redo){
            stringListener.textEmitted("Redo\n\r");
            //System.out.println("Redo");
        }

        else if (c == createDeck){
            stringListener.textEmitted("CreateDeck\n\r");
        }

        else if (c == addToDeck){
            stringListener.textEmitted("AddToDeck\n\r");
        }


        else if(c == placeSpace || c == placePiece){
            if(c == placeSpace && placePiece.isSelected()){
                placePiece.setSelected(false);
            }
            else if(c == placePiece && placeSpace.isSelected()){
                placeSpace.setSelected(false);
            }

            if(placePiece.isSelected()){
                this.placementType = BoardPane.PlacementType.PIECE;
            }
            else if(placeSpace.isSelected()){
                this.placementType = BoardPane.PlacementType.SPACE;
            }
            else{
                this.placementType = BoardPane.PlacementType.NONE;
            }

            stringListener.textEmitted("Placement\n\r");
        }

    }

    public void updateColorLabel(){
        this.colorLabel.setBackground(game.getBoard().getColor());
    }

    public BoardPane.PlacementType getPlacementType(){
        return this.placementType;
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
}
