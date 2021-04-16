package UI.Factories;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

public class ScrollBarUICreator {
    public static BasicScrollBarUI scrollBarUI(){
        return new BasicScrollBarUI() {
            @Override
            protected JButton createDecreaseButton(int orientation) {
                return zeroButton();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return zeroButton();
            }

            @Override
            protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds)
            {
            }

            JButton zeroButton(){
                JButton jbutton = new JButton();
                jbutton.setPreferredSize(new Dimension(0, 0));
                jbutton.setMinimumSize(new Dimension(0, 0));
                jbutton.setMaximumSize(new Dimension(0, 0));
                return jbutton;
            }

            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = Color.LIGHT_GRAY;
            }
        };
    }
}
