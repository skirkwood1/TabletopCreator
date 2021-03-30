package UI;

import Models.Card;
import Models.Game;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.JList;

public class DeckCreationDialog extends JOptionPane {
    private JTextField name;
    private JList<Card> cards;
    private JPanel layout;

    private JPanel numberPanel;
    private JTextField number;
    private JLabel numberLabel;

    private JScrollPane listScroller;

    public DeckCreationDialog(Game game){
        this.name = new JTextField();
        this.cards = new JList<>();
        this.layout = new JPanel(new BorderLayout());

        this.numberPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        this.number = new JTextField();

        number.setPreferredSize(new Dimension(50,20));
        this.numberLabel = new JLabel("Copies of Each:");
        numberLabel.setPreferredSize(new Dimension(100,20));

        numberPanel.add(numberLabel);
        numberPanel.add(number);

        layout.setPreferredSize(new Dimension(300,300));

        DefaultListModel<Card> dlm = new DefaultListModel<>();

        ArrayList<Card> cardList = game.getCards();
        for(Card card: cardList){
            dlm.addElement(card);
        }

        cards.setModel(dlm);

        this.listScroller = new JScrollPane(cards);
        layout.add(name,BorderLayout.NORTH);
        layout.add(listScroller,BorderLayout.CENTER);
        layout.add(numberPanel,BorderLayout.SOUTH);
    }


    public int display(){
        int n = showConfirmDialog(null,layout,"Create Deck",
                JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE);

        return n;
    }

    public int displayNoName(){
        this.layout.remove(name);

        int n = showConfirmDialog(null,layout,"Create Deck",
                JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE);

        return n;
    }

    public ArrayList<Card> getSelection(){
        //ArrayList<Card> cards = new ArrayList<>();
        return (ArrayList<Card>)this.cards.getSelectedValuesList();
    }

    public Integer getNumCopies(){
        try{
            return Integer.parseInt(this.number.getText());
        }catch(NumberFormatException e){
            return null;
        }
    }

    public String getDeckName(){
        return this.name.getText();
    }


}
