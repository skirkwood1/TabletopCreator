package Models;

import java.io.File;
import java.io.Serializable;

public class Piece extends Component implements Serializable {
    private static final long serialVersionUID = -497535162892637357L;

    public Piece(String name, String text, String filename){
        super(name,text,filename);
    }
}