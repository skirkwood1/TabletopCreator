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

    private JTextField widthField = new JTextField();
    private JTextField heightField = new JTextField();

    private JTextField topMargin = new JTextField();
    private JTextField bottomMargin = new JTextField();
    private JTextField leftMargin = new JTextField();
    private JTextField rightMargin = new JTextField();
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
        UIManager.put("Label.font",new Font("Segoe UI",Font.BOLD,14));
        UIManager.put("TextField.font",new Font("Segoe UI",Font.PLAIN,14));
        UIManager.put("Button.background",Color.LIGHT_GRAY);
        UIManager.put("Button.font",new Font("Segoe UI",Font.PLAIN,14));

        NumberFormat nf = NumberFormat.getNumberInstance();

        //JFormattedTextField widthField = new JFormattedTextField(nf);
        //JFormattedTextField heightField = new JFormattedTextField(nf);

        this.layout = new JPanel();

        layout.setLayout(new BoxLayout(layout,BoxLayout.PAGE_AXIS));
        layout.add(Box.createVerticalGlue());

        widthField.setText("" + game.getBoard().getSize()[0]);
        heightField.setText("" + game.getBoard().getSize()[1]);
        topMargin.setText("" + game.getBoard().getMargins()[0]);
        bottomMargin.setText("" + game.getBoard().getMargins()[1]);
        leftMargin.setText("" + game.getBoard().getMargins()[2]);
        rightMargin.setText("" + game.getBoard().getMargins()[3]);

        widthField.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.GRAY));
        heightField.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.GRAY));
        topMargin.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.GRAY));
        bottomMargin.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.GRAY));
        leftMargin.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.GRAY));
        rightMargin.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.GRAY));

        Dimension textFieldDimension = new Dimension(150,20);

//        JPanel width = new JPanel(new FlowLayout(FlowLayout.RIGHT));
//        JLabel widthLabel = new JLabel("Width");
//        widthLabel.setFont(new Font("Segoe UI",Font.PLAIN,13));
//        width.add(widthLabel);
//        widthField.setPreferredSize(textFieldDimension);
//        width.add(widthField);

        JPanel width = setupTextField("Width",widthField);
        JPanel height = setupTextField("Height",heightField);
        JPanel top = setupTextField("Top",topMargin);
        JPanel bottom = setupTextField("Bottom",bottomMargin);
        JPanel left = setupTextField("Left",leftMargin);
        JPanel right = setupTextField("Right",rightMargin);

        layout.add(new JLabel("Board Size",SwingConstants.RIGHT));
        layout.add(width);
        layout.add(height);
        layout.add(new JLabel("Play-field Margins",SwingConstants.RIGHT));
        layout.add(top);
        layout.add(bottom);
        layout.add(left);
        layout.add(right);

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

    public int[] getMargins(){
        int[] margins = new int[4];
        margins[0] = Integer.parseInt(topMargin.getText());
        margins[1] = Integer.parseInt(bottomMargin.getText());
        margins[2] = Integer.parseInt(leftMargin.getText());
        margins[3] = Integer.parseInt(rightMargin.getText());

        return margins;
    }

    void clear(){
        heightField.setText(null);
        widthField.setText(null);
    }

    JPanel setupTextField(String text,JTextField field){
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI",Font.PLAIN,13));
        panel.add(label);
        field.setPreferredSize(new Dimension(100,20));
        panel.add(field);

        return panel;

    }

}
