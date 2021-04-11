package UI.Listeners;

import Models.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;

public class ImageViewScroll extends MouseAdapter {
    private Point holdPoint;

    private final JScrollPane imagePane;
    private final JLabel componentImage;
    private final Game game;

    private double imageZoom = 1.0;

    public ImageViewScroll(Game game, JScrollPane imagePane, JLabel componentImage){
        this.game = game;
        this.imagePane = imagePane;
        this.componentImage = componentImage;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        holdPoint = new Point(e.getPoint());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        imagePane.setCursor(null);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        imagePane.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));

        Point dragEventPoint = e.getPoint();
        JViewport viewport = (JViewport) componentImage.getParent();
        Point viewPos = viewport.getViewPosition();
        int maxViewPosX = componentImage.getWidth() - viewport.getWidth();
        int maxViewPosY = componentImage.getHeight() - viewport.getHeight();

        if (componentImage.getWidth() > viewport.getWidth()) {
            viewPos.x -= dragEventPoint.x - holdPoint.x;

            if (viewPos.x < 0) {
                viewPos.x = 0;
                holdPoint.x = dragEventPoint.x;
            }

            if (viewPos.x > maxViewPosX) {
                viewPos.x = maxViewPosX;
                holdPoint.x = dragEventPoint.x;
            }
        }

        if (componentImage.getHeight() > viewport.getHeight()) {
            viewPos.y -= dragEventPoint.y - holdPoint.y;

            if (viewPos.y < 0) {
                viewPos.y = 0;
                holdPoint.y = dragEventPoint.y;
            }

            if (viewPos.y > maxViewPosY) {
                viewPos.y = maxViewPosY;
                holdPoint.y = dragEventPoint.y;
            }
        }

        viewport.setViewPosition(viewPos);
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {

        imageZoom -= e.getWheelRotation() * 0.1;
        if(imageZoom < 0.1){
            imageZoom = 0.1;
        }

        BufferedImage image = game.getSelectedComponent().getPicture(); // transform it
        if(image == null){
            image = game.getBoard().getTextureImage();
        }

        Image newimg = image.getScaledInstance((int)(image.getWidth()*imageZoom), (int)(image.getHeight()*imageZoom),  Image.SCALE_SMOOTH); // scale it the smooth way

        ImageIcon icon = new ImageIcon(newimg);
        componentImage.setIcon(icon);

    }
}
