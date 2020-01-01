package UI;

import javax.swing.*;
import java.awt.*;

public class Frame extends JFrame {

    private JTextArea textArea;
    private JButton btn;

    public Frame() {
        super("Tabletop Creator v0.01");

        setLayout(new BorderLayout());

        textArea = new JTextArea();
        btn = new JButton("Roll die");

        add(textArea, BorderLayout.CENTER);
        add(btn, BorderLayout.SOUTH);

        setSize(1280,720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}
