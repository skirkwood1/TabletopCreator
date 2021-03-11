package UI;
import Commands.CommandStack;
import Commands.PlaceSpaceCommand;
import Models.Board;
import Models.Game;
import Models.Space;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class BoardPane extends JPanel {

    private Game game;
    private Dimension dimension;

    public double zoom = 1.0;

    public BoardPane(Game game) {
        this.game = game;
        this.dimension = new Dimension(game.getBoard().getSize()[0]*40+40,game.getBoard().getSize()[1]*40+40);
        setPreferredSize(dimension);
        setSize(dimension);

        setLayout(new GridBagLayout());

        addMouseListener(ma);

    }

    MouseAdapter ma = new MouseAdapter() {

        @Override
        public void mouseClicked(MouseEvent e) {
            Graphics g = getGraphics();
            Graphics2D g2 = (Graphics2D)g;

            g2.setColor(game.getBoard().getColor());
            g2.scale(zoom,zoom);

            // get X and y position on board
            int x, y;
            x = (int)(((e.getX()/zoom-20)/40));
            y = (int)(((e.getY()/zoom-20)/40)); ///zoom);

            // draw a Oval at the point
            // where mouse is moved
            int[] size = game.getBoard().getSize();
            if(x < size[0] && y < size[1]){
                //g2.fillRect((int)((x*40+20)*zoom), (int)((y*40+20)*zoom), (int)(40*zoom), (int)(40*zoom));
                g2.fillRect(x*40+20,y*40+20,40,40);
                g2.setColor(Color.BLACK);
                g2.drawRect(x*40+20,y*40+20,40,40);

                PlaceSpaceCommand psc = new PlaceSpaceCommand(game,x,y);
                CommandStack.insertCommand(psc);

                System.out.println("Placed space at " + x + ", " + y);

                //game.getBoard().setSquare(x,y,game.getBoard().getColor());

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

    public void paintComponent(Graphics g) {

        Graphics2D g2 = (Graphics2D)g;

        setBackground(Color.WHITE);

        int[] boardSize = game.getBoard().getSize();
        int width = boardSize[0];
        int height = boardSize[1];

        g2.scale(zoom,zoom);



        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Space space = game.getBoard().getSpace(i, j);
                Color color = space.getColor();
                g2.setColor(color);
                g2.fillRect(i*40+20,j*40+20,40,40);

                g2.setColor(Color.BLACK);
                g2.drawRect(i*40+20,j*40+20,40,40);
            }
        }
    }

    public void setZoom(double scale){
        this.zoom = scale;
    }

    public void updateSize(){
        //this.dimension = new Dimension(board.getSize()[0]*40+40,board.getSize()[1]*40+40);


        double x = (this.game.getBoard().getSize()[0] * 40 + 40) * zoom;
        double y = (this.game.getBoard().getSize()[1] * 40 + 40) * zoom;

        setPreferredSize(new Dimension((int)x,(int)y));
        setSize(new Dimension((int)x,(int)y));
    }


}
