package Models;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public interface GameComponent {
    String getName();
    String getText();
    BufferedImage getImage();

    void setName(String name);
}