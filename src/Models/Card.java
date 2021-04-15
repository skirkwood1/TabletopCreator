package Models;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.Serializable;

public class Card extends Component implements Serializable {

    private static final long serialVersionUID = 469671549044569183L;

    public Card(String name, String text, String filename){
        super(name,text,filename);
    }

    public Card(String name, String text, BufferedImage image){
        super(name,text,image);
    }

    public Card copy(){
        return new Card(this.getName(),this.getText(),this.getPicture());
    }


}
