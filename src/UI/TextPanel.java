package UI;

import javax.swing.*;
import java.awt.*;

import static java.awt.Color.white;

public class TextPanel extends JPanel {
    private JTextArea textArea;

    public TextPanel() {
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        //textArea.setBorder(BorderFactory.createEmptyBorder());
        textArea.setMargin(new Insets(0,2,0,5));
        textArea.setAutoscrolls(true);

        setLayout(new BorderLayout());

        add(new JScrollPane(textArea), BorderLayout.CENTER);
    }

    public void appendText(String text){
        textArea.append(text);
    }

    public void setText(String text) {textArea.setText(text);}

    public void appendBottomText(String text){
        String currentText = textArea.getText();
        currentText = currentText.strip();

        textArea.setText("\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r" + currentText + "\n\r" + text);

    }
}
