package UI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class ResourceCreationDialog extends JOptionPane {
    JTextField titleField;
    JScrollPane resourcesScrollPane;

    //JTextField valueField;

    JPanel panel;
    JPanel values;
    JButton addResourceButton;
    JButton removeResourceButton;

    GridBagConstraints constraints;
    Font font = new Font("Segoe UI",Font.PLAIN,12);



    private int value;
    String name;

    public ResourceCreationDialog(){
        this.panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JPanel firstRes = new JPanel();
        firstRes.setLayout(new GridLayout(1,4));
        firstRes.setBorder(new EmptyBorder(0,0,4,0));

        this.titleField = new JTextField();
        titleField.setPreferredSize(new Dimension(80,20));
        JLabel titleLabel = new JLabel("Resource Title:");
        JPanel name = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titleLabel.setFont(font);

        name.add(titleLabel);
        name.add(titleField);

        JLabel nameLabel = new JLabel("Name: ");
        nameLabel.setFont(font);
        nameLabel.setMaximumSize(new Dimension(40,20));
        nameLabel.setBorder(new EmptyBorder(0,40,0,0));

        JTextField firstKey = new JTextField();
        firstKey.setPreferredSize(new Dimension(80,20));

        JLabel valueLabel = new JLabel("Start value: ");
        valueLabel.setFont(font);
        valueLabel.setMaximumSize(new Dimension(40,20));
        valueLabel.setBorder(new EmptyBorder(0,20,0,0));

        JTextField firstValue = new JTextField();
        firstValue.setPreferredSize(new Dimension(80,20));

        firstRes.add(nameLabel);
        firstRes.add(firstKey);
        firstRes.add(valueLabel);
        firstRes.add(firstValue);

        this.values = new JPanel(new GridBagLayout());
        constraints = new GridBagConstraints();
        constraints.gridy = 0;
        //this.values.setLayout(new BoxLayout(values, BoxLayout.Y_AXIS));

        values.add(firstRes);

        this.resourcesScrollPane = new JScrollPane(values);
        resourcesScrollPane.setPreferredSize(new Dimension(400,300));

        this.addResourceButton = new JButton("+");
        this.addResourceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addResource();
            }
        });

        panel.add(name,BorderLayout.NORTH);
        panel.add(resourcesScrollPane,BorderLayout.CENTER);
        panel.add(addResourceButton,BorderLayout.SOUTH);

        panel.setPreferredSize(new Dimension(400,400));

    }

    public void addResource(){
        JPanel newPanel = new JPanel();
        newPanel.setLayout(new GridLayout(1,4));
        newPanel.setBorder(new EmptyBorder(0,0,4,0));

        constraints.gridy++;

        JTextField firstKey = new JTextField();
        firstKey.setPreferredSize(new Dimension(80,20));
        JTextField firstValue = new JTextField();
        firstValue.setPreferredSize(new Dimension(80,20));
        JLabel nameLabel = new JLabel("Name: ");
        nameLabel.setBorder(new EmptyBorder(0,40,0,0));
        nameLabel.setFont(font);
        JLabel valueLabel = new JLabel("Start value: ");
        valueLabel.setBorder(new EmptyBorder(0,20,0,0));
        valueLabel.setFont(font);

        newPanel.add(nameLabel);
        newPanel.add(firstKey);
        newPanel.add(valueLabel);
        newPanel.add(firstValue);

        values.add(newPanel,constraints);
        panel.add(resourcesScrollPane,BorderLayout.CENTER);
        panel.revalidate();
        //panel.repaint();
    }

    public int display(){
        int n = showConfirmDialog(null,panel,"Create Resource",
                JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE);

        return n;
    }

    public int getValueField(){
        JTextField valueField = (JTextField)((JPanel)values.getComponent(0)).getComponent(3);
        return Integer.parseInt(valueField.getText());
    }

    public String getTitleField(){
        return titleField.getText();
    }

    public LinkedHashMap<String, Integer[]> getValMap(){
        Component[] rows = values.getComponents();
        LinkedHashMap<String, Integer[]> valMap = new LinkedHashMap<>();

        for(Component row : rows){
            if(row instanceof JPanel){
                JTextField key = (JTextField)((JPanel) row).getComponent(1);
                JTextField value = (JTextField)((JPanel) row).getComponent(3);
                Integer[] vals = {
                        Integer.parseInt(value.getText()),
                        Integer.parseInt(value.getText())};
                valMap.put(key.getText(), vals);
            }
        }
        return valMap;
    }
}
