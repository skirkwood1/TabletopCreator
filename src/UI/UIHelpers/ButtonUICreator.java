package UI.UIHelpers;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class ButtonUICreator {
    private static Color buttonBG = new Color(210,210,210);
    private static ChangeListener changeListener = new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
            AbstractButton button = (AbstractButton) e.getSource();
            ButtonModel model = button.getModel();

            if (model.isRollover()) {
                button.setBackground(Color.LIGHT_GRAY);
            } else {
                button.setBackground(buttonBG);
            }
        }
    };

    public static ChangeListener getChangeListener(){
        return changeListener;
    }
}
