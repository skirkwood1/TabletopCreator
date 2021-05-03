package Models;

import java.awt.image.BufferedImage;

public abstract class CardInterface implements GameComponent {
    public abstract CardInterface copy();
    public abstract void flip();
    public abstract boolean isFlipped();
}
