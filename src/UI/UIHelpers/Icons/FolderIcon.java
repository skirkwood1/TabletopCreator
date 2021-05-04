package UI.UIHelpers.Icons;

import javax.swing.*;
import java.awt.*;

public class FolderIcon implements Icon {
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

        g2.fillRect(x,y+4,16,12);
        g2.drawRect(x,y+4,16,12);

        g2.setColor(Color.GRAY);
        int[] xVerts = {x+8,x+16,x+16,x+11,x+8};
        int[] yVerts = {y+2,y+2,y,y,y+1};
        g2.fillPolygon(xVerts,yVerts,5);
        g2.drawPolygon(xVerts,yVerts,5);
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
