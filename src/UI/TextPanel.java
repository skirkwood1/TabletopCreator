package UI;

import javax.swing.*;
import java.awt.*;

public class TextPanel extends JPanel {
    private JTextArea textArea;

    public TextPanel() {
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

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
        textArea.setText("\n\r\n\r\n\r\n\r" + currentText + "\n\r" + text);

    }
}
