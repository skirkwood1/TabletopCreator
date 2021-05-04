package UI.UIHelpers.Icons;

import javax.swing.*;
import java.awt.*;

public class UpIcon implements Icon {

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(Color.DARK_GRAY);
        g2.setStroke(new BasicStroke(3f));
        g2.drawLine(x+8,y,x+8,y+16);
        //g2.drawLine(11,16,11,6);

        g2.drawLine(x+2,y+6,x+8,y);
        g2.drawLine(x+14,y+6,x+8,y);
//        int[] xVerts = {x+2,x+14,x+8};
//        int[] yVerts = {y+8,y+8,y};
//        g2.fillPolygon(xVerts,yVerts,3);
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
