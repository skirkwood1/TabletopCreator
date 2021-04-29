package UI;

import Models.Card;
import Models.Game;
import Models.Resource;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class PlayerCreationDialog extends JOptionPane {
    private final JTextField name;
    private JList<Resource> resources;
    private final JPanel layout;

    public PlayerCreationDialog(Game game){
        this.name = new JTextField();
        this.resources = new JList<>();
        this.layout = new JPanel(new BorderLayout());

        layout.setPreferredSize(new Dimension(300,300));

        DefaultListModel<Resource> dlm = new DefaultListModel<>();

        ArrayList<Resource> resourceList = game.getResources();
        for(Resource resource: resourceList){
            dlm.addElement(resource);
        }

        resources.setModel(dlm);

        JScrollPane listScroller = new JScrollPane(resources);
        layout.add(name,BorderLayout.NORTH);
        layout.add(listScroller,BorderLayout.CENTER);
    }

    public int display(){
        int n = showConfirmDialog(null,layout,"Create Deck",
                JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE);

        return n;
    }

    public ArrayList<Resource> getSelection(){
        //ArrayList<Card> cards = new ArrayList<>();
        return (ArrayList<Resource>)this.resources.getSelectedValuesList();
    }

    public String getPlayerName(){
        return this.name.getText();
    }


}
