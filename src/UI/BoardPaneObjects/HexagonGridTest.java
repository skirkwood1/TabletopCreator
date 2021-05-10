package UI.BoardPaneObjects;

import javax.sound.sampled.Line;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class HexagonGridTest {

    private Point mousePoint = new Point(0,0);
    private double zoom = 1.0;

    public ArrayList<Polygon> hexagons;

    private final double SCALE = 0.3;

    public HexagonGridTest(int rows, int columns){
        this.hexagons = new ArrayList<>();

        double radius = Math.sqrt(3) * 50;
        Point center = new Point(700,600);
        //double lineLength = Math.pow(hex1.xpoints[1]-hex1.xpoints[0],2);
        double height = Math.sqrt(3) * radius;
        double width = 2 * radius;

        for(int col = 0; col < columns; col++){
            double yoffset = height * col;
            for(int row = 0; row < rows; row++){
                double xvalue = center.x + .75*width*row;
                double yvalue = center.y;
                if(row%2 == 1){
                    yvalue += .5*height;
                }
                yvalue += yoffset;

                Point p = new Point((int)xvalue,
                        (int)yvalue
                );
                Polygon hex = createHexagon(p,radius);
                hexagons.add(hex);
            }
        }

    }

    public void draw(Graphics g, double zoom){
        Graphics2D g2 = (Graphics2D)g.create();
        this.zoom = zoom;

        g2.scale(SCALE,SCALE);
        g2.setStroke(new BasicStroke(2f));

        for(Polygon hex: hexagons){
            if(hex.contains(mousePoint)){
                g2.setColor(Color.RED);
            }else{
                g2.setColor(Color.WHITE);
            }

            g2.fill(hex);
            g2.setColor(Color.BLACK);
            g2.draw(hex);
        }

//        for(int col = 0; col < 10; col++){
//            double yoffset = height * col;
//            for(int row = 0; row < 10; row++){
//                double xvalue = center.x + .75*width*row;
//                double yvalue = center.y;
//                if(row%2 == 1){
//                    yvalue += .5*height;
//                }
//                yvalue += yoffset;
//
//                Point p = new Point((int)xvalue,
//                        (int)yvalue
//                );
//                Polygon hex = createHexagon(p,radius);
//                if(hex.contains(mousePoint)){
//                    g2.setColor(Color.RED);
//                }else{
//                    g2.setColor(Color.WHITE);
//                }
//
//                g2.fill(hex);
//                g2.setColor(Color.BLACK);
//                g2.draw(hex);
//            }
//        }

    }

    private Polygon createHexagon(Point center, double radius) {
        double theta = 2.0*Math.PI / 6.0;

        int[] xvals = new int[6];
        int[] yvals = new int[6];
        for(int i = 0; i < 6; i++){
            xvals[i] = (int)(radius * Math.cos(theta*i) + center.x);
            yvals[i] = (int)(radius * Math.sin(theta*i) + center.y);
        }

        Polygon hexagon = new Polygon(xvals,yvals,6);

        return hexagon;
    }

    private MouseAdapter locationTracker = new MouseAdapter() {
        @Override
        public void mouseMoved(MouseEvent e) {
            mousePoint = new Point((int)(e.getX()/zoom/SCALE),
                    (int)(e.getY()/zoom/SCALE));
            //System.out.println(mousePoint);
        }
    };

    public MouseAdapter getLocationTracker(){
        return this.locationTracker;
    }

}
