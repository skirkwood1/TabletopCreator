package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class Toolbar extends JPanel implements ActionListener {
    private JPanel buttons;
    private JButton save;
    private JButton open;

    private JMenuBar menuBar;
    private JMenu file,edit;
    private JMenuItem copy,cut,paste;

    private StringListener stringListener;


    public Toolbar(){
        setLayout(new BorderLayout());

        ImageIcon saveIcon = new ImageIcon(getClass().getClassLoader().getResource("icons8-save-100.png"));
        Image image = saveIcon.getImage();
        Image newImg = image.getScaledInstance(25, 25, java.awt.Image.SCALE_SMOOTH);
        saveIcon = new ImageIcon(newImg);

        ImageIcon openIcon = new ImageIcon(getClass().getClassLoader().getResource("folder-open-outline-filled.png"));
        image = openIcon.getImage();
        newImg = image.getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH);
        openIcon = new ImageIcon(newImg);

        save = new JButton(saveIcon);
        save.setPreferredSize(new Dimension(25,25));
        save.setToolTipText("Save");

        open = new JButton(openIcon);
        open.setPreferredSize(new Dimension(25,25));
        open.setToolTipText("Open");

        save.addActionListener(this);
        open.addActionListener(this);

        buttons = new JPanel();

        buttons.setLayout(new FlowLayout(FlowLayout.LEFT));

        buttons.add(save);
        buttons.add(open);

        menuBar = new JMenuBar();
        file = new JMenu("File");
        edit = new JMenu("Edit");
        cut = new JMenuItem("Cut");
        copy = new JMenuItem("Copy");
        paste = new JMenuItem("Paste");

        file.add(cut);
        file.add(copy);
        edit.add(paste);

        menuBar.add(file);
        menuBar.add(edit);

        add(menuBar,BorderLayout.NORTH);
        add(buttons,BorderLayout.SOUTH);



    }

    public void setStringListener(StringListener listener){
        this.stringListener = listener;
    }

    public void actionPerformed(ActionEvent e) {
        JButton clicked = (JButton)e.getSource();

        if (clicked == save){
            stringListener.textEmitted("Save\n\r");

        }
        else if (clicked == open){
            stringListener.textEmitted("Open\n\r");
        }

    }
}
