package UI.UIHelpers.Icons;

import javax.swing.*;
import java.awt.*;

public class TreeExpandedIcon implements Icon {
    public void paintIcon(Component c, Graphics g, int x, int y){
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(Color.GRAY);
        int[] xVerts = {x,x+12,x+6};
        int[] yVerts = {y,y,y+10};
        g2.fillPolygon(xVerts,yVerts,3);
    }

    @Override
    public int getIconWidth() {
        return 12;
    }

    @Override
    public int getIconHeight() {
        return 10;
    }
}
