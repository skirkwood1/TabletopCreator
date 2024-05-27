package UI;

import UI.UIHelpers.FileChooserCreator;
import UI.UIHelpers.FileViewerUI;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class ComponentCreationDialog extends JOptionPane {
    private JTextField name;
    private JTextArea text;
    //    private JButton confirmButton;
    private JPanel layout;
    private JFileChooser imageChooser;

    public ComponentCreationDialog(){
        UIManager.put("OptionPane.background", UIColorConstants.background);
        UIManager.put("Panel.background", UIColorConstants.background);

        this.name = new JTextField();
        this.text = new JTextArea();
        //this.confirmButton = new JButton();
        this.layout = new JPanel(new BorderLayout());
        this.imageChooser = new JFileChooser();

        imageChooser.setControlButtonsAreShown(false);
        imageChooser.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        BorderLayout bl = (BorderLayout)layout.getLayout();
        bl.setVgap(-1);

        Component[] comp = imageChooser.getComponents();
        imageChooser.setBackground(Color.WHITE);
        imageChooser.setOpaque(true);
        //fileUpload.setBorder(BorderFactory.createEmptyBorder(5,10,5,10));
        imageChooser.setFileView(new FileViewerUI());
        FileChooserCreator.setFileChooserElements(comp);

        name.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        name.setFont(new Font("Segoe UI",Font.PLAIN,12));

        text.setPreferredSize(new Dimension(250,150));
        text.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        text.setFont(new Font("Segoe UI",Font.PLAIN,12));

        layout.setBackground(Color.WHITE);
        layout.add(name,BorderLayout.NORTH);
        layout.add(text,BorderLayout.CENTER);
        layout.add(imageChooser,BorderLayout.SOUTH);
        //layout.add(confirmButton,BorderLayout.SOUTH);

        this.setOpaque(false);
        this.setBackground(Color.WHITE);
    }

    public int display(){
        int n = showConfirmDialog(null,layout,"GameComponent Import",
                JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE);

        return n;
    }

    String getComponentText(){
        return text.getText();
    }

    String getComponentName(){
        return name.getText();
    }

    String getFileSelect(){
        try{
            return imageChooser.getSelectedFile().getCanonicalPath();
        }catch(IOException ex){
            ex.printStackTrace();
        }
        return null;
    }

    void clear(){
        name.setText(null);
        text.setText(null);
        imageChooser.setSelectedFile(null);
    }
}
