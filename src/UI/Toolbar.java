package UI;

import Models.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Toolbar extends JPanel implements ActionListener {
    private JButton save;
    private JButton open;

    private StringListener stringListener;

    public Toolbar(){
        ImageIcon saveIcon = new ImageIcon("src/Images/icons8-save-100.png");
        Image image = saveIcon.getImage();
        Image newImg = image.getScaledInstance(25, 25, java.awt.Image.SCALE_SMOOTH);
        saveIcon = new ImageIcon(newImg);

        ImageIcon openIcon = new ImageIcon("src/Images/folder-open-outline-filled.png");
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

        setLayout(new FlowLayout(FlowLayout.LEFT));

        add(save);
        add(open);

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
