package UI;

import javax.swing.*;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import java.awt.*;

public class ColorChooseDialog extends JOptionPane {
    private JColorChooser colorChooser;

    private JPanel layout;

    public ColorChooseDialog(){
        this.colorChooser = new JColorChooser(Color.WHITE);
        this.layout = new JPanel(new BorderLayout());

        int length = 20;
        UIManager.put("ColorChooser.swatchesRecentSwatchSize", new Dimension(length, length));
        UIManager.put("ColorChooser.swatchesSwatchSize", new Dimension(length, length));

        AbstractColorChooserPanel[] oldPanels = colorChooser.getChooserPanels();
        colorChooser.setChooserPanels(new AbstractColorChooserPanel[] { oldPanels[0], oldPanels[1] });



        JLabel preview = new JLabel();
        preview.setPreferredSize(new Dimension(50,50));
        preview.setBorder(BorderFactory.createEmptyBorder(0,0,1,0));
        preview.setText("●");
        preview.setFont(new Font("Serif", Font.BOLD, 48));
        preview.setSize(new Dimension(300,300));

        this.colorChooser.setPreviewPanel(preview);

        layout.add(colorChooser,BorderLayout.CENTER);

        add(layout);
    }

    public int display(){
        int n = showConfirmDialog(null,layout,"Test",
                JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE);

        return n;
    }

    public Color getColor(){
        return colorChooser.getColor();
    }
}
