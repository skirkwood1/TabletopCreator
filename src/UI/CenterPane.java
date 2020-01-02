package UI;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class CenterPane extends JPanel {
    private ComponentTree componentTree;
    private TextPanel textPanel;
    private JSplitPane overallPane;
    private JSplitPane textAndCardPane;
    private JSplitPane cardPane;
    private TextPanel cardText;
    private JLabel componentImage;

    public CenterPane() {
        cardText = new TextPanel();
        componentImage = new JLabel();
        textPanel = new TextPanel();
        componentTree = new ComponentTree();

        textPanel.setPreferredSize(new Dimension(800, 600));

        cardText.setPreferredSize(new Dimension(200,300));
        cardText.setMinimumSize(new Dimension(200,50));
        componentImage.setPreferredSize(new Dimension(200, 100));
        cardPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, cardText, componentImage);

        textAndCardPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, textPanel, cardPane);
        textAndCardPane.setResizeWeight(1);

        componentTree.setMinimumSize(new Dimension(50,0));
        componentTree.setMaximumSize(new Dimension(250,0));
        componentTree.setPreferredSize(new Dimension(150,0));
        componentTree.setSize(new Dimension(150,0));
        componentTree.revalidate();

        overallPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, componentTree, textAndCardPane);

        overallPane.setResizeWeight(0);
        overallPane.setPreferredSize(new Dimension(1200,600));
        overallPane.setDividerLocation(0.25);

        overallPane.addPropertyChangeListener("dividerLocation", e -> {
            int location = ((Integer)e.getNewValue()).intValue();

            if (location > 400)
            {
                JSplitPane splitPane = (JSplitPane)e.getSource();
                splitPane.setDividerLocation( 400 );
            }
        });

        setLayout(new FlowLayout(FlowLayout.LEFT));

        add(overallPane);
    }

    public void appendText(String text){
        textPanel.appendText(text);
    }

    public void appendCardText(String text){
        cardText.appendText(text);
    }

}
