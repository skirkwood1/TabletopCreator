package Models;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public interface Component {
    String getName();
    String getText();
    BufferedImage getImage();
}
