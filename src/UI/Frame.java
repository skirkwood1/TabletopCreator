package UI;

import Models.Dice;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class Frame extends JFrame {

    private ComponentTree componentTree;
    private Toolbar toolbar;
    private TextPanel textPanel;
    private JButton btn;
    private CenterPane centerPane;

    Dice die = new Dice(6);

    public Frame() {
        super("Tabletop Creator v0.01");

        setLayout(new BorderLayout());

        toolbar = new Toolbar();
        centerPane = new CenterPane();
        btn = new JButton("Roll die");


        add(toolbar, BorderLayout.NORTH);
        add(centerPane, BorderLayout.CENTER);
        add(btn, BorderLayout.SOUTH);
        pack();

        toolbar.setStringListener(text -> centerPane.appendText(text));

        btn.addActionListener(e -> centerPane.appendText(Dice.dieList.get(0).roll() + ""));

        setSize(1280,720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}
