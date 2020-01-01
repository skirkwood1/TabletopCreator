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
    private JSplitPane pane;

    Dice die = new Dice(6);

    public Frame() {
        super("Tabletop Creator v0.01");

        setLayout(new BorderLayout());

        toolbar = new Toolbar();
        textPanel = new TextPanel();
        componentTree = new ComponentTree();
        btn = new JButton("Roll die");

        pane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,componentTree,textPanel);
        pane.addPropertyChangeListener("dividerLocation", new PropertyChangeListener()
        {
            @Override
            public void propertyChange(PropertyChangeEvent e)
            {
                int location = ((Integer)e.getNewValue()).intValue();
                System.out.println(location);

                if (location > 150)
                {
                    JSplitPane splitPane = (JSplitPane)e.getSource();
                    splitPane.setDividerLocation( 150 );
                }
            }
        });

        add(toolbar, BorderLayout.NORTH);
        add(pane, BorderLayout.CENTER);
        add(btn, BorderLayout.SOUTH);

        toolbar.setTextPanel(textPanel);

        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textPanel.appendText(Dice.dieList.get(0).roll() + "");
            }
        });

        setSize(1280,720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}
