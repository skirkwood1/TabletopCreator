package ChatServer;

import java.io.Serializable;

public class QuitMessage implements Message, Serializable {
    public String getMessage(){
        return "quit";
    }
}
