package UI.UIHelpers.Icons;

import javax.swing.*;
import java.awt.*;

public class HomeIcon implements Icon {
    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(Color.DARK_GRAY);
        g2.setStroke(new BasicStroke(2f));
        //g2.drawLine(x+8,y+2,x+8,y+14);
        //g2.drawLine(11,16,11,6);

        g2.fillRect(x+2,y+6,12,12);

        int[] xVerts = {x,x+16,x+8};
        int[] yVerts = {y+8,y+8,y};
        g2.fillPolygon(xVerts,yVerts,3);
        g2.drawPolygon(xVerts,yVerts,3);
    }

    @Override
    public int getIconWidth() {
        return 16;
    }

    @Override
    public int getIconHeight() {
        return 16;
    }
}
