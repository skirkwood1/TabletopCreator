package UI;

import Models.Card;
import Models.Game;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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

    Game game;

    public CenterPane(Game game) {
        this.game = game;

        cardText = new TextPanel();
        componentImage = new JLabel();
        textPanel = new TextPanel();
        componentTree = new ComponentTree(game);
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

        overallPane.setContinuousLayout(true);

        add(overallPane);

        componentTree.getTree().addTreeSelectionListener(e -> {

            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)componentTree.getTree().getLastSelectedPathComponent();
            if(selectedNode != null){
                String cardName = selectedNode.toString();
                Card card = null;
                if(selectedNode.isLeaf()){
                    card = game.getCard(cardName);
                }
                if(card != null){
                    displayImage(card.getImage());
                    setCardText(card.getText());
                }
            }
        });

        componentImage.setAutoscrolls(true);

        MouseAdapter ma = new MouseAdapter() {

            private Point origin;

            @Override
            public void mousePressed(MouseEvent e) {
                origin = new Point(e.getPoint());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (origin != null) {
                    JViewport viewPort = (JViewport) SwingUtilities.getAncestorOfClass(JViewport.class, componentImage);
                    if (viewPort != null) {
                        int deltaX = origin.x - e.getX();
                        int deltaY = origin.y - e.getY();

                        Rectangle view = viewPort.getViewRect();
                        view.x += deltaX;
                        view.y += deltaY;

                        componentImage.scrollRectToVisible(view);
                    }
                }
            }

        };

        componentImage.addMouseListener(ma);
        componentImage.addMouseMotionListener(ma);

        imagePane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        imagePane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);



    }


    void appendText(String text){
        textPanel.appendText(text);
    }

    void setCardText(String text){
        cardText.setText(text);
    }

    void displayImage(File imageFile){
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

    void updateComponentTree(Card card){
        componentTree.updateTree(card);
    }

    public void collapseComponentTree(){componentTree.collapseTree();}

    void refreshComponentTree(Game game){
        this.game = game;
        componentTree.refreshTree(game);
    }

}
