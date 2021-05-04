package UI.UIHelpers.Icons;

import javax.swing.*;
import java.awt.*;

public class FileDetailViewIcon implements Icon {
    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(Color.DARK_GRAY);
        g2.setStroke(new BasicStroke(1.5f));
        //g2.drawLine(x+8,y+2,x+8,y+14);
        //g2.drawLine(11,16,11,6);

        g2.fillRect(x,y,4,4);
        g2.fillRect(x,y+6,4,4);
        g2.fillRect(x,y+12,4,4);

        g2.drawLine(x+6,y+2,x+15,y+2);
        g2.drawLine(x+6,y+8,x+15,y+8);
        g2.drawLine(x+6,y+14,x+15,y+14);
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
