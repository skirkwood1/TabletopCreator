package UI;

import Models.Card;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;

public class CenterPane extends JPanel {
    private ComponentTree componentTree;
    private TextPanel textPanel;
    private JSplitPane overallPane;
    private JSplitPane textAndCardPane;
    private JSplitPane cardPane;
    private TextPanel cardText;
    private JLabel componentImage;
    private JScrollPane imagePane;

    public CenterPane() {
        cardText = new TextPanel();
        componentImage = new JLabel();
        textPanel = new TextPanel();
        componentTree = new ComponentTree();
        imagePane = new JScrollPane(componentImage);

        textPanel.setPreferredSize(new Dimension(800, 600));

        cardText.setPreferredSize(new Dimension(200,300));
        cardText.setMinimumSize(new Dimension(200,50));

        imagePane.setPreferredSize(new Dimension(200, 100));
        imagePane.setMinimumSize(new Dimension(200,100));
        componentImage.setHorizontalAlignment(SwingConstants.CENTER);
        componentImage.setVerticalAlignment(SwingConstants.CENTER);

        cardPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, cardText, imagePane);

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

        componentTree.getTree().addTreeSelectionListener(e -> {

            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)componentTree.getTree().getLastSelectedPathComponent();
            String cardName = selectedNode.toString();
            Card card = null;
            if(selectedNode.isLeaf()){
                card = Card.getCard(cardName);
            }
            if(card != null){
                displayImage(card.getImage());
                setCardText(card.getText());
            }
        });

    }


    public void appendText(String text){
        textPanel.appendText(text);
    }

    public void setCardText(String text){
        cardText.setText(text);
    }

    public void displayImage(File imageFile){
        try {
            Image image = ImageIO.read(imageFile);

            int baseHeight = ((BufferedImage) image).getHeight();
            int baseWidth = ((BufferedImage) image).getWidth();

            int imageSize;
            float imageScale;
            if(imagePane.getHeight()>imagePane.getWidth()){
                imageSize = imagePane.getWidth();
                imageScale = (float)imageSize/baseWidth;
            }
            else{
                imageSize = imagePane.getHeight();
                imageScale = (float)imageSize/baseHeight;
            }

            image = image.getScaledInstance((int)(baseHeight*imageScale),
                    (int)(((BufferedImage) image).getHeight()*imageScale),
                    Image.SCALE_SMOOTH);
            ImageIcon icon = new ImageIcon(image);
            componentImage.setIcon(icon);
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }

}
