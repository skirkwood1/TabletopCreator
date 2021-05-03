package UI.BoardPaneObjects;

import Models.Player;
import Models.Resource;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.util.ArrayList;

public class PlayerDrawer implements DrawerInterface {

    private Point point,mousePoint;
    private Player player;

    private ArrayList<ResourceDrawer> resources;
    private double zoom;

    private Rectangle bounds;

    private MouseAdapter locationTracker = new MouseAdapter() {
        @Override
        public void mouseMoved(MouseEvent e) {
            mousePoint = e.getPoint();
            for(ResourceDrawer rd: resources){
                rd.getLocationTracker().mouseMoved(e);
            }
        }
        @Override
        public void mouseClicked(MouseEvent e){
            for(ResourceDrawer rd: resources){
                rd.getLocationTracker().mouseClicked(e);
            }
        }
    };

    public PlayerDrawer(Player player, Point point){
        this.player = player;
        this.point = new Point(point.x,point.y);
        this.mousePoint = new Point(0,0);
        this.resources = new ArrayList<>();

        for(Resource resource: player.getResources()){
            resources.add(new ResourceDrawer(resource));
        }
    }

    @Override
    public void draw(Graphics g, double zoom) {
        this.zoom = zoom;

        Graphics2D g2 = (Graphics2D)g.create();

        g2.setFont(new Font("Segoe UI",Font.BOLD,13));

        FontRenderContext frc = g2.getFontRenderContext();

        String str = player.getName();
        GlyphVector gv = g2.getFont().createGlyphVector(frc, player.getName());

        int width = 5;

        for(ResourceDrawer rd: this.resources){
            rd.move(new Point(point.x + width,
                    point.y + 20));
            rd.initializeBounds(g);
            width += rd.getBounds().width + 5;
        }

        this.bounds = new Rectangle(point.x,
                point.y,
                width,
                70);
        g2.setColor(Color.WHITE);
        g2.fill(bounds);

        g2.setColor(Color.BLACK);
        g2.draw(bounds);

        g2.drawGlyphVector(gv,point.x+5,point.y+15);

        for(ResourceDrawer rd: this.resources){
            rd.draw(g,zoom);
        }
    }

    @Override
    public MouseAdapter getLocationTracker() {
        return locationTracker;
    }

    @Override
    public Rectangle getBounds() {
        return this.bounds;
    }

    @Override
    public Point getPoint() {
        return this.point;
    }

    @Override
    public void move(Point point) {
        this.point = point;
    }

    @Override
    public Player getComponent() {
        return this.player;
    }
}
