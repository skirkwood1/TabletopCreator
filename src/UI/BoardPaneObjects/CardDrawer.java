package UI.BoardPaneObjects;

import Models.CardInterface;
import Models.Deck;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.GeneralPath;

public class CardDrawer implements DrawerInterface {

    private final int SCALE = 40;

    private Point point;
    private Point mousePoint;

    private double zoom;
    private Rectangle bounds;

    private CardInterface card;

    private MouseAdapter locationTracker = new MouseAdapter() {
        @Override
        public void mouseMoved(MouseEvent e) {
            mousePoint = new Point((int)(e.getX()/zoom),
                    (int)(e.getY()/zoom));
        }
    };

    public CardDrawer(CardInterface card,Point point){
        this.card = card;
        this.point = point;
        this.mousePoint = new Point(0,0);
    }

    @Override
    public void draw(Graphics g, double zoom) {
        Graphics2D g2 = (Graphics2D)g.create();
        this.zoom = zoom;

        g2.setFont(new Font("Segoe UI",Font.PLAIN,12));
        g2.setColor(Color.BLACK);

        Dimension d = scaleCard(this.card);

        this.bounds = new Rectangle(point.x,
                point.y,
                (int)d.getWidth(),
                (int)d.getHeight());

        g2.drawImage(card.getImage(),
                (int)point.getX(),
                (int)point.getY(),
                (int)bounds.getWidth(),
                (int)bounds.getHeight(),
                null);

        if(card instanceof Deck){
            Deck deck = (Deck)card;
            FontRenderContext frc = g2.getFontRenderContext();
            GlyphVector gv = g2.getFont().createGlyphVector(frc, deck.getName());
            Rectangle rect = gv.getPixelBounds(null,
                    point.x,
                    point.y);

            g2.drawGlyphVector(gv,point.x,point.y+1);
            if(rect.width/zoom < bounds.width){
                g2.drawLine(
                        (int)((point.x +(rect.width)/zoom)),
                        point.y-(int)(rect.getHeight()/(3*zoom)),
                        point.x+bounds.width-8,
                        point.y-(int)(rect.getHeight()/(3*zoom)));
            }
            int[] xPoints = {
                    point.x + 1,
                    point.x + bounds.width-1,
                    point.x + bounds.width-3,
                    point.x + 3};
            int[] yPoints = {
                    point.y + bounds.height,
                    point.y + bounds.height,
                    point.y + bounds.height+4,
                    point.y + bounds.height+4};

            Stroke stroke = new BasicStroke(0.5f);
            Stroke oldStroke = g2.getStroke();
            g2.setStroke(stroke);
            GeneralPath polygon = new GeneralPath(GeneralPath.WIND_EVEN_ODD,xPoints.length);
            polygon.moveTo(xPoints[0],yPoints[0]);

            for (int index = 1; index < xPoints.length; index++) {
                polygon.lineTo(xPoints[index], yPoints[index]);
            }

            polygon.closePath();
            g2.draw(polygon);
            g2.setColor(Color.WHITE);
            g2.fill(polygon);

            g2.setColor(Color.BLACK);
            g2.drawLine(point.x + 2,
                    point.y + bounds.height + 2,
                    point.x + bounds.width - 2,
                    point.y + bounds.height + 2);

            g2.setStroke(oldStroke);
        }

        if(bounds.contains(mousePoint)){
            g2.draw(bounds);
        }
    }

    public Dimension scaleCard(CardInterface card){
        double width = SCALE*1.5;
        double heightScale = card.getImage().getWidth() / width;
        double height = card.getImage().getHeight() / heightScale;

        return new Dimension((int)width,(int)height);
    }

    public void move(Point point){
        this.point = point;
    }

    @Override
    public MouseAdapter getLocationTracker() {
        return this.locationTracker;
    }

    @Override
    public Rectangle getBounds() {
        return this.bounds;
    }

    @Override
    public Point getPoint() {
        return this.point;
    }

    public CardInterface getComponent(){
        return this.card;
    }
}
