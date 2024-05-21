package UI.UIHelpers;

import UI.UIColorConstants;
import UI.UIHelpers.Icons.TreeCollapsedIcon;

import javax.swing.*;
import javax.swing.plaf.basic.BasicFileChooserUI;
import javax.swing.plaf.metal.MetalToggleButtonUI;
import java.awt.*;

public class FileChooserCreator {
    private static MetalToggleButtonUI mtbUI = new MetalToggleButtonUI() {
        @Override
        protected Color getSelectColor() {
            return UIColorConstants.buttonSelected;
        }
    };


    public static void setFileChooserElements(Component[] comps){
        for(Component comp: comps) {
            if(comp instanceof JScrollPane){
                JScrollPane scrollPane = (JScrollPane)comp;
                scrollPane.getVerticalScrollBar().setUI(ScrollBarUICreator.scrollBarUI());
            }
            if(comp instanceof JList){
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (InstantiationException e) {
                    throw new RuntimeException(e);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (UnsupportedLookAndFeelException e) {
                    throw new RuntimeException(e);
                }
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
                jb.setBackground(UIColorConstants.buttonUnselected);
                jb.setFocusPainted(false);
                jb.setMargin(new Insets(5,5,5,5));
                jb.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
                jb.addChangeListener(ButtonUICreator.getChangeListener());
            }

            if(comp instanceof  JToggleButton){
                JToggleButton jb = (JToggleButton)comp;
                //jb.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
                jb.setBackground(UIColorConstants.buttonUnselected);
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
