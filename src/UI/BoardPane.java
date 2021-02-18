package UI;
import Models.Board;
import Models.Space;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import javax.swing.*;

public class BoardPane extends JPanel {

    private Board board;

    public BoardPane(Board board) {
        this.board = board;
    }

    public void paintComponent(Graphics g) {

        setBackground(Color.WHITE);

        int[] boardSize = board.getSize();
        int width = boardSize[0];
        int height = boardSize[1];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Space space = board.getSpace(i, j);
                Color color = space.getColor();
                g.setColor(color);
                g.fillRect(i * 40, j * 40, 40, 40);
            }
        }

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Graphics g = getGraphics();

                Color color = Color.red;
                g.setColor(color);

                // get X and y position
                int x, y;
                x = (e.getX()/40);
                y = (e.getY()/40);

                // draw a Oval at the point
                // where mouse is moved
                int[] size = board.getSize();
                if(x < size[0] && y < size[1]){
                    g.fillRect(x*40, y*40, 40, 40);
                    board.setSquare(x,y,color);
                }

            }

            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
            }

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                super.mouseWheelMoved(e);
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
            }
        });
        //addMouseMotionListener(this);

    }


}
