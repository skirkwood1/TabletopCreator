package UI.UIHelpers;

import UI.UIHelpers.Icons.TreeCollapsedIcon;

import javax.swing.*;
import javax.swing.plaf.basic.BasicFileChooserUI;
import javax.swing.plaf.metal.MetalToggleButtonUI;
import java.awt.*;

public class FileChooserCreator {
    private static MetalToggleButtonUI mtbUI = new MetalToggleButtonUI() {
        @Override
        protected Color getSelectColor() {
            return new Color(170,170,170);
        }
    };


    public static void setFileChooserElements(Component[] comps){
        for(Component comp: comps) {
            if(comp instanceof JScrollPane){
                JScrollPane scrollPane = (JScrollPane)comp;
                scrollPane.getVerticalScrollBar().setUI(ScrollBarUICreator.scrollBarUI());
            }
            if(comp instanceof Container){
                setFileChooserElements(((Container)comp).getComponents());
            }
            try{
                comp.setFont(new Font("Segoe UI",Font.PLAIN,12));
            } catch(Exception e){
                e.printStackTrace();
            }//do nothing
            try{
                comp.setBackground(Color.WHITE);
            }catch(Exception e){
                e.printStackTrace();
            }
            if(comp instanceof JComboBox){
                JComboBox comboBox = (JComboBox)comp;
                comboBox.setBorder(BorderFactory.createEmptyBorder());
                comboBox.setPopupVisible(false);
                comboBox.setFocusable(false);
                comboBox.setUI(ComboBoxUICreator.comboxBoxUI());
            }



            if(comp instanceof JButton){
                JButton jb = (JButton)comp;
                //jb.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
                jb.setBackground(new Color(220,220,220));
                jb.setFocusPainted(false);
                jb.setMargin(new Insets(5,5,5,5));
                jb.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
                jb.addChangeListener(ButtonUICreator.getChangeListener());
            }

            if(comp instanceof  JToggleButton){
                JToggleButton jb = (JToggleButton)comp;
                //jb.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
                jb.setBackground(new Color(220,220,220));
                jb.setFocusPainted(false);
                jb.setMargin(new Insets(5,5,5,5));
                jb.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
                jb.setUI(mtbUI);
            }

            if(comp instanceof JScrollBar){
                JScrollBar sb = (JScrollBar)comp;
                sb.setUI(ScrollBarUICreator.scrollBarUI());
            }

        }
    }
}
