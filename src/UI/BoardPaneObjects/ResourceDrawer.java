package UI.BoardPaneObjects;

import Models.ResourceSheet;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.Ellipse2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public class ResourceDrawer implements DrawerInterface, Serializable {

    private static final long serialVersionUID = 579324485687726774L;
    private final int MARGIN = 10;

    private ResourceSheet resourceSheet;

    private Point point;
    private Point mousePoint;
    private boolean mouseHeld;

    private double zoom;

    private Ellipse2D plusButton,minusButton;
    private final ArrayList<Ellipse2D> plusButtons,minusButtons;
    private Rectangle bounds;

    public ResourceDrawer(ResourceSheet resourceSheet){
        this.resourceSheet = resourceSheet;
        this.point = new Point(0,0);
        this.mousePoint = new Point(0,0);

        this.plusButtons = new ArrayList();
        this.minusButtons = new ArrayList();

        for(Map.Entry<String, Integer[]> resValue: resourceSheet.getResources().entrySet()){
            plusButtons.add(null);
            minusButtons.add(null);
        }
    }

    public ResourceDrawer(ResourceSheet resourceSheet, Point point){
        this.resourceSheet = resourceSheet;
        this.point = new Point(point.x-30,point.y-30);
        this.mousePoint = new Point(0,0);

        this.plusButtons = new ArrayList<>();
        this.minusButtons = new ArrayList<>();

        for(Map.Entry<String, Integer[]> resValue: resourceSheet.getResources().entrySet()){
            plusButtons.add(null);
            minusButtons.add(null);
        }
    }

    public void move(Point point){
        this.point = point;
    }

    public void updateMouse(Point point){
        this.mousePoint = point;
    }

    public void initializeBounds(Graphics g){
        g.setFont(new Font("Segoe UI",Font.PLAIN,10));
        String str = resourceSheet.getName();
        int width = Math.max(90, g.getFontMetrics().stringWidth(str) + 12);
        int resourceHeight = this.resourceSheet.getResources().size()*20;

        for(String resName: resourceSheet.getResources().keySet()){
            int stringWidth = g.getFontMetrics().stringWidth(resName) + 60;
            if(stringWidth > width){
                width = stringWidth;
            }
        }
        this.bounds = new Rectangle((int)(point.getX()),
                (int)(point.getY()),
                width,
                resourceHeight + 16);
    }

    public void draw(Graphics g,double zoom){
        this.zoom = zoom;
        Graphics2D g2 = (Graphics2D)g.create();

        initializeBounds(g);

        g2.setFont(new Font("Segoe UI",Font.PLAIN,10));
        g2.setColor(Color.BLACK);

        FontRenderContext frc = g2.getFontRenderContext();

        ArrayList<GlyphVector> valueLines = new ArrayList<>();

        GlyphVector gv = g2.getFont().createGlyphVector(frc, resourceSheet.getName());

        for(Map.Entry<String, Integer[]> resValue: resourceSheet.getResources().entrySet()){
            valueLines.add(g2.getFont().createGlyphVector(frc, resValue.getKey() + ": " + resValue.getValue()[0]));
        }
        //GlyphVector gv2 = g2.getFont().createGlyphVector(frc, "Value: " + resource.getValue());

        g2.setColor(Color.WHITE);
        g2.fill(this.bounds);
        g2.setColor(Color.BLACK);
        g2.draw(this.bounds);
        g2.drawLine(point.x,
                point.y + 12,
                point.x + bounds.width,
                point.y + 12);

        g2.drawGlyphVector(gv,
                (int)(point.getX() + 4),
                (int)(point.getY()) + 10);

        for(int i = 0; i < valueLines.size(); i++){
            g2.setFont(new Font("Segoe UI",Font.PLAIN,10));
            g2.setColor(Color.BLACK);
            g2.drawGlyphVector(valueLines.get(i),
                    point.x + 4,
                    point.y + 8 + (20*(i+1)));

//            this.plusButton = drawPlusSign(g,
//                    point.x + bounds.width - 8,
//                    point.y + 5 + (20*(i+1)));
//            this.minusButton = drawMinusSign(g,
//                    point.x + bounds.width - 22,
//                    point.y + 5 + (20*(i+1)));

            plusButtons.set(i, drawPlusSign(g,
                    point.x + bounds.width - 10,
                    point.y + 4 + (20*(i+1))));
            minusButtons.set(i, drawMinusSign(g,
                    point.x + bounds.width - 24,
                    point.y + 4 + (20*(i+1))));
        }

        if(bounds.contains(mousePoint)){
            g2.draw(bounds);
        }
    }

    private Ellipse2D drawPlusSign(Graphics g, int x, int y){
        Ellipse2D ellipse = drawButton(g,x,y);

        Graphics2D g2 = (Graphics2D)g.create();
        g2.setColor(Color.BLACK);

        g2.drawLine(x,y-3,x,y+3);
        g2.drawLine(x-3,y,x+3,y);

        return ellipse;
    }

    private Ellipse2D drawMinusSign(Graphics g, int x, int y){
        Ellipse2D ellipse = drawButton(g,x,y);

        Graphics2D g2 = (Graphics2D)g.create();
        g2.setColor(Color.BLACK);

        g2.drawLine(x-3,y,x+3,y);

        return ellipse;
    }

    private Ellipse2D drawButton(Graphics g, int x, int y){
        Graphics2D g2 = (Graphics2D)g.create();

        g2.setColor(Color.BLACK);

        Ellipse2D ellipse = new Ellipse2D.Double(x-6,y-6,12,12);

        if(ellipse.contains(mousePoint) & !mouseHeld){
            g2.setColor(new Color(100,200,255));
            g2.fill(ellipse);
            g2.setColor(Color.BLACK);
        }
        g2.draw(ellipse);
        return ellipse;
    }

    private MouseAdapter locationTracker = new MouseAdapter() {
        @Override
        public void mouseMoved(MouseEvent e) {
            mousePoint = new Point((int)(e.getX()/zoom),
                    (int)(e.getY()/zoom));
            //System.out.println(mousePoint);
        }

        public void mouseClicked(MouseEvent e){
            for(int i = 0; i < plusButtons.size(); i++){
                if(plusButtons.get(i).contains(mousePoint)){
                    resourceSheet.incrementValue(resourceSheet.getEntry(i).getKey());
                }else if(minusButtons.get(i).contains(mousePoint)){
                    resourceSheet.decrementValue(resourceSheet.getEntry(i).getKey());
                }
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            mouseHeld = true;
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            mouseHeld = false;
        }
    };

    public MouseAdapter getLocationTracker(){
        return this.locationTracker;
    }

    public Rectangle getBounds(){
        return this.bounds;
    }

    public Point getPoint(){
        return this.point;
    }

    public ResourceSheet getComponent(){
        return this.resourceSheet;
    }
}
