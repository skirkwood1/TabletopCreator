package UI.Listeners;

import Observers.Observer;
import UI.BoardPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;

public class BoardPaneViewScroll extends MouseAdapter {

        //private Point origin;
        private Point holdPoint;

        private int mouseButton = 0;
        private double zoom = 1.0;

        private JScrollPane boardScreen;
        private BoardPane boardPane;
        private ArrayList<Observer> observers;

        public BoardPaneViewScroll(JScrollPane boardScreen, BoardPane boardPane, ArrayList<Observer> observers){
            this.boardScreen = boardScreen;
            this.boardPane = boardPane;
            this.observers = observers;
        }

        @Override
        public void mousePressed(MouseEvent e) {

            holdPoint = new Point(e.getPoint());
            //origin = new Point(e.getPoint());
            if((e.getModifiersEx() & InputEvent.BUTTON2_DOWN_MASK) != 0){
                mouseButton = 3;
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            mouseButton = 0;
            boardScreen.setCursor(null);

            notifyObservers();
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (mouseButton == 3) {
                boardScreen.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));

                Point dragEventPoint = e.getPoint();
                JViewport viewport = (JViewport) boardPane.getParent();
                Point viewPos = viewport.getViewPosition();
                int maxViewPosX = boardPane.getWidth() - viewport.getWidth();
                int maxViewPosY = boardPane.getHeight() - viewport.getHeight();

                if (boardPane.getWidth() > viewport.getWidth()) {
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

                if (boardPane.getHeight() > viewport.getHeight()) {
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
            else if (mouseButton == 1){

            }
            notifyObservers();
        }
        public void mouseMoved(MouseEvent e){
            notifyObservers();
        }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {

        zoom -= e.getWheelRotation() * 0.1;
        if(zoom < 0.1){
            zoom = 0.1;
        }
        boardPane.setZoom(zoom);

        boardPane.updateSize();

        boardScreen.setViewportView(boardPane);

        boardPane.removeAll();
        boardPane.revalidate();
        boardPane.repaint();

        boardScreen.revalidate();
        boardScreen.repaint();

    }

    public void setZoom(double zoom){
            this.zoom = zoom;
    }

    public double getZoom(){
            return this.zoom;
    }

        private void notifyObservers(){
            for(Observer obs: observers){
                obs.update();
            }
        }


}
