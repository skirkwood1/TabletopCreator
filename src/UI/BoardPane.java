package UI;
import Models.Board;
import Models.Space;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class BoardPane extends JPanel {

    private Board board;
    private Dimension dimension;

    public double zoom = 1.0;

    public BoardPane(Board board) {
        this.board = board;
        this.dimension = new Dimension(board.getSize()[0]*40+40,board.getSize()[1]*40+40);
        setPreferredSize(dimension);
        setSize(dimension);

        setLayout(new GridBagLayout());

    }

    public void paintComponent(Graphics g) {

        Graphics2D g2 = (Graphics2D)g;

        setBackground(Color.WHITE);

        int[] boardSize = board.getSize();
        int width = boardSize[0];
        int height = boardSize[1];

        g2.scale(zoom,zoom);



        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Space space = board.getSpace(i, j);
                Color color = space.getColor();
                g2.setColor(color);
                g2.fillRect(i*40+20,j*40+20,40,40);

                g2.setColor(Color.BLACK);
                g2.drawRect(i*40+20,j*40+20,40,40);
            }
        }

        MouseAdapter ma = new MouseAdapter() {
            private Point origin;
            private Point mousePt;

            //public double zoom = 1.0;

            @Override
            public void mouseClicked(MouseEvent e) {
                Graphics g = getGraphics();
                Graphics2D g2 = (Graphics2D)g;

                Color color = Color.red;
                g2.setColor(color);
                g2.scale(zoom,zoom);

                // get X and y position on board
                int x, y;
                x = (int)(((e.getX()/zoom-20)/40));
                y = (int)(((e.getY()/zoom-20)/40)); ///zoom);

                // draw a Oval at the point
                // where mouse is moved
                int[] size = board.getSize();
                if(x < size[0] && y < size[1]){
                    //g2.fillRect((int)((x*40+20)*zoom), (int)((y*40+20)*zoom), (int)(40*zoom), (int)(40*zoom));
                    g2.fillRect(x*40+20,y*40+20,40,40);
                    g2.setColor(Color.BLACK);
                    g2.drawRect(x*40+20,y*40+20,40,40);
                    board.setSquare(x,y,color);
                }

            }

            @Override
            public void mousePressed(MouseEvent e) {
                //origin = new Point(e.getPoint());
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
//

                super.mouseDragged(e);
            }


            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
            }

            //public double getZoom(){return zoom;}

        };

        //g2.scale(zoom,zoom);

        addMouseListener(ma);
        //addMouseMotionListener(this);



    }

    public void setZoom(double scale){
        this.zoom = scale;
    }


}
