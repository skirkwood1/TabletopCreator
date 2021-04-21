package ChatServer;

import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class ChatGameButton extends JButton {
    ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("ProgramIcon.png"));

    private float fileSize;

    public ChatGameButton(){
        super();
        this.fileSize = 0f;
    }

    @Override
    protected void paintComponent(Graphics g){
        Graphics2D g2 = (Graphics2D) g.create();

        RenderingHints rh = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHints(rh);

        g2.setPaint(new Color(50,150,255));
        g2.fillRoundRect(1, 1, getWidth()-2, getHeight()-2, 10, 10);

        if(getModel().isRollover()){
            g2.setPaint(new Color(50,150,255));
        }else{
            g2.setPaint(new Color(50,220,255));
        }

        g2.fillRoundRect(1, 1, getWidth()-12, getHeight()-2, 10, 10);
        g2.drawImage(icon.getImage(),5,getHeight()/2 - icon.getIconHeight()/8,icon.getIconWidth()/4,icon.getIconHeight()/4,null);
        g2.setPaint(Color.BLACK);

        g2.setFont(new Font("Segoe UI",Font.PLAIN,16));
        FontRenderContext frc = g2.getFontRenderContext();

        GlyphVector gv = g2.getFont().createGlyphVector(frc, getText());
        Rectangle rect = gv.getPixelBounds(null,0,0);
        g2.drawGlyphVector(gv, 60, getHeight()/2f+(float)rect.getHeight()/2);

        g2.setFont(new Font("Segoe UI",Font.PLAIN,13));
        frc = g2.getFontRenderContext();
        gv = g2.getFont().createGlyphVector(frc,String.format("File Size: %.2fMB",fileSize));
        rect = gv.getPixelBounds(null,0,0);
        g2.drawGlyphVector(gv, (float)(getWidth()-rect.getWidth()+10), getHeight()-5);

        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g){
        Graphics2D g2 = (Graphics2D)g.create();

        RenderingHints rh = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHints(rh);

        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(2f));
        g2.drawRoundRect(1, 1, getWidth()-3, getHeight()-3, 10, 10);
    }

    public void setDataSize(long size){
        this.fileSize = size/1024f/1024f;
    }
}
