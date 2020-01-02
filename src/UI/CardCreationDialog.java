package UI;

import javax.swing.*;
import java.awt.*;

public class CardCreationDialog extends JOptionPane {
    private JTextField cardName;
    private JTextArea cardText;
//    private JButton confirmButton;
    private JPanel layout;
    private JFileChooser imageChooser;

    public CardCreationDialog(){
        this.cardName = new JTextField();
        this.cardText = new JTextArea();
        //this.confirmButton = new JButton();
        this.layout = new JPanel(new BorderLayout());
        this.imageChooser = new JFileChooser();

        cardText.setPreferredSize(new Dimension(300,300));

        layout.add(cardName,BorderLayout.NORTH);
        layout.add(cardText,BorderLayout.CENTER);
        layout.add(imageChooser,BorderLayout.SOUTH);
        //layout.add(confirmButton,BorderLayout.SOUTH);
    }

    public int display(){
        int n = showConfirmDialog(null,layout,"Test",
                JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE);

        return n;
    }

    String getCardText(){
        return cardText.getText();
    }

    String getCardName(){
        return cardName.getText();
    }

    String getFileSelect(){
        try{
            return imageChooser.getSelectedFile().getCanonicalPath();
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    void clear(){
        cardName.setText(null);
        cardText.setText(null);
        imageChooser.setSelectedFile(null);
    }
}
