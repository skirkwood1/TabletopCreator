package UI;

import Models.*;

import javax.swing.*;
import java.awt.*;
import java.io.*;

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

    Game game = new Game();

    public Frame() {
        super("Tabletop Creator v0.01");

        GridBagConstraints gbc = new GridBagConstraints();

        //setLayout(new GridBagLayout());
        setLayout(new BorderLayout());

        toolbar = new Toolbar();
        centerPane = new CenterPane(game);
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

                game.getBoard().setSize(resizePane.getDesiredWidth(),resizePane.getDesiredHeight());
                System.out.println(game.getBoard());
                centerPane.updateBoard();


            }
            else if(text.equals("ColorChooser\n\r")){
                colorDialog.display();
                Color color = colorDialog.getColor();
                centerPane.updateColor(color);
                toolbar.updateColorLabel(color);
            }
        });

        cardBtn.addActionListener(e -> {
            int n = cardCreationDialog.display();
            if(n == JOptionPane.YES_OPTION){
                String cardName = cardCreationDialog.getComponentName();
                String cardText = cardCreationDialog.getComponentText();
                String fileSelected = cardCreationDialog.getFileSelect();

                Card card = game.addCard(
                     cardName,cardText,fileSelected);
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

                Piece piece = game.addPiece(
                        pieceName,pieceText,fileSelected);
                centerPane.updateComponentTree(piece);

                pieceCreationDialog.clear();
            }
        });

        setSize(1280,720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void saveGame(Game game, File file){
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

    public Game openGame(File file){
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
}
