package UI.UIHelpers.Icons;

import javax.swing.*;
import java.awt.*;

public class TreeIcon implements Icon {

    private static int SIZE = 0;

    public TreeIcon() {
    }

    public int getIconWidth() {
        return SIZE;
    }

    public int getIconHeight() {
        return SIZE;
    }

    public void paintIcon(Component c, Graphics g, int x, int y) {
        //System.out.println(c.getWidth() + " " + c.getHeight() + " " + x + " " + y);
    }
}
