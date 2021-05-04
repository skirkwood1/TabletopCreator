package UI;

import UI.UIHelpers.ScrollBarUICreator;

import javax.swing.*;
import java.awt.*;

import static java.awt.Color.white;

public class CommandLog extends JPanel {
    private JTextArea textArea;

    public CommandLog() {
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        textArea.setFont(new Font("Segoe UI",Font.PLAIN,12));

        UIManager.put("TextArea.border",BorderFactory.createEmptyBorder(2,2,2,2));

        //textArea.setBorder(BorderFactory.createEmptyBorder());
        //textArea.setMargin(new Insets(0,2,0,5));
        textArea.setAutoscrolls(true);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.getVerticalScrollBar().setUI(ScrollBarUICreator.scrollBarUI());

        setLayout(new BorderLayout());

        add(scrollPane, BorderLayout.CENTER);
    }

    public void appendText(String text){
        textArea.append(text + "\n\r");
    }

    public void setText(String text) {textArea.setText(text);}

    public void appendBottomText(String text){
        String currentText = textArea.getText();
        currentText = currentText.strip();

        textArea.setText("\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r" + currentText + "\n\r" + text);

    }
}