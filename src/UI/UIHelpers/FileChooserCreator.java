package UI.UIHelpers;

import javax.swing.*;
import java.awt.*;

public class FileChooserCreator {
    public static void setFileChooserUI(Component[] comps){
        for(Component comp: comps) {
            if(comp instanceof Container){
                setFileChooserUI(((Container)comp).getComponents());
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
            if(comp instanceof JButton){
                JButton jb = (JButton)comp;
                //jb.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
                jb.setBackground(new Color(220,220,220));
            }
            else if(comp instanceof JScrollBar){
                JScrollBar sb = (JScrollBar)comp;
                sb.setUI(ScrollBarUICreator.scrollBarUI());
            }
        }
    }
}
