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
    private JButton colorChoose;

    private JLabel colorLabel;

    private JMenuBar menuBar;
    private JMenu file,edit;
    private JMenuItem copy,cut,paste,changeSize,saveMenu,openMenu;

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

        ImageIcon colorIcon = new ImageIcon(getClass().getClassLoader().getResource("img_colormap.gif"));
        image = colorIcon.getImage();
        newImg = image.getScaledInstance(20,20, Image.SCALE_SMOOTH);
        colorIcon = new ImageIcon(newImg);

        colorLabel = new JLabel();
        colorLabel.setBackground(Color.RED);
        colorLabel.setOpaque(true);
        colorLabel.setPreferredSize(new Dimension(25,25));

        save = new JButton(saveIcon);
        save.setPreferredSize(new Dimension(25,25));
        save.setToolTipText("Save");

        open = new JButton(openIcon);
        open.setPreferredSize(new Dimension(25,25));
        open.setToolTipText("Open");

        saveMenu = new JMenuItem("Save");
        saveMenu.setIcon(saveIcon);

        openMenu = new JMenuItem("Open");
        openMenu.setIcon(openIcon);

        colorChoose = new JButton(colorIcon);
        colorChoose.setPreferredSize(new Dimension(25,25));
        colorChoose.setToolTipText("Choose Color");

        save.addActionListener(this);
        open.addActionListener(this);
        colorChoose.addActionListener(this);

        buttons = new JPanel();

        buttons.setLayout(new FlowLayout(FlowLayout.LEFT));

        buttons.add(save);
        buttons.add(open);
        buttons.add(colorChoose);
        buttons.add(colorLabel);

        menuBar = new JMenuBar();
        file = new JMenu("File");
        edit = new JMenu("Edit");

        cut = new JMenuItem("Cut");
        copy = new JMenuItem("Copy");
        paste = new JMenuItem("Paste");
        changeSize = new JMenuItem("Change Size");

        changeSize.addActionListener(this);
        saveMenu.addActionListener(this);
        openMenu.addActionListener(this);

        file.add(cut);
        file.add(copy);
        file.add(saveMenu);
        file.add(openMenu);

        edit.add(paste);
        edit.add(changeSize);

        menuBar.add(file);
        menuBar.add(edit);

        add(menuBar,BorderLayout.NORTH);
        add(buttons,BorderLayout.SOUTH);



    }

    public void setStringListener(StringListener listener){
        this.stringListener = listener;
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == save || e.getSource() == saveMenu){
            stringListener.textEmitted("Save\n\r");

        }
        if (e.getSource() == open || e.getSource() == openMenu){
            stringListener.textEmitted("Open\n\r");
        }
        if (e.getSource() == changeSize){
            stringListener.textEmitted("ChangeSize\n\r");
            System.out.println("Change Size");
        }

        if (e.getSource() == colorChoose){
            stringListener.textEmitted("ColorChooser\n\r");
            System.out.println("Choose Color");

        }

    }

    public void updateColorLabel(Color c){
        this.colorLabel.setBackground(c);
    }
}
