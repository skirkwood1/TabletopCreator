package UI;

import javax.swing.*;
import java.awt.*;

public class ResourceCreationDialog extends JOptionPane {
    JTextField nameField;

    JTextField valueField;

    JPanel panel;

    private int value;
    String name;

    public ResourceCreationDialog(){
        this.panel = new JPanel();
        panel.setLayout(new BorderLayout());

        this.nameField = new JTextField();
        nameField.setPreferredSize(new Dimension(80,20));
        JLabel nameLabel = new JLabel("Name:");

        this.valueField = new JTextField();
        valueField.setPreferredSize(new Dimension(50,20));
        JLabel valueLabel = new JLabel("Starting value:");

        JPanel name = new JPanel(new FlowLayout(FlowLayout.CENTER));
        name.add(nameLabel);
        name.add(nameField);

        JPanel value = new JPanel(new FlowLayout(FlowLayout.CENTER));
        value.add(valueLabel);
        value.add(valueField);

        panel.add(name,BorderLayout.NORTH);
        panel.add(value,BorderLayout.SOUTH);
    }

    public int display(){
        int n = showConfirmDialog(null,panel,"Create Resource",
                JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE);

        return n;
    }

    public int getValueField(){
        return Integer.parseInt(valueField.getText());
    }

    public String getNameField(){
        return nameField.getText();
    }
}
