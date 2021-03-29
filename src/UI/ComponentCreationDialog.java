package UI;

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
        this.name = new JTextField();
        this.text = new JTextArea();
        //this.confirmButton = new JButton();
        this.layout = new JPanel(new BorderLayout());
        this.imageChooser = new JFileChooser();

        text.setPreferredSize(new Dimension(300,300));

        layout.add(name,BorderLayout.NORTH);
        layout.add(text,BorderLayout.CENTER);
        layout.add(imageChooser,BorderLayout.SOUTH);
        //layout.add(confirmButton,BorderLayout.SOUTH);
    }

    public int display(){
        int n = showConfirmDialog(null,layout,"Component Import",
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
