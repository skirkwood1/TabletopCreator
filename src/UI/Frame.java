package UI;

import Commands.AddComponentCommand;
import Commands.ChangeSizeCommand;
import Commands.CommandStack;
import Commands.UpdateColorCommand;
import Models.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.HashMap;

public class Frame extends JFrame {

    private ComponentTree componentTree;
    private Toolbar toolbar;
    private TextPanel textPanel;
    private JButton cardBtn;
    private JButton pieceBtn;
    private CenterPane centerPane;
    private ComponentCreationDialog cardCreationDialog;
    private ComponentCreationDialog pieceCreationDialog;
    private JSplitPane buttonPane;

    private ResizeBoardPane resizePane;

    private JFileChooser fileChooser = new JFileChooser();
    private ColorChooseDialog colorDialog = new ColorChooseDialog();

    private Game game = new Game();
    private CommandStack commandStack = new CommandStack();

    private HashMap<KeyStroke, Action> actionMap = new HashMap<KeyStroke, Action>();

    public Frame() {
        super("Tabletop Creator v0.01");

        GridBagConstraints gbc = new GridBagConstraints();

        //setLayout(new GridBagLayout());
        setLayout(new BorderLayout());

        toolbar = new Toolbar(game);
        centerPane = new CenterPane(game,commandStack);
        cardBtn = new JButton("Add card");
        pieceBtn = new JButton("Add piece");

        buttonPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        buttonPane.setDividerSize(0);

        cardCreationDialog = new ComponentCreationDialog();
        pieceCreationDialog = new ComponentCreationDialog();

        buttonPane.add(cardBtn);
        buttonPane.add(pieceBtn);
        buttonPane.setResizeWeight(0.5);

        this.resizePane = new ResizeBoardPane(game);


        add(toolbar, BorderLayout.NORTH);

        add(centerPane, BorderLayout.CENTER);

        add(buttonPane, BorderLayout.SOUTH);

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
                    game = openGame(fileToOpen);
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

                UpdateColorCommand ucc = new UpdateColorCommand(game,color,toolbar);
                commandStack.insertCommand(ucc);

                //toolbar.updateColorLabel();
            }

            else if(text.equals("Undo\n\r")){
                commandStack.undo();
                //centerPane.refreshComponentTree(this.game);
                centerPane.updateBoard();
            }

            else if(text.equals("Redo\n\r")){
                commandStack.redo();
                centerPane.updateBoard();
            }

            else if(text.equals("Placement\n\r")){
                centerPane.boardPane.setPlacementType(toolbar.getPlacementType());
                centerPane.updateBoard();
            }
        });

        cardBtn.addActionListener(e -> {
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
        });

        pieceBtn.addActionListener(e -> {
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
        });

        setSize(1280,720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        keyboardSetup();
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

    private void keyboardSetup(){
        KeyStroke key1 = KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK);
        KeyStroke key2 = KeyStroke.getKeyStroke(KeyEvent.VK_Y, KeyEvent.CTRL_DOWN_MASK);

        actionMap.put(key1, new AbstractAction("action1") {
            @Override
            public void actionPerformed(ActionEvent e) {
                commandStack.undo();
                //centerPane.refreshComponentTree(this.game);
                centerPane.updateBoard();
                centerPane.refreshComponentTree(game);
            }
        });

        actionMap.put(key2, new AbstractAction("action2") {
            @Override
            public void actionPerformed(ActionEvent e) {
                commandStack.redo();
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
