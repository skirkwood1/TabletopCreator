package UI.BoardPaneObjects;

import Models.Resource;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.Ellipse2D;

public class ResourceDrawer implements DrawerInterface {

    private final int MARGIN = 10;

    private Resource resource;

    private Point point;
    private Point mousePoint;

    private double zoom;

    private Ellipse2D plusButton,minusButton;
    private Rectangle bounds;

    public ResourceDrawer(Resource resource, Point point){
        this.resource = resource;
        this.point = new Point(point.x-30,point.y-30);
        this.mousePoint = new Point(0,0);
    }

    public void move(Point point){
        this.point = point;
    }

    public void draw(Graphics g,double zoom){
        this.zoom = zoom;

        Graphics2D g2 = (Graphics2D)g.create();

        g2.setFont(new Font("Segoe UI",Font.PLAIN,12));
        g2.setColor(Color.BLACK);

        FontRenderContext frc = g2.getFontRenderContext();

        String str = resource.getName();
        GlyphVector gv = g2.getFont().createGlyphVector(frc, resource.getName());

        String str2 = "Value: " + resource.getValue();
        GlyphVector gv2 = g2.getFont().createGlyphVector(frc, "Value: " + resource.getValue());
        //gv.getOutline();

        //int width =  g2.getFontMetrics().stringWidth(str);

//        Rectangle nameBounds = gv.getPixelBounds(null,
//                (float)point.getX(),
//                (float)point.getY());

        int width = 80 > g2.getFontMetrics().stringWidth(str) ? 80 : g2.getFontMetrics().stringWidth(str);

        this.bounds = new Rectangle((int)(point.getX()),
                (int)(point.getY()),
                width + 8,
                45);

        g2.setColor(Color.WHITE);
        g2.fill(this.bounds);
        g2.setColor(Color.BLACK);
        g2.draw(this.bounds);
        g2.drawLine(point.x,
                point.y + 12,
                point.x + width + 8,
                point.y + 12);

        g2.drawGlyphVector(gv,
                (int)(point.getX() + 4),
                (int)(point.getY()) + 10);

        g2.drawGlyphVector(gv2,
                point.x + 4,
                point.y + 30);

        this.plusButton = drawPlusSign(g,
                point.x + width,
                point.y + 22);
        this.minusButton = drawMinusSign(g,
                point.x + width,
                point.y + 36);

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

        if(ellipse.contains(mousePoint)){
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
            if(plusButton.contains(mousePoint)){
                resource.incrementValue();
            }else if(minusButton.contains(mousePoint)){
                resource.decrementValue();
            }
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

    public Resource getComponent(){
        return this.resource;
    }

}
