package UI;

import Models.Game;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.text.ParseException;

public class ResizeBoardPane extends JOptionPane {
//    JFormattedTextField widthField = new JFormattedTextField();
//    JFormattedTextField heightField = new JFormattedTextField();

    JTextField widthField = new JTextField();
    JTextField heightField = new JTextField();
    JPanel layout;



    public ResizeBoardPane(Game game) {
//        MaskFormatter mf;
//
//        try{
//            mf = new MaskFormatter("####");
//            //mf.setValidCharacters("0123456789");
//            this.widthField = new JFormattedTextField(mf);
//            this.heightField = new JFormattedTextField(mf);
//        }
//        catch(ParseException pe){}


        NumberFormat nf = NumberFormat.getNumberInstance();

        //JFormattedTextField widthField = new JFormattedTextField(nf);
        //JFormattedTextField heightField = new JFormattedTextField(nf);

        this.layout = new JPanel(new BorderLayout());

        widthField.setText("" + game.getBoard().getSize()[0]);
        heightField.setText("" + game.getBoard().getSize()[1]);

        layout.add(widthField,BorderLayout.NORTH);
        layout.add(heightField,BorderLayout.CENTER);

    }

    public int display(){
        int n = showConfirmDialog(null,layout,"Test",
                JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE);

        return n;
    }

    public int getDesiredWidth(){
        return Integer.parseInt(widthField.getText());
    }

    public int getDesiredHeight(){
        return Integer.parseInt(heightField.getText());
    }

    void clear(){
        heightField.setText(null);
        widthField.setText(null);
    }

}
