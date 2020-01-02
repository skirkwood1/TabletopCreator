package UI;

import Models.*;

import javax.swing.*;
import java.awt.*;
import java.io.*;

public class Frame extends JFrame {

    private ComponentTree componentTree;
    private Toolbar toolbar;
    private TextPanel textPanel;
    private JButton btn;
    private CenterPane centerPane;

    private JFileChooser fileChooser = new JFileChooser();

    Game game = new Game();

    public Frame() {
        super("Tabletop Creator v0.01");

        setLayout(new BorderLayout());

        toolbar = new Toolbar();
        centerPane = new CenterPane(game);
        btn = new JButton("Add card");


        add(toolbar, BorderLayout.NORTH);
        add(centerPane, BorderLayout.CENTER);
        add(btn, BorderLayout.SOUTH);
        pack();

        toolbar.setStringListener(text -> {
            centerPane.appendText(text);
            if(text.equals("Save\n\r")){
                fileChooser.setDialogTitle("Save file");
                int userSelection = fileChooser.showSaveDialog(this);
                if(userSelection == JFileChooser.APPROVE_OPTION){
                    File fileToSave = fileChooser.getSelectedFile();
                    System.out.println("Save as file:" + fileToSave.getAbsolutePath());
                    saveGame(game, fileToSave.getAbsolutePath());
                }
            }
        });

        btn.addActionListener(e -> {
            Card card = game.addCard("test","test","src/Images/folder-open-outline-filled.png");
            centerPane.updateComponentTree(card);
        });

        setSize(1280,720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void saveGame(Game game, String filePath){
        try{
            FileOutputStream fileOut = new FileOutputStream(filePath);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(game);
            objectOut.close();
            System.out.println("File saved");
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
