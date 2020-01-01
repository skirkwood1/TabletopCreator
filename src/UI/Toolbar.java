package UI;

import javax.swing.*;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Toolbar extends JPanel implements ActionListener {
    private JButton save;
    private JButton open;

    public Toolbar(){
        save = new JButton("Save");
        open = new JButton("Open");

        save.addActionListener(this);
        open.addActionListener(this);

        setLayout(new FlowLayout(FlowLayout.LEFT));

        add(save);
        add(open);



    }

    public void actionPerformed(ActionEvent e) {
        System.out.println(2);
    }
}
