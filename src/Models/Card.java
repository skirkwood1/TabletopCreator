package Models;

import java.io.File;
import java.io.Serializable;

public class Card extends Component implements Serializable {

    public Card(String name, String text, String filename){
        super(name,text,filename);
    }
}
