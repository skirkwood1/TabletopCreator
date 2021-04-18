package Models;

import java.awt.image.BufferedImage;

public interface CardInterface {
    BufferedImage getImage();
    CardInterface copy();

    void flip();
    boolean isFlipped();
}
