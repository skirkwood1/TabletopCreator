package Models;

import java.io.File;
import java.io.Serializable;

public class Piece extends Component implements Serializable {

    public Piece(String name, String text, String filename){
        super(name,text,filename);
    }

}