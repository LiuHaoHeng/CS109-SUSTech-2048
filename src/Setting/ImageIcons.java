package Setting;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

public class ImageIcons implements Serializable {
    public static ImageIcon backImageDefault = new ImageIcon((new ImageIcon("Images\\Return1.png")).getImage().getScaledInstance(111, 77, Image.SCALE_SMOOTH));
    public static ImageIcon backImagePressed = new ImageIcon((new ImageIcon("Images\\Return2.png")).getImage().getScaledInstance(111, 77, Image.SCALE_SMOOTH));
}
