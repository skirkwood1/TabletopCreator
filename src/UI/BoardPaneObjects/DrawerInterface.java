package UI.BoardPaneObjects;

import java.awt.*;
import java.awt.event.MouseAdapter;

public interface DrawerInterface {
    void draw(Graphics g, double zoom);
    MouseAdapter getLocationTracker();
    Rectangle getBounds();
    Point getPoint();
    void move(Point point);
    Object getComponent();
}
