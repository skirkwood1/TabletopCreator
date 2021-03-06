package UI;

import Models.Card;
import Models.Game;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.JList;

public class DeckCreationDialog extends JOptionPane {
    private final JTextField name;
    private JList<Card> cards;
    private final JPanel layout;

    private final JTextField number;

    public DeckCreationDialog(Game game){
        this.name = new JTextField();
        this.cards = new JList<>();
        this.layout = new JPanel(new BorderLayout());

        JPanel numberPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        this.number = new JTextField();

        number.setPreferredSize(new Dimension(50,20));
        JLabel numberLabel = new JLabel("Copies of Each:");
        //numberLabel.setPreferredSize(new Dimension(120,20));

        numberPanel.add(numberLabel);
        numberPanel.add(number);

        layout.setPreferredSize(new Dimension(300,300));

        DefaultListModel<Card> dlm = new DefaultListModel<>();

        ArrayList<Card> cardList = game.getCards();
        for(Card card: cardList){
            dlm.addElement(card);
        }

        cards.setModel(dlm);

        JScrollPane listScroller = new JScrollPane(cards);
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
